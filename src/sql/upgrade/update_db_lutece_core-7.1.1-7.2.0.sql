--
-- Remove core_theme_global -> core_datastore
--
DROP TABLE IF EXISTS core_theme_global;


--
-- Update core_theme datas
--
DROP TABLE IF EXISTS core_theme;
CREATE TABLE IF NOT EXISTS core_theme (
  code_theme varchar(25) COLLATE utf8_unicode_ci NOT NULL DEFAULT '',
  theme_description varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  path_images varchar(255) COLLATE utf8_unicode_ci NOT NULL,
  path_css varchar(255) COLLATE utf8_unicode_ci NOT NULL,
  theme_author varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  theme_author_url varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  theme_version varchar(255) COLLATE utf8_unicode_ci NOT NULL,
  theme_licence varchar(255) COLLATE utf8_unicode_ci NOT NULL,
  path_js varchar(255) COLLATE utf8_unicode_ci NOT NULL,
  PRIMARY KEY (code_theme)
) ENGINE=MyISAM DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

DELETE FROM core_theme;

INSERT INTO core_theme (code_theme, theme_description, path_images, path_css, theme_author, theme_author_url, theme_version, theme_licence, path_js) VALUES ('lutece', 'Lutece', 'images/', 'css', 'Ville de Paris', 'https://lutece.paris.fr', '1.0', 'BSD', 'js');

