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
<#macro cConsent title='#i18n{portal.theme.consentTitle}' services=[] lang='"privacyUrl":"#i18n{portal.theme.consentPrivacyUrl}","alertBigPrivacy": "#i18n{portal.theme.consentAlertBigPrivacy}"' privacyLink='#i18n{portal.theme.consentPrivacyLink}' showIcon=true iconPosition='bottomRight' cookieMenu='#i18n{portal.theme.consentCookieMenu}' cookiePolicyLink='#i18n{portal.theme.consentCookiePolicyLink}' alertConfidentialityLabel='#i18n{portal.theme.consentAlertConfidentialityLabel}' alertConfidentialityLink='#i18n{portal.theme.consentAlertConfidentialityLink}' hashtag='cookiepolicycitelibre' cookiename='parisfr' nocredit=false platform='' deprecated...>
<@deprecatedWarning args=deprecated />
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
