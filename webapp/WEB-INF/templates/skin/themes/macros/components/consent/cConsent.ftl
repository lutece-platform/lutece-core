<#-- Macro: cConsent

Description: Sélecteur de plateforme de gestion du consentement. Délègue à @cConsentTac (TarteAuCitron)
ou @cConsentOrejime (Orejime) selon la propriété de thème configurable dans le Back-Office Lutèce
(Menu "Propriétés du site" / clé de datastore 'theme.site_property.consent.platform').
Valeurs attendues : 'tac' pour TarteAuCitron, 'orejime' pour Orejime. Valeur par défaut : 'orejime'.

Paramètres :

@param - title - string - optional - le titre du consentement (par défaut: 'Ce site')
@param - lang - string - optional - (TAC uniquement) Objet JSON des traductions personnalisées (par défaut: '')
@param - privacyLink - string - optional - Lien vers la page de protection des données personnelles
@param - showIcon - boolean - optional - (TAC uniquement) Affichage du bouton "Gestion des cookies" (par défaut: true)
@param - iconPosition - string - optional - (TAC uniquement) Position du bouton (par défaut: 'bottomRight')
@param - cookieMenu - string - optional - Libellé du lien footer pour gérer les cookies (par défaut: 'Gestion des cookies')
@param - cookiePolicyLink - string - optional - Lien vers la page Gestion des cookies
@param - alertConfidentialityLabel - string - optional - Libellé de l'alerte de confidentialité
@param - alertConfidentialityLink - string - optional - Lien vers la politique de confidentialité
@param - hashtag - string - optional - (TAC uniquement) Identifiant pour modal de consentement (par défaut: 'cookiepolicycitelibre')
@param - cookiename - string - optional - Nom du cookie posé pour le consentement (par défaut: 'parisfr')
@param - nocredit - boolean - optional - (TAC uniquement) Retrait du crédit TarteAuCitron (par défaut: false)
@param - platform - string - optional - Force la plateforme ('tac' ou 'orejime'), outrepasse la valeur BO (par défaut: '')
 -->
<#include "cConsentOrejime.ftl" />
<#include "cConsentOrejimeService.ftl" />
<#include "cConsentTac.ftl" />
<#include "cConsentTacService.ftl" />
<#include "cConsentTacServiceMatomo.ftl" />
<#macro cConsent title='Ce site' services=[] lang='"privacyUrl":"Politique de cookie","alertBigPrivacy": "Afin de rendre ses services plus performants, d’améliorer l’expérience utilisateur et la manière dont les contenus vous sont présentés, nous analysons la façon dont <a href=https://www.paris.fr/pages/cookies-234>le service numérique est utilisé grâce à des cookies</a>"' privacyLink='//www.paris.fr/pages/mentions-legales-235#confidentialite-et-protection-des-donnees' showIcon=true iconPosition='bottomRight' cookieMenu='Gestion des cookies' cookiePolicyLink='https://www.paris.fr/pages/cookies-234' alertConfidentialityLabel='Politique de confidentialité' alertConfidentialityLink='https://www.paris.fr/pages/mentions-legales-235#confidentialite-et-protection-des-donnees' hashtag='cookiepolicycitelibre' cookiename='parisfr' nocredit=false platform=''>
<#assign consentPlatform=platform />
<#if consentPlatform == ''>
<#assign dsPlatform=dskey('portal.theme.site_property.consent.platform.select')!'' />
<#if dsPlatform?starts_with('DS') || dsPlatform == ''>
<#assign consentPlatform='orejime' />
<#else>
<#assign consentPlatform=dsPlatform?lower_case />
</#if>
</#if>
<#if consentPlatform == 'tac' || consentPlatform == 'tarte_au_citron'>
    <@cConsentTac title=title lang=lang privacyLink=privacyLink showIcon=showIcon iconPosition=iconPosition cookieMenu=cookieMenu cookiePolicyLink=cookiePolicyLink alertConfidentialityLabel=alertConfidentialityLabel alertConfidentialityLink=alertConfidentialityLink hashtag=hashtag cookiename=cookiename nocredit=nocredit>
        <@cConsentTacService  />
        <@cConsentTacService code='matomohightrack'>${matomo!}</@cConsentTacService>
    </@cConsentTac>
<#elseif consentPlatform == 'orejime'>
    <@cConsentOrejime title=title privacyLink=privacyLink cookieMenu=cookieMenu cookiePolicyLink=cookiePolicyLink alertConfidentialityLabel=alertConfidentialityLabel alertConfidentialityLink=alertConfidentialityLink cookiename=cookiename nocredit=nocredit />
<#else>
    <#-- Problem... No consent platform selected ! -->
</#if>
</#macro>
