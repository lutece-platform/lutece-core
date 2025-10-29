<#-- Macro: cConsentTac

Description: gère des Cookies CNIL ave cla librairie TarteAuCitron - https://tarteaucitron.io/fr /

Parameters:

@param - title - string - optional - le titre du consentement (par défaut: 'Ce site')
@param - privacyLink - string - optional - Lien vers la page de protection des données personnelles (par défaut: '')
@param - denyAll - boolean - optional - Option permettant d'afficher le bouton "Tout refuser" (par défaut: false)
@param - cookieMenu - string - optional - Libellé du ien footer pour gérer les cookies (par défaut: 'Gestion des cookies')
@param - cookiePolicyLink - string - optional - Lien vers la page Gestion des cookies de Paris.fr. (par défaut: '')
@param - hashtag - string - optional - Identifiant pour modal de gestion du consentement (par défaut: 'cookiepolicypageId')
@param - cookiename - string - optional - Nom du cookie posé pour le consentement (par défaut: 'cookiepageId')
@param - nocredit - boolean - optional - Option permettant d'afficher les crédit du projet TarteAuCitron (par défaut: false)
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