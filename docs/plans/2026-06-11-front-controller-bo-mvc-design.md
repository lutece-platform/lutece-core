# Design : Front-controller MVC Back-Office (suppression des JSP « fond de traitement »)

**Date :** 2026-06-11
**Statut :** POC validé en runtime (branche `poc/front-controller-bo-mvc`, pilote `ThemeJspBean`)
**Module :** lutece-core (cœur du framework)
**Version Lutece :** 8.x

## Contexte

Le modèle MVC Back-Office de Lutece v8 impose, pour **chaque** JspBean, un fichier
JSP boilerplate identique dans `webapp/jsp/admin/.../ManageX.jsp`. Ce fichier ne fait
que deux choses : appeler `processController()` (le routage `@View`/`@Action` est déjà
géré par le framework MVC) et enrober le résultat avec les includes
`AdminHeader.jsp` / `AdminFooter.jsp`. C'est l'anti-pattern à supprimer : un fichier
de présentation par contrôleur, mêlant appel de contrôleur (scriptlet EL) et mécanique
de chrome, dupliqué à l'identique partout.

Exemple actuel (`jsp/admin/theme/ManageThemes.jsp`) :

```jsp
<%@ page errorPage="../ErrorPage.jsp" %>
${ pageContext.setAttribute( 'strContent', themeJspBean.processController( pageContext.request, pageContext.response ) ) }
<jsp:include page="../AdminHeader.jsp" />
${ pageContext.getAttribute( 'strContent' ) }
<%@ include file="../AdminFooter.jsp" %>
```

## Décision

Introduire un **front-controller** (servlet unique) qui remplace la coquille JSP :
il résout le bean cible par **nom de route**, appelle `processController()`, puis
compose lui-même le chrome (header/footer). Aucune JSP à créer pour un nouveau
contrôleur.

- **Stratégie de routage :** registre par nom — `@Controller(name="...")`, URL
  `/jsp/admin/mvc/{name}`.
- **Compatibilité :** cohabitation totale. Les anciens beans + leurs JSP continuent
  de fonctionner sans modification ; la migration est progressive, sans régression.
- **Inchangé :** `processController`, `@View`/`@Action`, binding des paramètres,
  validation, token CSRF, RBAC. On retire uniquement l'intermédiaire JSP et on
  centralise le chrome.

### Pourquoi un front-controller

`processController()` fait déjà tout (init, RBAC via `@Controller.right()`, token CSRF,
dispatch `@View`/`@Action`, binding, validation). La JSP n'ajoute rien au routage : elle
ne fait que l'appeler. Le front-controller supprime donc l'intermédiaire vide et remet
le routage là où il est déjà décrit — dans les annotations du bean. C'est le pattern
Java EE canonique (Spring `DispatcherServlet`, JAX-RS, Jakarta MVC/Krazo), et c'est
déjà ce que fait le côté XPage (Front-Office) de Lutèce via le dispatch par `xpageName`.
On aligne simplement le Back-Office sur ce principe.

## Design

### Composants

