<#--
Macro: cConsent

Description: Generates a cookie consent component by delegating to either @cConsentTac (TarteAuCitron) or @cConsentOrejime (Orejime) based on the theme property configured in the Lutèce Back-Office (Menu "Propriétés du site" / datastore key 'theme.site_property.consent.platform'). The platform parameter can override the BO setting. Defaults to 'tac' when no platform is configured.

Parameters:
- title (string, optional): main title of the consent banner. Default: '#i18n{portal.theme.consentTitle}'.
- services (list, optional): list of services to configure for consent tracking. Default: [].
- lang (string, optional): TAC-only. JSON object of custom translations. Default: '"privacyUrl":"#i18n{portal.theme.consentPrivacyUrl}","alertBigPrivacy": "#i18n{portal.theme.consentAlertBigPrivacy}"'.
- privacyLink (string, optional): URL to the personal data protection page. Default: '#i18n{portal.theme.consentPrivacyLink}'.
- showIcon (boolean, optional): TAC-only. Whether to display the "Cookie management" floating button. Default: true.
- iconPosition (string, optional): TAC-only. Position of the floating cookie button. Accepted values: 'bottomRight', 'bottomLeft', 'topRight', 'topLeft'. Default: 'bottomRight'.
- cookieMenu (string, optional): footer link label to reopen the cookie consent dialog. Default: '#i18n{portal.theme.consentCookieMenu}'.
- cookiePolicyLink (string, optional): URL to the cookie management page. Default: '#i18n{portal.theme.consentCookiePolicyLink}'.
- alertConfidentialityLabel (string, optional): label of the confidentiality alert message. Default: '#i18n{portal.theme.consentAlertConfidentialityLabel}'.
- alertConfidentialityLink (string, optional): URL to the privacy policy. Default: '#i18n{portal.theme.consentAlertConfidentialityLink}'.
- hashtag (string, optional): TAC-only. Identifier used to open the consent modal via URL fragment. Default: 'cookiepolicycitelibre'.
- cookiename (string, optional): name of the cookie storing the consent state. Default: 'parisfr'.
- nocredit (boolean, optional): TAC-only. Remove the TarteAuCitron credit notice. Default: false.
- platform (string, optional): force the consent platform, overriding the BO setting. Accepted values: 'tac', 'orejime', ''. Default: ''.

Showcase:
- desc: Consentement - @cConsent
- newFeature: true
- updatedFeature: false
- deprecated: false

Snippet:

    Basic usage (platform resolved from BO datastore):

    <@cConsent />

    Force TarteAuCitron platform with a custom title:

    <@cConsent platform='tac' title='Mon Site' cookiename='mysite' />

    Force Orejime platform with custom links:

    <@cConsent platform='orejime' privacyLink='/privacy' cookiePolicyLink='/cookies' />

    TAC with floating icon positioned at top-left and no credit notice:

    <@cConsent platform='tac' iconPosition='topLeft' nocredit=true />

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
<#assign consentPlatform='tac' />
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
