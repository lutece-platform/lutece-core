<#-- Macro: cConsentTac

Description: gère des Cookies CNIL ave cla librairie TarteAuCitron - https://tarteaucitron.io/fr /

Parameters:

@param - title - string - optional - le titre du consentement (par défaut: 'Ce site')
@param - messages - string - optional - Messages personnalisés pour le consentement (par défaut: messages Paris.fr)
@param - lang - string - optional - Objet JSON des traductions personnalisées pour remplacer les textes par défaut (par défaut: '')
@param - privacyLink - string - optional - Lien vers la page de protection des données personnelles de Paris.fr (par défaut: '//www.paris.fr/pages/mentions-legales-235#confidentialite-et-protection-des-donnees')
@param - showIcon - boolean - optional - Option permettant d'afficher le bouton "Gestion des cookies" (par défaut: true)
@param - iconPostion - string - optional - Option permettant d'afficher le bouton "Gestion des cookies" à gauche ou à droite, en haut ou en bas (par défaut: 'bottomRight')
@param - cookieMenu - string - optional - Libellé du ien footer pour gérer les cookies (par défaut: 'Gestion des cookies')
@param - cookiePolicyLink - string - optional - Lien vers la page Gestion des cookies de Paris.fr. (par défaut: 'https://www.paris.fr/pages/cookies-234')
@param - alertConfidentialityLabel - string - optional - Libellé de l'alerte de confidentialité (par défaut: 'Politique de confidentialité')
@param - alertConfidentialityLink - string - optional - Lien vers la politique de confidentialité (par défaut: 'https://www.paris.fr/pages/mentions-legales-235#confidentialite-et-protection-des-donnees')
@param - hashtag - string - optional - Identifiant pour modal de gestion du consentement (par défaut: 'cookiepolicycitelibre')
@param - cookiename - string - optional - Nom du cookie posé pour le consentement (par défaut: 'cookiecitelibre')
@param - nocredit - boolean - optional - Option permettant d'afficher les crédit du projet TarteAuCitron (par défaut: false)
 -->
<#macro cConsentTac title='#i18n{portal.theme.consentTitle}' services=[] lang='"privacyUrl":"#i18n{portal.theme.consentPrivacyUrl}","alertBigPrivacy": "#i18n{portal.theme.consentAlertBigPrivacy}"' privacyLink='#i18n{portal.theme.consentPrivacyLink}' showIcon=true iconPosition='bottomRight' cookieMenu='#i18n{portal.theme.consentCookieMenu}' cookiePolicyLink='#i18n{portal.theme.consentCookiePolicyLink}' alertConfidentialityLabel='#i18n{portal.theme.consentAlertConfidentialityLabel}' alertConfidentialityLink='#i18n{portal.theme.consentAlertConfidentialityLink}' hashtag='cookiepolicycitelibre' cookiename='parisfr' nocredit=false deprecated...>
<@deprecatedWarning args=deprecated />
<link rel="stylesheet" href="${commonsSharedThemePath}${commonsSiteJsPath}vendor/tarteaucitron/css/theme-tac.min.css" >
<script src="${commonsSharedThemePath}${commonsSiteJsPath}vendor/tarteaucitron/tarteaucitron.min.js"></script>
<!-- Service Mon Paris -->
<script>
<#if lang !=''>
tarteaucitronCustomText = {
   ${lang}
}; 
</#if>
tarteaucitron.services.monparis = {
  "key": "monparis",
  "type": "api",
  "name": "Mon Paris",
  "uri": "https://moncompte.paris.fr",
  "readmoreLink": "${cookiePolicyLink}",
  "needconsent": false,
  "useExternalCss" : true,
  "mandatory": true,
  "cookies": ['mcpAuth','JSESSIONID','AUTH_SESSION_ID','KEYCLOAK_IDENTITY','KEYCLOAK_SESSION'],
  "js": function () {
        "use strict";
        },
    "fallback": function () {
        "use strict";
        // fallback code if consent is not given
    },
};

