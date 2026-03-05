<#--
Macro: cConsentTac

Description: Generates a CNIL-compliant cookie consent banner using the TarteAuCitron library (https://tarteaucitron.io).

Parameters:
- title (string, optional): The consent banner title. Default: 'Ce site'.
- privacyLink (string, optional): URL to the privacy policy page. Default: ''.
- denyAll (boolean, optional): If true, shows a "Deny all" button. Default: false.
- cookieMenu (string, optional): Label for the cookie management footer link. Default: 'Gestion des cookies'.
- cookiePolicyLink (string, optional): URL to the cookie policy page. Default: ''.
- hashtag (string, optional): Hashtag identifier for the consent management modal. Default: 'cookiepolicy'.
- cookiename (string, optional): Name of the consent cookie. Default: 'cookiepageId'.
- nocredit (boolean, optional): If true, hides the TarteAuCitron credits. Default: false.

Snippet:

    Basic cookie consent with privacy link:

    <@cConsentTac privacyLink='jsp/site/Portal.jsp?page=privacy' cookiePolicyLink='jsp/site/Portal.jsp?page=cookies'>
        <@cConsentTacService code='youtube' />
        <@cConsentTacService code='matomo' />
    </@cConsentTac>

-->
<#macro cConsentTac title='Ce site'  privacyLink='' denyAll=false cookieMenu='Gestion des cookies' cookiePolicyLink='' hashtag='cookiepolicy' cookiename='cookiepageId' nocredit=false  deprecated...>
<@deprecatedWarning args=deprecated />

<link rel="stylesheet" href="${commonsSiteThemePath}${commonsSiteCssPath}vendor/tarteaucitron.min.css" >
<script src="${commonsSharedThemePath}vendor/tarteaucitron/tarteaucitron.min.js"></script>
<script>
tarteaucitron.init({
    "bodyPosition": "top",
    "privacyUrl": "${privacyLink}",         /* Privacy policy url */
    "hashtag": "#${hashtag}",               /* Open the panel with this hashtag */
    "cookieName": "${cookiename}",          /* Cookie name */
    "orientation": "bottom",                /* Banner position (top - bottom) */
    "showAlertSmall": false,                /* Show the small banner on bottom right */
    "cookieslist": true,                    /* Show the cookie list */
    "adblocker": true,                      /* Show a Warning if an adblocker is detected */
    "AcceptAllCta" : true,                  /* Show the accept all button when highPrivacy on */
    "DenyAllCta" : ${denyAll?c},            /* Show the Deny all button when highPrivacy on */
    "highPrivacy": true,                    /* Disable auto consent */
    "handleBrowserDNTRequest": true,        /* If Do Not Track == 1, disallow all */
    "removeCredit": ${nocredit?c},          /* Remove credit link */
    "moreInfoLink": true,                   /* Show more info link */
    "useExternalCss": true,                 /* If false, the tarteaucitron.css file will be loaded */        
    "readmoreLink": "${cookiePolicyLink}",  /* Change the default readmore link */
    "mandatory": true,                      /* Show a message about mandatory cookies */
});
<#nested>
</script>
</#macro>