| Élément | Type | Rôle |
|---|---|---|
| `@Controller(name="…")` | Modif annotation | Champ `name` optionnel = clé de route. `controllerJsp`/`controllerPath` deviennent facultatifs (`default ""`). |
| `MvcControllerRegistry` | Nouveau `@ApplicationScoped` | Scan CDI au démarrage : map `name → Bean`. Détection de collision (fail-fast). |
| `AdminMvcServlet` | Nouvelle servlet `/jsp/admin/mvc/*` | Front-controller : résout le bean, appelle `processController`, gère view/action, compose le chrome. |
| `PageFrameService` + `admin_page_frame.html` | Nouveau service + template | Compose `<head>` + styles + menu header + contenu + footer. Remplace `AdminHeader.jsp`/`AdminFooter.jsp`. |
| `getControllerBaseUrl()` + helpers d'URL | Modif `MVCAdminJspBean` | Base d'URL `jsp/admin/mvc/{name}` (si `name`) pour les liens href ; `getViewUrl` renvoie une cible same-dir pour les redirections (voir § Génération d'URL). |

### Flux d'une requête

```
GET /jsp/admin/mvc/theme?view=manageThemes
  → [filtres /jsp/admin/* : auth, multipart, XSS, token, encoding]   (hérités, rien à coder)
  → AdminMvcServlet
       name      = "theme"
       beanClass = registry.resolve(name)                 // 404 si inconnu
       ctrl      = CDI.current().select(beanClass).get()  // respecte @SessionScoped
       content   = ctrl.processController(request, response)  // init+RBAC+token+dispatch
       si response.isCommitted()  → ACTION : redirect déjà émis, fin
       sinon                      → page = pageFrameService.wrap(request, content)
                                     response.getWriter().write(page)   // VIEW
  catch AccessDeniedException → page de message "accès refusé" (AdminMessageService)
```

### Annotation `@Controller` (rétrocompatible)

```java
@Controller(
    name = "theme",                // NOUVEAU — clé de route, optionnel
    right = "CORE_THEME_MANAGEMENT",
    securityTokenEnabled = true
    // controllerJsp / controllerPath : default "" — non requis pour un nouveau bean
)
```

- `name()` → `default ""`. Clé de route du front-controller.
- Fallback : si `name` est vide, le registre utilise la valeur du `@Named` du bean.
- Les anciens beans gardent `controllerJsp`/`controllerPath` → leur JSP marche toujours.

### MvcControllerRegistry (`@ApplicationScoped`)

- `@Observes @Initialized(ApplicationScoped.class)` : interroge le `BeanManager`,
  filtre les beans assignables à `MVCAdminJspBean` portant `@Controller`.
- Construit `Map<String, Bean<?>>` : `name → bean` (ou nom du `@Named` si `name` vide).
- **Collision** : deux contrôleurs avec le même `name` → `AppException` au démarrage.
- `resolve(name)` → `Bean<?>` ; instanciation via `beanManager.getReference(...)`.

### Token CSRF

`processController` (ligne 141) enregistre les actions sous une clé. On adapte :

```java
String tokenKey = !_controller.name( ).isBlank( )
        ? _controller.name( )                                            // nouveau modèle
        : _controller.controllerPath( ) + _controller.controllerJsp( );  // legacy
```

Génération (`@View`) et validation (`@Action`) utilisent la même clé pour un bean
donné → cohérence garantie, aucun impact legacy.

### Servlet `AdminMvcServlet`

- Mappée sur `/jsp/admin/mvc/*` → hérite des filtres admin existants
  (`authenticationFilter`, `multipartFilterAdmin`, `safeRequestFilterAdmin`,
  `securityTokenFilterAdmin`, `encodingFilter`). **Aucune sécurité à réimplémenter.**
- `doGet` et `doPost` délèguent à une méthode `process()` commune.
- Distinction view/action : si `response.isCommitted()` après `processController`
  (les `@Action` appellent `redirect()` qui fait déjà `sendRedirect`), on ne rend rien ;
  sinon on compose et écrit le HTML de la vue.
- `AccessDeniedException` : capturée → redirection vers la page de message d'accès
  refusé (même comportement que `errorPage="ErrorPage.jsp"` aujourd'hui).

### Template `admin_page_frame.html`

Remplace `AdminHeader.jsp` + `AdminFooter.jsp`, centralisé une seule fois :

```
<!DOCTYPE html><html><head> … ${adminStyleSheets} </head>
  ${adminMenuHeader}
  ${content}            ← retour de processController
  ${adminMenuFooter}
</html>
```

Fragments produits par les mêmes méthodes qu'aujourd'hui :
`adminMenuJspBean.getAdminStyleSheets()`, `getAdminMenuHeader(request)`,
`getAdminMenuFooter(request)` → rendu identique à l'existant.

### Génération d'URL

```java
protected String getControllerBaseUrl( ) {
    return !_controller.name( ).isBlank( )
        ? "jsp/admin/mvc/" + _controller.name( )                        // nouveau modèle
        : _controller.controllerPath( ) + _controller.controllerJsp( ); // legacy inchangé
}
```

**Distinction cruciale entre redirection et lien href** (validée en runtime) :

- **`getActionUrl` / `getViewFullUrl`** (utilisés dans les **liens href** des templates) →
  chemin complet `jsp/admin/mvc/{name}?…`, résolu par le navigateur contre le
  `<base href>` de la page.
- **`getViewUrl`** (utilisé comme **cible de redirection** par `redirectView`/`redirect`) →
  chemin **relatif au même répertoire**, soit juste `{name}?view=…`. Indispensable car
  `HttpServletResponse.sendRedirect` résout l'URL relativement à l'URI courante
  (`/jsp/admin/mvc/{name}`), **pas** contre le `<base href>`. Un chemin complet ici
  produirait `/jsp/admin/mvc/jsp/admin/mvc/{name}` → 404.

C'est exactement la séparation qui existe déjà en legacy (`getControllerJsp()` = nom de
fichier same-dir pour la redirection vs `getControllerPath()+getControllerJsp()` = chemin
complet pour les liens).

## Migration d'un contrôleur — avant / après

| | AVANT | APRÈS |
|---|---|---|
| Annotation | `@Controller(controllerJsp="ManageThemes.jsp", controllerPath="jsp/admin/templates/", right="CORE_THEME_MANAGEMENT")` | `@Controller(name="theme", right="CORE_THEME_MANAGEMENT")` |
| Fichier JSP | `webapp/jsp/admin/theme/ManageThemes.jsp` | **supprimé** |
| Feature `<url>` | `jsp/admin/theme/ManageThemes.jsp` | `jsp/admin/mvc/theme` |
| Liens href (templates) | chemin codé en dur vers la JSP | `getActionUrl(...)` → `jsp/admin/mvc/theme?action=...` |
| Code Java (`@View`/`@Action`) | — | **inchangé** |

**Bilan migration d'un contrôleur existant :** 1 annotation éditée, 1 JSP supprimée,
1 URL de feature modifiée. **Nouveau** contrôleur : `@Controller(name=...)` + feature,
**zéro JSP**.

## Fichiers à créer / modifier

### À créer (fait dans le POC)
- [x] `src/java/fr/paris/lutece/portal/web/admin/AdminMvcServlet.java` — front-controller.
- [x] `src/java/fr/paris/lutece/portal/util/mvc/admin/MvcControllerRegistry.java` — registre CDI.
- [x] `src/java/fr/paris/lutece/portal/util/mvc/admin/PageFrameService.java` — composition du chrome.
- [x] `webapp/WEB-INF/templates/admin/admin_page_frame.html` — template de page.

### À modifier (fait dans le POC)
- [x] `src/java/fr/paris/lutece/portal/util/mvc/admin/annotations/Controller.java`
      — ajouter `name() default ""` ; passer `controllerJsp()`/`controllerPath()` en `default ""`.
- [x] `src/java/fr/paris/lutece/portal/util/mvc/admin/MVCAdminJspBean.java`
      — clé token (`getSecurityTokenKey()`) ; `getControllerBaseUrl()` ; `getViewUrl` (cible
      redirection same-dir) / `getActionUrl` / `getViewFullUrl` (liens href, chemin complet).
- [x] `webapp/WEB-INF/web.xml` — déclaration + mapping `AdminMvcServlet` sur `/jsp/admin/mvc/*`.

### Migration pilote (preuve de concept — validée en runtime)
- [x] `ThemeJspBean` (`name="theme"`) : vue `manageThemes` + action `modifyGlobalTheme`.
      Joignable via `jsp/admin/mvc/theme` **et** son ancienne JSP `ManageThemes.jsp`
      (cohabitation). Parcours complet vue → action → redirect → vue validé.

> Note : `AutoIncludeJspBean` avait d'abord été choisi comme pilote mais il n'a aucune
> `@View` par défaut (uniquement une `@Action` qui redirige vers un dashboard) —
> l'ouvrir « à vide » déclenche un NPE latent du core. Mauvais cas de test ; remplacé
> par `ThemeJspBean`, un vrai contrôleur de gestion.

## Hors périmètre (YAGNI)

- Refonte du modèle d'annotations vers Jakarta MVC pur (`@Path`/`@GET`/`@POST`).
- Migration de masse des 218 JSP du core (faite progressivement, contrôleur par contrôleur).
- Modification du modèle XPage (Front-Office), déjà en front-controller.
- Génération automatique de code / scaffolding (peut venir après, en s'appuyant sur ce socle).

## Risques & points de vigilance

- **Clé du token CSRF** : doit rester identique entre génération (`@View`) et
  validation (`@Action`) pour un même bean → assuré par le calcul unique de `tokenKey`.
- **`@SessionScoped` partagé** : le champ transient `_response` est réaffecté à chaque
  `processController` (déjà le cas) → pas d'effet de bord entre requêtes.
- **Multipart** : assuré par `multipartFilterAdmin` hérité du mapping `/jsp/admin/*`.
- **Rendu du chrome** : valider que `admin_page_frame.html` produit un HTML strictement
  équivalent à `AdminHeader.jsp`/`AdminFooter.jsp` (têtes cache, accessibilité, base href).
- **Redirection relative (résolu)** : `sendRedirect` résout contre l'URI courante, pas le
  `<base href>` → la cible de redirection doit être same-dir (`{name}?view=…`), les liens
  href gardent le chemin complet. Cf. § Génération d'URL.
- **Bug latent du core (corrigé)** : si un contrôleur n'a pas de `@View` par défaut,
  `processController` appelait `fireBeforeControllerEvent(null,…)` → NPE dans
  `AccessLogService.onControllerInvocation`. Corrigé via le garde-fou action-only
  (cf. chapitre « Contrôleurs publics », § Garde-fou).

---

# Chapitre 2 — Contrôleurs publics (pré-authentification) & migration d'AdminLoginJspBean

**Date :** 2026-06-12
**Statut :** POC validé en runtime (login OK, logs propres sur `doLogin`)

## Contexte

Au-delà des contrôleurs post-authentification (chapitre 1), certaines pages admin sont
**pré-authentification** : login, mot de passe oublié, identifiant oublié, réinitialisation,
contact. Leurs JSP de traitement (`DoAdminLogin.jsp`, `DoAdminForgotPassword.jsp`, …)
portaient le **même anti-pattern** — et il est **aggravé** sous Jakarta EE 11 / Servlet 6.1
(WebSphere Liberty) :

```jsp
<%@ page errorPage="ErrorPage.jsp" %>
<jsp:include page="AdminHeaderSessionLess.jsp" />     <#-- écrit une page HTML via getWriter -->
${ pageContext.response.sendRedirect( adminLoginJspBean.doLogin( pageContext.request )) }
```

Le `jsp:include` écrit du HTML (acquiert `getWriter`), puis `sendRedirect` est appelé ; le
conteneur écrit le corps de redirection via `getOutputStream` → `IllegalStateException`
loguée en boucle. **Aucune correction au niveau JSP n'est possible** : toute JSP acquiert un
`JspWriter` au démarrage de `_jspService`. Seule une **servlet** (qui ne touche jamais
`getWriter` pour un corps) fait un `sendRedirect` propre → c'est le front-controller.

## Obstacles spécifiques au pré-auth

1. **`init(request, right)` exige un utilisateur + un droit.** `processController` appelle
   systématiquement `init( request, _controller.right( ) )`, or `init()` exige un user
   authentifié et un `Right` existant. En pré-auth (user `null`, pas de droit) → échec / NPE.
2. **L'`AuthenticationFilter` protège `/jsp/admin/*`.** Notre servlet en hérite ; il faut
   donc autoriser explicitement l'URL publique.
3. **`getResquestedUrl` de l'`AuthenticationFilter` utilise `getServletPath()`** — pour un
   mapping en préfixe (`/jsp/admin/mvc/*`), `getServletPath()` vaut `/jsp/admin/mvc` (le nom
   du contrôleur est dans `getPathInfo()`). La whitelist ne pourrait donc pas cibler un
   contrôleur précis. → un contrôleur public exige un **mapping servlet exact**.

## Décision — flag `publicAccess` explicite (jamais implicite)

On ajoute `boolean publicAccess( ) default false` à `@Controller`. **Opt-in explicite** :
- `right()` **reste obligatoire** (pas de `default`) → aucun contrôleur existant ne devient
  public par oubli.
- Un contrôleur n'est public **que** s'il déclare `publicAccess = true` (et son URL doit
  *aussi* être dans la whitelist de l'`AuthenticationFilter` — double barrière indépendante).

> Rejeté : « `right` vide = public ». Trop dangereux (exposition par oubli + perte du
> contrôle RBAC). Le flag rend l'intention visible et auditable.

## Design

### `@Controller.publicAccess` + `initPublic`

```java
// MVCAdminJspBean.processController
if ( _controller.publicAccess( ) )
    initPublic( request );                      // init minimal : locale seule, sans user/right
else
    init( request, _controller.right( ) );      // comportement strict ACTUEL, inchangé
```

`AdminFeaturesPageJspBean.initPublic( request )` : `_user = getAdminUser(request)` (peut être
`null`), `_locale = AdminUserService.getLocale(request)`. Aucun lookup de droit, aucun
`checkRight`.

### Mapping servlet exact + résolution du nom

```xml
<servlet-mapping>
    <servlet-name>AdminMvcServlet</servlet-name>
    <url-pattern>/jsp/admin/mvc/*</url-pattern>            <!-- contrôleurs normaux -->
    <url-pattern>/jsp/admin/mvc/adminLogin</url-pattern>   <!-- contrôleur public : URL complète pour l'auth filter -->
</servlet-mapping>
```

`AdminMvcServlet.extractRouteName` gère les deux cas : `pathInfo` présent (préfixe) → 1ᵉʳ
segment ; `pathInfo` `null` (mapping exact) → dernier segment du `servletPath`.

### Whitelist de l'`AuthenticationFilter`

```properties
path.jsp.admin.public.list=…,adminLoginMvc,…
path.jsp.admin.public.adminLoginMvc=jsp/admin/mvc/adminLogin
```

`getResquestedUrl` ignorant la query string, **vue + toutes les actions** d'`adminLogin`
partagent la même URL `jsp/admin/mvc/adminLogin` → **une seule** entrée couvre tout.

### Audit

`MvcControllerRegistry` logue un `INFO` au démarrage pour chaque contrôleur `publicAccess`.

## Migration d'`AdminLoginJspBean`

- `extends MVCAdminJspBean`, `@Controller( name = "adminLogin", right = "", publicAccess = true )`.
- **Toutes les actions** annotées `@Action` et leurs `return <url>` passés par `redirect(...)` :
  `login`, `doForgotPassword`, `doResetPassword`, `doForgotLogin`, `doFormContact`, `doLogout`.
- Formulaires **model-driven** : `getActionUrl(...)` injecté au modèle (`do_admin_login_url`,
  `action_url`) ; les templates postent vers `/jsp/admin/mvc/adminLogin?action=…`.
- URLs relatives rendues **absolues** (`JSP_URL_FORM_CONTACT`, early-return HTTPS) pour une
  redirection correcte depuis `/jsp/admin/mvc/`.
- Logout : propriété `lutece.admin.logout.url` repointée → le lien du menu passe par le
  front-controller.
- **Token CSRF du login : inchangé** (mécanisme manuel `ISecurityTokenService`, clé
  `admin/admin_login.html`). On ne modifie pas la sémantique de sécurité.

### Périmètre : actions seulement

Les **vues** `get*` restent servies par leurs JSP existantes (`AdminLogin.jsp`, …). Ce sont
des JSP de **rendu pur** (pas de `sendRedirect`) → elles n'ont pas l'anti-pattern. Migrer les
vues en `@View` nécessiterait un **chrome sessionless** (`PageFrameService.wrapPublic`, car
`getAdminMenuHeader` exige un user) + la reconfiguration des URLs d'auth
(`getLoginPageUrl` en dur) → tâche dédiée, à faible ROI et risque élevé (lockout login).
**Reporté volontairement.**

## Garde-fou « action-only » (fix du bug latent du core)

Un contrôleur **uniquement `@Action`** (qui redirige ailleurs) est légitime. Invoqué sans
action correspondante, l'ancien code faisait `fireBeforeControllerEvent(null)` → NPE 500.
Désormais :

```java
m = MVCUtils.findDefaultViewMethod( methods );
if ( m == null )
{
    return null;          // rien à rendre (action-only sans action) — pas de NPE
}
```

et `AdminMvcServlet` répond **404** propre quand `processController` renvoie `null` (réponse
non committée). **Aucun impact** sur les contrôleurs à vue (`ThemeJspBean` & co.) :
`findDefaultViewMethod` y retourne toujours la `@View(defaultView=true)`, donc `m` n'est
jamais `null`.

## Fichiers (chapitre 2)

### Modifiés
- [x] `annotations/Controller.java` — `publicAccess()`.
- [x] `web/admin/AdminFeaturesPageJspBean.java` — `initPublic()`.
- [x] `util/mvc/admin/MVCAdminJspBean.java` — branche `publicAccess` + garde-fou action-only.
- [x] `web/admin/AdminMvcServlet.java` — mapping exact, contenu `null` → 404, clé i18n réelle.
- [x] `util/mvc/admin/MvcControllerRegistry.java` — log d'audit public.
- [x] `web/user/AdminLoginJspBean.java` — conversion MVC + `@Action` + `redirect()`.
- [x] `webapp/WEB-INF/web.xml` — mapping exact `/jsp/admin/mvc/adminLogin`.
- [x] `webapp/WEB-INF/conf/lutece.properties` — whitelist `adminLoginMvc` + logout repointé.
- [x] 5 templates `admin_login/forgot_password/reset_password/forgot_login/form_contact.html`
      — action du formulaire model-driven.

## Points de vigilance (chapitre 2)

- **Chrome sessionless** requis avant toute migration des **vues** pré-auth.
- **URL de login** : si un jour on migre la vue login, repointer `getLoginPageUrl()` et
  adapter le test interne de `getLogin` (sinon boucle de redirection).
- **Token périmé** : un token de login expiré (redéploiement, page ancienne, retour
  navigateur) lève `AccessDeniedException` → message « accès refusé ». Comportement d'origine
  conservé ; en usage normal (page fraîche) le login fonctionne.
- **Bruit de logs non lié** : `WELD-000xxx` (intégration Weld/Liberty, cascade après un 500),
  `Error500.jsp _jsp_performFinalCleanUp` (bug pré-existant du core), `IOException broken
  pipe` (abandon navigateur), `/DS Value Missing` (clé datastore non seedée). Indépendants du
  front-controller.

---

# Chapitre 3 — Bilan de la migration Back-Office (ticket LUT-32862)

**Date :** 2026-06-24
**Statut :** Migration par-bean **terminée** pour tout le périmètre atteignable sans évolution de SPI/framework.

## Objectif

Migrer **tous** les JspBeans Back-Office « classiques » (héritant de `AdminFeaturesPageJspBean`)
vers le modèle front-controller du chapitre 1 : `@Controller(name=…)` extends `MVCAdminJspBean`,
méthodes `@View`/`@Action`, suppression des JSP de routage, mise à jour de `core_admin_right.admin_url`,
tests pilotés par `processController`. Règle de travail : **un bean à la fois, un commit par bean
(préfixe `LUT-32862:`), tests lancés à chaque étape**, sans régression.

## Contrôleurs migrés

### Pilotes initiaux (chapitre 1/2, préfixe antérieur)

| Bean | Route | Notes |
|---|---|---|
| `ThemeJspBean` | `theme` | Pilote runtime (vue `manageThemes` + action `modifyGlobalTheme`). |
| `SecurityHeaderJspBean` + `SecurityHeaderConfigJspBean` | `securityheader` | CRUD en-têtes de sécurité. |
| `AutoIncludeJspBean` | (dashboard) | Action-only (redirige vers dashboard) ; `admin_url` NULL. |
| `CacheJspBean` | `cache` | Gestion du cache. |
| `AdminLoginJspBean` | `adminLogin` | **Public** (`publicAccess=true`) — cf. chapitre 2. |

### Migration LUT-32862 (un commit par bean)

| Bean | Route | Commit |
|---|---|---|
| `LevelsJspBean` | (dashboard) | `33a6133b5` |
| `RightJspBean` | `rights` | `0f7db77bf` |
| `DaemonsJspBean` | `daemons` | `305a544f5` |
| `RoleJspBean` | `pagerole` | `2cea360a5` |
| `EditorChoiceLutecePanelJspBean` | (dashboard) | `35a2d99e2` |
| `AdminWorkgroupJspBean` | `workgroups` | `971b4696e` |
| `MailingListJspBean` | `mailinglists` | `0e05e3a8d` |
| `PageTemplatesJspBean` | `pagetemplates` | `8db63c77f` |
| `ExternalFeaturesJspBean` | (dashboard) | `593702f3b` |
| `FeaturesGroupJspBean` | (dashboard) | `b651743f0` |
| `RoleManagementJspBean` | `rbac` | `262c9cd88` (assistant add-control, le plus gros bean RBAC) |
| `PluginJspBean` | `plugins` | `9596d4c41` (drop du `ServletContext` inutilisé) |
| `AttributeJspBean` + `AttributeFieldJspBean` | `attribute` / `attributefield` | `52a807def` (redirection cross-route) |
| `SystemJspBean` (propriétés du site) | `properties` | `7de1ced4e` |
| `SearchIndexationJspBean` | `searchindexation` | `a6890095f` |
| `SearchJspBean` | `search` | `8317d8342` |
| `DashboardJspBean` | `dashboard` | `8a629db84` |
| `AdminDashboardJspBean` | `admindashboard` | `b9fb42ad3` |
| `AdminUserJspBean` | `user` | `5aa5ffbf3` (2754 l., 17 `@View` + 19 `@Action`, 74 tests) |

> Note : `admin_url = NULL` signifie un bean joignable uniquement via un dashboard / lien interne
> (pas une entrée de menu) → **aucune** ligne SQL à modifier (autoinclude, levels, editor,
> externalfeatures, featuresgroup, dashboard).

## Patterns établis (à réutiliser pour toute migration future)

1. **Redirection vs contenu.** Une `@View` retourne du HTML (`getAdminPage(...)`), enveloppé par
   `PageFrameService.wrap`. Une `@Action` redirige (`redirectView`/`redirect`, qui font
   `sendRedirect` et retournent `null`). **Le front-controller enrobe toute chaîne non-`null`
   comme contenu de page** → une action qui doit afficher un message d'erreur/confirmation
   **doit** envelopper l'URL : `return redirect( request, AdminMessageService.getMessageUrl( … ) );`
   (ne jamais retourner l'URL brute).
2. **Dialogues de confirmation.** La cible du bouton de confirmation passe par
   `getActionUrl(ACTION_…)` et l'URL d'annulation par `getViewFullUrl(VIEW_…)`. La **clé** du
   token CSRF reste la chaîne legacy opaque (ex. `jsp/admin/user/DoRemoveUser.jsp`) : génération
   et validation partagent la même clé, donc on la conserve telle quelle.
3. **Token CSRF.** On garde le mécanisme explicite `getSecurityTokenService().getToken/validate`
   avec les clés d'origine ; `securityTokenEnabled` reste `false` (pas d'enregistrement
   automatique des actions).
4. **Modèle.** Préférer `@Inject Models _models` ; conserver une `HashMap` locale uniquement quand
   une API externe mute la map (ex. `AdminUserService.getFilteredUsersInterface(list, request,
   model, url)`) ou pour minimiser le risque sur un très gros bean.
5. **Tests.** Piloter via `_instance.processController( withAction(request, "x"), new
   MockHttpServletResponse() )` ; enregistrer un `AdminUser` niveau 0 (admin) car
   `processController` appelle `init(request, right)` — contrôle que les appels directs
   contournaient. Les vues de rendu pur restent appelables directement.
6. **Commit.** Stager séparément les fichiers modifiés et les `git rm` (sinon `git add` avorte et
   le commit n'embarque que les suppressions).

## Cas spéciaux différés (limitation framework, non bloquants pour le périmètre)

| Bean | Raison |
|---|---|
| `InsertServiceSelectorJspBean` | Méthodes retournant du **HTML brut** (popups éditeur, sans `getAdminPage`) — le front-controller les enrobe → casserait l'affichage. Besoin de `@ResponseBody`/contenu brut. |
| `ExtendableResourceJspBean` | `doProcessExtendableResourceAction` retourne un `IPluginActionResult` (signature `(request,response)`), pas un `String` — cadre plugin-action. |
| `doImportUsersFromFile` / `doExportUsers` (`AdminUserJspBean`) | Retour `DefaultPluginActionResult` + téléchargement de fichier → **conservés sur leurs JSP legacy** (hybride). Même limitation que ci-dessus. |
| `reactivateAccount` (`AdminUserJspBean`) | Endpoint atteint depuis un lien d'e-mail (daemon) → JSP legacy conservée. |

Ces cas nécessitent un support framework **retours non-`String`** (`@ResponseBody`, résultat
plugin-action, téléchargement), explicitement reporté.

## Périmètre restant (hors migration per-bean)

Classes héritant encore de `AdminFeaturesPageJspBean` et **non** migrables tel quel :

- `PluginAdminPageJspBean` — **classe de base** (parent des beans de plugin). Ne jamais migrer.
- `PortletJspBean` — **base de SPI publique** (cf. chapitre 4).
- `AliasPortletJspBean`, `AdminPagePortletJspBean`, `AdminPageJspBean`, `AdminMapJspBean` —
  sous-système site/page/portlet, soudé à la SPI portlet (cf. chapitre 4).

**Conclusion :** tout JspBean migrable sans évolution de framework/SPI est migré (19 commits
LUT-32862 + pilotes). Le reste est conditionné à (a) la refonte de la SPI portlet (chapitre 4) et
(b) le support des retours non-`String` (`@ResponseBody`/plugin-action).

---

# Chapitre 4 — Note de design : évolution de la SPI Portlet (différée)

**Date :** 2026-06-24
**Statut :** **Design / RFC** — non implémenté. Identifié comme bloquant pour la fin de la
migration Back-Office. Nécessite une décision d'architecture (impact écosystème).

## Contexte

Le dernier îlot Back-Office encore en « JSP de routage » est le sous-système **site / page /
portlets**. Contrairement aux beans déjà migrés, ce n'est **pas** une migration par-bean : c'est
une **SPI publique** dont héritent tous les types de portlets de l'écosystème Lutèce (plugins
internes et externes). La toucher sans précaution casserait tous ces plugins.

## Architecture actuelle

### `PortletJspBean` — base abstraite publique

```java
public abstract class PortletJspBean extends AdminFeaturesPageJspBean
{
    public abstract String getCreate( HttpServletRequest request );  // formulaire de création
    public abstract String doCreate( HttpServletRequest request );   // traitement création
    public abstract String getModify( HttpServletRequest request );  // formulaire de modif
    public abstract String doModify( HttpServletRequest request );   // traitement modif
}
```

Chaque type de portlet (ex. `AliasPortletJspBean` au cœur, mais aussi `ArticleListPortletJspBean`,
`HtmlPortletJspBean`, … dans des dépôts externes) **étend** cette classe et fournit ses 4 méthodes
+ ses JSP + ses templates.

### Dispatch piloté par la base de données (`core_portlet_type`)

Le routage vers le bon type de portlet n'est **pas** annotation-driven : il passe par des **URL
stockées en base**. Table `core_portlet_type` :

| Colonne | Rôle |
|---|---|
| `url_creation` | JSP du formulaire de création (ex. `plugins/alias/CreatePortletAlias.jsp`) |
| `url_update` | JSP du formulaire de modification |
| `url_docreate` / `url_domodify` | JSP de traitement (`DoCreatePortletAlias.jsp`, …) |
| `create_script` / `modify_script` | template du squelette |
| `create_specific` / `modify_specific` (+ `_form`) | template spécifique au type |
| `home_class` | classe `…Home` du portlet |
| `plugin_name` | plugin fournisseur |

Exemple (alias, cœur) :

```sql
INSERT INTO core_portlet_type VALUES ('ALIAS_PORTLET','portal.site.portletAlias.name',
  'plugins/alias/CreatePortletAlias.jsp','plugins/alias/ModifyPortletAlias.jsp',
  'fr.paris.lutece.portal.business.portlet.AliasPortletHome','alias',
  'plugins/alias/DoCreatePortletAlias.jsp', …);
```

### Coordination

- `AdminPagePortletJspBean.doCreatePortlet(request)` lit `PORTLET_TYPE_ID`, cherche le type dans
  `core_portlet_type` et **retourne `portletType.getUrlCreation()`** (l'URL de la JSP du plugin).
  `doModifyPortlet` retourne `getUrlUpdate()` + `portlet_id`. C'est un **routeur de redirection
  vers des JSP DB-configurées**, pas un contrôleur de rendu.
- `AdminPageJspBean` gère la page (vue admin, `doModifyPage`, `doCreateChildPage`,
  `getRemovePage`/`doRemovePage`, preview, image de ressource). Sa vue est incluse dans
  `AdminSite.jsp`.
- `AdminMapJspBean` produit le fragment offcanvas « plan du site » embarqué dans `AdminSite.jsp`
  (`AdminMap.jsp` = endpoint AJAX de rafraîchissement de l'arbre).
- `AdminSite.jsp` câble le tout (`adminPageJspBean` + `adminMapJspBean`) et constitue l'écran de
  la feature `CORE_ADMIN_SITE`.

## Pourquoi ce n'est pas une migration per-bean

1. **SPI publique.** Migrer `PortletJspBean` (passer `getCreate`/`doCreate`/`getModify`/`doModify`
   en `@View`/`@Action`) impose de réécrire **tous les portlets de l'écosystème** — y compris des
   dépôts externes non maîtrisés. Rupture de compatibilité ascendante massive.
2. **Dispatch en base.** Le routage repose sur les colonnes URL de `core_portlet_type` (données
   livrées par chaque plugin via son `*.sql`). Migrer suppose de redéfinir ce contrat de données
   **et** de migrer les données de tous les plugins.
3. **Aller-retour couplé.** Une création de portlet fait : page admin → `doCreatePortlet` → JSP du
   type → `DoCreate…` → redirection vers la page admin. Migrer la page admin sans migrer les JSP
   de type (ou l'inverse) casse l'aller-retour. Le sous-système doit bouger **d'un bloc**.

C'est précisément l'« évolution du modèle MVC » mise de côté volontairement.

## Approches envisagées

### Approche A — SPI annotation-driven + cohabitation (recommandée)

Faire de chaque type de portlet un **contrôleur front-controller** à part entière, avec une route
dédiée, tout en gardant le dispatch DB pendant la transition.

- `PortletJspBean` devient soit une base `MVCAdminJspBean` (les sous-classes deviennent
  `@Controller(name="portlet-<type>")` avec `@View getCreate/getModify` + `@Action doCreate/doModify`),
  soit on introduit une **nouvelle** interface SPI à côté de l'ancienne (les deux coexistent).
- `core_portlet_type.url_creation/url_update` peut désormais contenir une URL front-controller
  (`jsp/admin/mvc/portlet-alias?view=create`). Les types **non migrés** gardent leur JSP → **aucune
  rupture** : le dispatch lit toujours l'URL en base, qu'elle pointe vers une JSP ou vers le
  front-controller.
- Migration **type par type** (même discipline que LUT-32862), en commençant par `AliasPortlet`
  (cœur), puis les plugins, à leur rythme.
- **Pour :** rétrocompatible, progressif, aligné sur le modèle existant. **Contre :** demande une
  passe de migration sur chaque plugin de portlet ; deux mécanismes coexistent un temps.

### Approche B — Adaptateur générique (façade)

Garder `PortletJspBean` tel quel et introduire **un** contrôleur front-controller générique
(`PortletMvcServlet`/`@Controller(name="portlet")`) qui, à partir du `PORTLET_TYPE_ID`, instancie
le bean de type et **délègue** à `getCreate`/`doCreate`/`getModify`/`doModify`.

- **Pour :** supprime les JSP de routage **sans** toucher la SPI ni les plugins ; un seul point
  d'entrée. **Contre :** ne migre pas réellement les types vers MVC (ils restent des
  `PortletJspBean`) ; conserve le rendu HTML brut renvoyé par les méthodes existantes → nécessite
  quand même le support « contenu brut » (`@ResponseBody`-like) côté front-controller, et un chrome
  adapté. Solution intermédiaire, faible coût, faible bénéfice structurel.

### Approche C — Refonte complète Jakarta MVC

Réécrire toute la SPI portlet en Jakarta MVC (JSR-371) pur. **Rejetée** : coût et rupture
maximaux, hors sujet pour ce POC (déjà listé en « Hors périmètre » du chapitre 1).

## Recommandation

**Approche A**, en deux temps :

1. **Socle SPI** : introduire le support front-controller pour les portlets (base/interface +
   capacité du dispatch DB à pointer vers `jsp/admin/mvc/…`), migrer `AliasPortletJspBean` comme
   pilote, **sans** toucher les autres types (leurs JSP continuent via l'URL DB).
2. **Sous-système site/page** : une fois la SPI portlet en place, migrer `AdminPagePortletJspBean`,
   `AdminPageJspBean` et `AdminMapJspBean` (et recâbler `AdminSite.jsp` → `jsp/admin/mvc/…`), en
   gérant le fragment offcanvas du plan de site (rendu brut, cf. besoin `@ResponseBody`).

Prérequis transverse : **support des retours non-`String`** au front-controller (`@ResponseBody`
pour fragments/JSON/téléchargement) — partagé avec les cas spéciaux `InsertServiceSelector` /
`ExtendableResource` / export utilisateurs du chapitre 3.

## Plan de migration (Approche A) — esquisse

1. Étendre l'annotation/dispatch : autoriser une route front-controller comme `url_creation`/
   `url_update`. Vérifier que `AdminPagePortletJspBean.doCreatePortlet/doModifyPortlet` (qui ne
   font que retourner l'URL DB) restent inchangés.
2. Ajouter le support `@ResponseBody` / contenu brut au `MVCAdminJspBean`/`AdminMvcServlet`
   (fragment HTML sans enrobage chrome) — prérequis commun.
3. Migrer `AliasPortletJspBean` → `@Controller` + `@View`/`@Action` ; mettre à jour la ligne
   `ALIAS_PORTLET` de `core_portlet_type` (init + upgrade SQL) ; supprimer ses 4 JSP
   `plugins/alias/*PortletAlias*.jsp`. Tester l'aller-retour création/modif depuis une page.
4. Migrer `AdminMapJspBean` (fragment offcanvas en `@ResponseBody`) puis `AdminPageJspBean` /
   `AdminPagePortletJspBean` ; recâbler `AdminSite.jsp` ; mettre à jour `CORE_ADMIN_SITE.admin_url`.
5. Publier une note de migration pour les **plugins de portlets externes** (recette : passer en
   `@Controller`, repointer `url_creation`/`url_update`, supprimer les JSP de routage).

## Risques & points de vigilance

- **Rupture écosystème** : tant que la SPI historique `PortletJspBean` n'est pas dépréciée
  proprement (cohabitation), ne pas la supprimer. Garder les deux chemins jusqu'à migration des
  plugins.
- **Données `core_portlet_type`** : tout changement de contrat d'URL doit être accompagné d'un
  script d'upgrade et documenté pour les fournisseurs de portlets.
- **Rendu brut** : les formulaires de portlet sont des fragments insérés dans la page d'admin de
  page ; ils ne doivent **pas** être enrobés deux fois par le chrome → d'où le besoin
  `@ResponseBody`.
- **`AdminSite.jsp`** : écran composite (page + plan de site) ; migrer les deux beans avant de
  supprimer la JSP, sinon l'aller-retour portlet et le plan de site cassent.

## Hors périmètre de cette note

- L'implémentation effective (cette note est une RFC de cadrage).
- La dépréciation/suppression de l'ancienne SPI (à planifier après migration des plugins).
- La migration des plugins de portlets externes (recette à fournir, exécutée par chaque mainteneur).