tarteaucitron.init({
    "bodyPosition": "top",          /* Tag positionné en haut pour accessibilité */
    "privacyUrl": "${privacyLink}", /* Privacy policy url */
    "hashtag": "#${hashtag}",       /* Open the panel with this hashtag */
    "cookieName": "${cookiename}",  /* Cookie name */
    "orientation": "bottom",        /* Banner position (top - bottom) */
    "groupServices": false,         /* Group services by category */
    "showDetailsOnClick": true,     /* Click to expand the description */
    "serviceDefaultState": "wait",  /* Default state (true - wait - false) */
    "showAlertSmall": false,        /* Show the small banner on bottom right */
    "showIcon": ${showIcon?c},      /* Show cookie icon to manage cookies */
    // "iconSrc": "",               /* Optional: URL or base64 encoded image */
    "iconPosition": "${iconPosition}", /* Position of the icon between BottomRight, BottomLeft, TopRight and TopLeft */
    "cookieslist": true,            /* Show the cookie list */
    "adblocker": true,              /* Show a Warning if an adblocker is detected */
    "AcceptAllCta" : true,          /* Show the accept all button when highPrivacy on */
    "DenyAllCta" : true,            /* Show the Deny all button when highPrivacy on */
    "highPrivacy": true,            /* Disable auto consent */
    "alwaysNeedConsent": true,      /* Ask the consent for "Privacy by design" services */
    "handleBrowserDNTRequest": true,/* If Do Not Track == 1, disallow all */
    "removeCredit": ${nocredit?c},  /* Remove credit link */
    "moreInfoLink": true,           /* Show more info link */
    "useExternalCss": true,         /* If false, the tarteaucitron.css file will be loaded */        
    "readmoreLink": "${cookiePolicyLink}", /* Change the default readmore link */
    "mandatory": true,              /* Show a message about mandatory cookies */
    "mandatoryCta": true,           /* Show the disabled accept button when mandatory on */

    "customCloserId": "tac",        /* Optional a11y: Custom element ID used to open the panel */

    "googleConsentMode": true,      /* Enable Google Consent Mode v2 for Google ads and GA4 */
    "bingConsentMode": true,        /* Enable Bing Consent Mode for Clarity and Bing Ads */
    "softConsentMode": false,       /* Soft consent mode (consent is required to load the services) */

    "dataLayer": false,             /* Send an event to dataLayer with the services status */
    "serverSide": false,            /* Server side only, tags are not loaded client side */
    "partnersList": true            /* Details the number of partners on the popup and middle banner */
});
<#nested>

// Move tarteaucitronRoot after first h1 for accessibility
function cleanTarteaucitronRoot() {
  const tarteaucitronRoot = document.querySelector('#tarteaucitronRoot');
  if ( tarteaucitronRoot ) {
      tarteaucitronRoot.querySelector('#tac_title').remove(); // Removed for accessibility
      tarteaucitronRoot.querySelector('#tarteaucitronPremium').remove(); // Removed for accessibility
      tarteaucitronRoot.querySelectorAll('.tarteaucitronCTAButton').forEach(
          button => button.addEventListener('click', function() {
             let tcText=document.querySelector('title').textContent + '. Votre choix a été enregistré ! '; 

             if( button.classList.contains('tarteaucitronAllow') ) {
                 tcText+=' Vous avez accepté les conditions.'; 
             } else {
                 tcText+=' Vous avez refusé les conditions.'; 
             }
             
             const eltContext = document.createElement('p');
             eltContext.id = 'tc-text';
             eltContext.className = 'visually-hidden visually-hidden-focusable';
             eltContext.setAttribute('tabindex', '0');
             eltContext.textContent = tcText;

             tarteaucitronRoot.insertAdjacentElement('afterend', eltContext);
             document.querySelector('#tc-text').focus(); // Move focus to the consent message for accessibility
          })
      ); 

  } else if (typeof tarteaucitron !== 'undefined') {
    setTimeout(cleanTarteaucitronRoot, 100);
  }
}

<!-- Add link to privacy policy in alert message -->
<#if alertConfidentialityLink !=''>
  function addPrivacyLink() {
    const tarteaucitronRoot = document.querySelector('#tarteaucitronRoot');
    const privacyLink = document.querySelector('#tarteaucitronPrivacyUrl');
    const confLink = document.querySelector('#tarteaucitronConsentUrl');
    
    if(  privacyLink && tarteaucitronRoot && confLink === null ) {
      // Add new link after privacyLink
      const newLink = document.createElement('a');
      newLink.id = 'tarteaucitronConsentUrl';
      newLink.href = '${alertConfidentialityLink}';
      newLink.textContent = '${alertConfidentialityLabel}';
      privacyLink.insertAdjacentHTML('afterend', newLink.outerHTML);
    } else if (typeof tarteaucitron !== 'undefined') {
      // Wait a bit more for DOM elements to be created
      setTimeout(addPrivacyLink, 100);
    }
  }
</#if>

document.addEventListener('DOMContentLoaded', function() {
  // Wait for tarteaucitron to be loaded
  function waitForTarteaucitron() {
    if (typeof tarteaucitron !== 'undefined') {
        <#if alertConfidentialityLink !=''>addPrivacyLink();</#if>
        cleanTarteaucitronRoot();
    } else {
      setTimeout( waitForTarteaucitron, 50);
    }
  }

  waitForTarteaucitron();
  
});
</script>
</#macro>