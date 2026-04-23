<#--
Macro: cConsentOrejimeService

Description: Macro de composant de consentement pour la librairie Orejime. Utilisée pour déclarer un service tiers dans la configuration de consentement.

Parameters:
- code (string, optional): Identifiant du service (ex: 'matomo', 'googleanalytics'). Par défaut: ''.
- title (string, optional): Titre du service affiché dans la liste des services de consentement. Par défaut: ''.
- description (string, optional): Description du service affichée dans la liste des services de consentement. Par défaut: ''.
- required (boolean, optional): Indique si le service est obligatoire (true) ou optionnel (false). Par défaut: true.
- exempt (boolean, optional): Indique si le service est exempté de consentement (true) ou non (false). Par défaut: false.       

Showcase:
- desc: Enregistrement d'un service pour Orejime - @cConsentOrejimeService
- newFeature: true

Snippet:

    Register a Youtube service:

    <@cConsentOrejimeService code='youtube' title='Youtube' description='Service de lecture de vidéos Youtube' required=true exempt=true />
-->
<#macro cConsentOrejimeService code='' title='' description='' required=true exempt=false deprecated...>
<@deprecatedWarning args=deprecated />
<#if code !=''>
<#nested>
{
    id: '${code}',
    title: '${title}',
    description: "${description}",
    isMandatory: ${required?c},
    isExempt: ${exempt?c},
},
</#if>
</#macro>
<#-- 
Macro:  cConsentOrejimeTemplateService :           
Description: Macro de composant de consentement pour la librairie Orejime. Permet de définir un template de service réutilisable dans la configuration de consentement. Utile pour les services contextuels qui ne sont pas basés sur un tiers mais nécessitent tout de même une déclaration dans la liste des services de consentement pour être affiché dans la bannière et la modal de gestion du consentement (ex: cookie wall).

Parameters:
- code (string, optional): Identifiant du service (ex: 'matomo', 'googleanalytics'). Par défaut: ''.
- contextual (boolean, optional): Indique si le service est contextuel, c'est à dire q'un fallback sera affiché pour le service tant que le consentement n'est pas donné. Par défaut: false.    

Showcase:
- desc: Enregistrement d'un script externe (exemple: Youtube ) pour Orejime - @cConsentOrejimeTemplateService
- newFeature: true

Snippet:

    Defini un template de code pour un service déclaré par cConsentOrejimeService (ex: youtube) pour gérer l'insertion ou non du script (ex: iframe Youtube).

    <@cConsentOrejimeTemplateService code='youtube' contextual=true>
      <iframe src="https://www.youtube.com/embed/toto"></iframe>
    </@cConsentOrejimeTemplateService>

-->
<#macro cConsentOrejimeTemplateService code='' contextual=false deprecated...>
<@deprecatedWarning args=deprecated />
<template data-purpose="${code}"<#if contextual> data-contextual</#if>>
<#nested>
</template>
</#macro>