--
-- Update core_datastore keys
--
DELETE FROM core_datastore WHERE entity_key='theme.globalThemeCode';
INSERT INTO core_datastore VALUES ('theme.globalThemeCode', 'lutece');
DELETE FROM core_datastore WHERE entity_key='theme.globalThemeVersion';
INSERT INTO core_datastore VALUES ('theme.globalThemeVersion', '1.0');
DELETE FROM core_datastore WHERE entity_key='theme.site_property.banner';
INSERT INTO core_datastore VALUES ('theme.site_property.banner', 'images/banner.jpg');
DELETE FROM core_datastore WHERE entity_key='theme.site_property.banner.credits';
INSERT INTO core_datastore VALUES ('theme.site_property.banner.credits', '');
DELETE FROM core_datastore WHERE entity_key='theme.site_property.banner.image.positionx';
INSERT INTO core_datastore VALUES ('theme.site_property.banner.image.positionx', '');
DELETE FROM core_datastore WHERE entity_key='theme.site_property.banner.image.positiony';
INSERT INTO core_datastore VALUES ('theme.site_property.banner.image.positiony', '');
DELETE FROM core_datastore WHERE entity_key='theme.site_property.banner.shown.checkbox';
INSERT INTO core_datastore VALUES ('theme.site_property.banner.shown.checkbox', '1');
DELETE FROM core_datastore WHERE entity_key='theme.site_property.banner.showSiteImgEverywhere.checkbox';
INSERT INTO core_datastore VALUES ('theme.site_property.banner.showSiteImgEverywhere.checkbox', '0');
DELETE FROM core_datastore WHERE entity_key='theme.site_property.banner.title.bgcolor';
INSERT INTO core_datastore VALUES ('theme.site_property.banner.title.bgcolor', '#ffffff');
DELETE FROM core_datastore WHERE entity_key='theme.site_property.banner.title.color';
INSERT INTO core_datastore VALUES ('theme.site_property.banner.title.color', '#000000');
DELETE FROM core_datastore WHERE entity_key='theme.site_property.banner.title.padding';
INSERT INTO core_datastore VALUES ('theme.site_property.banner.title.padding', '');
DELETE FROM core_datastore WHERE entity_key='theme.site_property.bannerBrowserWarning.checkbox';
INSERT INTO core_datastore VALUES ('theme.site_property.bannerBrowserWarning.checkbox', '0');
DELETE FROM core_datastore WHERE entity_key='theme.site_property.bannerForm';
INSERT INTO core_datastore VALUES ('theme.site_property.bannerForm', '');
DELETE FROM core_datastore WHERE entity_key='theme.site_property.bannerForm.showBannerImg.checkbox';
INSERT INTO core_datastore VALUES ('theme.site_property.bannerForm.showBannerImg.checkbox', '1');
DELETE FROM core_datastore WHERE entity_key='theme.site_property.bannerForm.showFormImg.checkbox';
INSERT INTO core_datastore VALUES ('theme.site_property.bannerForm.showFormImg.checkbox', '0');
DELETE FROM core_datastore WHERE entity_key='theme.site_property.bannerForm.showFormTitle.checkbox';
INSERT INTO core_datastore VALUES ('theme.site_property.bannerForm.showFormTitle.checkbox', '1');
DELETE FROM core_datastore WHERE entity_key='theme.site_property.bannerMessage.CloseButton.checkbox';
INSERT INTO core_datastore VALUES ('theme.site_property.bannerMessage.CloseButton.checkbox', '1');
DELETE FROM core_datastore WHERE entity_key='theme.site_property.bannerMessage.Duration';
INSERT INTO core_datastore VALUES ('theme.site_property.bannerMessage.Duration', '0');
DELETE FROM core_datastore WHERE entity_key='theme.site_property.bannerMessage.htmlblock';
INSERT INTO core_datastore VALUES ('theme.site_property.bannerMessage.htmlblock', '');
DELETE FROM core_datastore WHERE entity_key='theme.site_property.bannerMessage.Position';
INSERT INTO core_datastore VALUES ('theme.site_property.bannerMessage.Position', 'top:1rem;right:1rem;');
DELETE FROM core_datastore WHERE entity_key='theme.site_property.bannerMessage.Title';
INSERT INTO core_datastore VALUES ('theme.site_property.bannerMessage.Title', '');
DELETE FROM core_datastore WHERE entity_key='theme.site_property.bannerMessage.Type';
INSERT INTO core_datastore VALUES ('theme.site_property.bannerMessage.Type', 'info');
DELETE FROM core_datastore WHERE entity_key='theme.site_property.config.datepicker.textblock';
INSERT INTO core_datastore VALUES ('theme.site_property.config.datepicker.textblock', 'daysOfWeekDisabled: [0,6],');
DELETE FROM core_datastore WHERE entity_key='theme.site_property.config.editor.textblock';
INSERT INTO core_datastore VALUES ('theme.site_property.config.editor.textblock', '');
DELETE FROM core_datastore WHERE entity_key='theme.site_property.layout.dir.checkbox';
INSERT INTO core_datastore VALUES ('theme.site_property.layout.dir.checkbox', '1');
DELETE FROM core_datastore WHERE entity_key='theme.site_property.layout.type.checkbox';
INSERT INTO core_datastore VALUES ('theme.site_property.layout.type.checkbox', '0');
DELETE FROM core_datastore WHERE entity_key='theme.site_property.link.showTargetIcon.checkbox';
INSERT INTO core_datastore VALUES ('theme.site_property.link.showTargetIcon.checkbox', '1');
DELETE FROM core_datastore WHERE entity_key='theme.site_property.menu.fixedMenu.checkbox';
INSERT INTO core_datastore VALUES ('theme.site_property.menu.fixedMenu.checkbox', '1');
DELETE FROM core_datastore WHERE entity_key='theme.site_property.menu.gototop.checkbox';
INSERT INTO core_datastore VALUES ('theme.site_property.menu.gototop.checkbox', '1');
DELETE FROM core_datastore WHERE entity_key='theme.site_property.menu.hasDefaultMenu.checkbox';
INSERT INTO core_datastore VALUES ('theme.site_property.menu.hasDefaultMenu.checkbox', '1');
DELETE FROM core_datastore WHERE entity_key='theme.site_property.menu.longMenu.checkbox';
INSERT INTO core_datastore VALUES ('theme.site_property.menu.longMenu.checkbox', '0');
DELETE FROM core_datastore WHERE entity_key='theme.site_property.menu.sidebarMenu.checkbox';
INSERT INTO core_datastore VALUES ('theme.site_property.menu.sidebarMenu.checkbox', '0');
DELETE FROM core_datastore WHERE entity_key='theme.site_property.menu.sidebarMenuCollapse.checkbox';
INSERT INTO core_datastore VALUES ('theme.site_property.menu.sidebarMenuCollapse.checkbox', '0');
DELETE FROM core_datastore WHERE entity_key='theme.site_property.menu.siteMapMenu.checkbox';
INSERT INTO core_datastore VALUES ('theme.site_property.menu.siteMapMenu.checkbox', '1');
DELETE FROM core_datastore WHERE entity_key='theme.site_property.menu.skipLinkMainId';
INSERT INTO core_datastore VALUES ('theme.site_property.menu.skipLinkMainId', 'main');
DELETE FROM core_datastore WHERE entity_key='theme.site_property.menu.skipLinkMenu.checkbox';
INSERT INTO core_datastore VALUES ('theme.site_property.menu.skipLinkMenu.checkbox', '0');
DELETE FROM core_datastore WHERE entity_key='theme.site_property.robotIndex.checkbox';
INSERT INTO core_datastore VALUES ('theme.site_property.robotIndex.checkbox', '0');
DELETE FROM core_datastore WHERE entity_key='theme.site_property.Url.accessibilityLabel';
INSERT INTO core_datastore VALUES ('theme.site_property.Url.accessibilityLabel', 'Acccesibilité partiellement conforme');
DELETE FROM core_datastore WHERE entity_key='theme.site_property.Url.accessibilityURL';
INSERT INTO core_datastore VALUES ('theme.site_property.Url.accessibilityURL', '');
DELETE FROM core_datastore WHERE entity_key='theme.site_property.Url.account';
INSERT INTO core_datastore VALUES ('theme.site_property.Url.account', '');
DELETE FROM core_datastore WHERE entity_key='theme.site_property.Url.auth';
INSERT INTO core_datastore VALUES ('theme.site_property.Url.auth', '');
DELETE FROM core_datastore WHERE entity_key='theme.site_property.Url.cguURL';
INSERT INTO core_datastore VALUES ('theme.site_property.Url.cguURL', '');
DELETE FROM core_datastore WHERE entity_key='theme.site_property.Url.cguURLLabel';
INSERT INTO core_datastore VALUES ('theme.site_property.Url.cguURLLabel', 'Condition générales d\'utilisation');
DELETE FROM core_datastore WHERE entity_key='theme.site_property.Url.clientId';
INSERT INTO core_datastore VALUES ('theme.site_property.Url.clientId', '');
DELETE FROM core_datastore WHERE entity_key='theme.site_property.Url.contactURL';
INSERT INTO core_datastore VALUES ('theme.site_property.Url.contactURL', 'jsp/site/Portal.jsp?page=contact&view=viewContactPage&id_contact_list=1');
DELETE FROM core_datastore WHERE entity_key='theme.site_property.Url.contactURLLabel';
INSERT INTO core_datastore VALUES ('theme.site_property.Url.contactURLLabel', 'Contact');
DELETE FROM core_datastore WHERE entity_key='theme.site_property.Url.cookieURL';
INSERT INTO core_datastore VALUES ('theme.site_property.Url.cookieURL', '');
DELETE FROM core_datastore WHERE entity_key='theme.site_property.Url.dataURL';
INSERT INTO core_datastore VALUES ('theme.site_property.Url.dataURL', 'jsp/site/PopupCredits.jsp');
DELETE FROM core_datastore WHERE entity_key='theme.site_property.Url.dataURLLabel';
INSERT INTO core_datastore VALUES ('theme.site_property.Url.dataURLLabel', 'Politique de gestion des données à caractère personnel');
DELETE FROM core_datastore WHERE entity_key='theme.site_property.Url.legalURL';
INSERT INTO core_datastore VALUES ('theme.site_property.Url.legalURL', 'jsp/site/PopupLegalInfo.jsp');
DELETE FROM core_datastore WHERE entity_key='theme.site_property.Url.legalURLLabel';
INSERT INTO core_datastore VALUES ('theme.site_property.Url.legalURLLabel', 'Mentions légales');
DELETE FROM core_datastore WHERE entity_key='theme.site_property.Url.search';
INSERT INTO core_datastore VALUES ('theme.site_property.Url.search', 'jsp/site/Portal.jsp?page=search');
DELETE FROM core_datastore WHERE entity_key='theme.site_property.Url.wikiURL';
INSERT INTO core_datastore VALUES ('theme.site_property.Url.wikiURL', 'https://lutece.paris.fr/support');
DELETE FROM core_datastore WHERE entity_key='theme.site_property.Url.wikiURLLabel';
INSERT INTO core_datastore VALUES ('theme.site_property.Url.wikiURLLabel', 'Wiki');
DELETE FROM core_datastore WHERE entity_key='theme.site_property.xss.xssChars';
INSERT INTO core_datastore VALUES ('theme.site_property.xss.xssChars', '<>#"&');
DELETE FROM core_datastore WHERE entity_key='theme.site_property.xss.xssMsg';
INSERT INTO core_datastore VALUES ('theme.site_property.xss.xssMsg', 'Les caract\\u00e8res &#60; &#62; &#35; et &#34;  &amp; sont interdits dans le contenu de votre message.');
