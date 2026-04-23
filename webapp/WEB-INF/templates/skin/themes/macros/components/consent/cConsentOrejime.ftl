<#-- 
Macro: cConsentOrejime

Description: Composant de gestion du consentement basé sur la librairie Orejime de Boscop

Parameters:

- title - string - optional - le titre du consentement (par défaut: 'Ce site')
- messages - string - optional - Messages personnalisés pour le consentement (par défaut: messages Paris.fr)
- lang - string - optional - Objet JSON des traductions personnalisées pour remplacer les textes par défaut (par défaut: '')
- privacyLink - string - optional - Lien vers la page de protection des données personnelles de Paris.fr (par défaut: '//www.paris.fr/pages/mentions-legales-235#confidentialite-et-protection-des-donnees')
- showIcon - boolean - optional - Option permettant d'afficher le bouton "Gestion des cookies" (par défaut: true)
- iconPostion - string - optional - Option permettant d'afficher le bouton "Gestion des cookies" à gauche ou à droite, en haut ou en bas (par défaut: 'bottomRight')
- cookieMenu - string - optional - Libellé du ien footer pour gérer les cookies (par défaut: 'Gestion des cookies')
- cookiePolicyLink - string - optional - Lien vers la page Gestion des cookies de Paris.fr. (par défaut: 'https://www.paris.fr/pages/cookies-234')
- alertConfidentialityLabel - string - optional - Libellé de l'alerte de confidentialité (par défaut: 'Politique de confidentialité')
- alertConfidentialityLink - string - optional - Lien vers la politique de confidentialité (par défaut: 'https://www.paris.fr/pages/mentions-legales-235#confidentialite-et-protection-des-donnees')
- hashtag - string - optional - Identifiant pour modal de gestion du consentement (par défaut: 'cookiepolicycitelibre')
- cookiename - string - optional - Nom du cookie posé pour le consentement (par défaut: 'cookiecitelibre')
- nocredit - boolean - optional - Option permettant d'afficher les crédit du projet TarteAuCitron (par défaut: false)
 
Showcase:
- desc: Consentement avec la librairie Orejime - @cConsentOrejime
- newFeature: true

Snippet:
 
 -->
<#macro cConsentOrejime title='#i18n{portal.theme.consentTitle}' services=[] lang='"privacyUrl":"#i18n{portal.theme.consentPrivacyUrl}","alertBigPrivacy": "#i18n{portal.theme.consentAlertBigPrivacy}"' privacyLink='#i18n{portal.theme.consentPrivacyLink}' showIcon=true iconPosition='bottomRight' cookieMenu='#i18n{portal.theme.consentCookieMenu}' cookiePolicyLink='#i18n{portal.theme.consentCookiePolicyLink}' alertConfidentialityLabel='#i18n{portal.theme.consentAlertConfidentialityLabel}' alertConfidentialityLink='#i18n{portal.theme.consentAlertConfidentialityLink}' hashtag='cookiepolicycitelibre' cookiename='parisfr' nocredit=false deprecated...>
<@deprecatedWarning args=deprecated />
<link rel="stylesheet" href="${commonsSiteThemePath}${commonsSiteJsPath}vendor/orejime/orejime-standard.css" >
<link rel="stylesheet" href="${commonsSiteThemePath}${commonsSiteJsPath}vendor/orejime/orejime-parisfr.css" >
<script src="${commonsSiteThemePath}${commonsSiteJsPath}vendor/orejime/orejime-standard-fr.js"></script>
<#--  
-->
<script>
window.orejimeConfig = {
  translations: {
		banner: {
      accept: 'Tout accepter',
		  decline: 'Tout refuser',
			description: 'Afin de rendre ses services plus performants, d’améliorer l’expérience utilisateur et la manière dont les contenus vous sont présentés, nous analysons la façon dont le service numérique est utilisé grâce aux cookies ({purposes} ).\nPour en savoir plus, merci de lire notre {privacyPolicy}.\n',
		},
	},
	purposes: [
		{
			id: 'mandatory',
			title: 'Mon Paris',
			description: "Ceci ne peut pas être désactivé car c'est requis pour le bon fonctionnement du service. ",
			isMandatory: true
		},
		{
			id: 'matomo',
			title: 'Matomo Analytics',
			description: "Ceci ne peut pas être désactivé car c'est requis pour le bon fonctionnement du service. ",
      isMandatory: true,
      isExempt: true
		}
	],
	privacyPolicyUrl: '${privacyLink}',
  forceModal: false,
  forceBanner: false,
  logo: './themes/skin/parisfr/images/header-logo-paris.svg'
};

document.addEventListener('DOMContentLoaded', function() {
    const divOrejime = document.createElement('div');
    divOrejime.className = 'orejime-Env position-fixed end-0 bottom-0 me-m';
    const btnShowOrejime = document.createElement('button');
    btnShowOrejime.className = 'orejime-Button';
    btnShowOrejime.id = 'showConsent';
    btnShowOrejime.textContent = 'Cookies';
    divOrejime.appendChild(btnShowOrejime); 
    document.body.appendChild(divOrejime);

    const orejimeLogoContainer = document.querySelector('.orejime-Banner-logoContainer')
    if (orejimeLogoContainer) {
        const logoSeparator = document.createElement('hr');
        logoSeparator.setAttribute('aria-hidden', 'true');
        orejimeLogoContainer.appendChild(logoSeparator);
        const logoTitle = document.createElement('span');
        logoTitle.textContent = '${title}';
        orejimeLogoContainer.appendChild(logoTitle);
    }
    const orejimePrivacyLink = document.querySelector('.orejime-Banner-privacyPolicyLink');
    if (orejimePrivacyLink) {
        orejimePrivacyLink.setAttribute('target', '_blank');
    }
    const showConsent = document.querySelector('#showConsent')
    if( showConsent ){
        showConsent.addEventListener('click', function() {
            orejime.prompt();
        });
      } else {
        console.warn('Button with id "showConsent" not found.');
      }
});
</script>
</#macro>