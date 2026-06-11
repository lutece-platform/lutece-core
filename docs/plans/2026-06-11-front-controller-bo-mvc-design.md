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
- **Bug latent du core (à corriger hors POC)** : si un contrôleur n'a pas de `@View` par
  défaut, `processController` appelle `fireBeforeControllerEvent(null,…)` et
  `AccessLogService.onControllerInvocation` NPE sur `method.getName()`. Ajouter un
  garde-fou indépendamment du front-controller.
