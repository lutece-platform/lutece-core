-- liquibase formatted sql
-- changeset core:update_db_lutece_core-8.0.1-8.0.2.sql
-- preconditions onFail:MARK_RAN onError:WARN
-- LUT-32220 : Restrict XSL Export management to admin level 0 (system admin only)
UPDATE core_admin_right SET level_right = 0 WHERE id_right = 'CORE_XSL_EXPORT_MANAGEMENT';

-- changeset core:update_db_lutece_core-8.0.1-8.0.2.sql-rev1.sql
-- preconditions onFail:MARK_RAN onError:WARN
-- comment Legacy XSL style tables are absent from databases created with the 8.x scripts: skip instead of failing the run
-- precondition-sql-check expectedResult:3 SELECT COUNT(1) from INFORMATION_SCHEMA.TABLES WHERE TABLE_SCHEMA=database() AND TABLE_NAME IN ('core_style_mode_stylesheet','core_stylesheet','core_style');
DELETE FROM core_style_mode_stylesheet WHERE id_style = 3 AND id_mode = 0 and id_stylesheet = 211;
DELETE FROM core_style_mode_stylesheet WHERE id_style = 4 AND id_mode = 0 and id_stylesheet = 213;
DELETE FROM core_style_mode_stylesheet WHERE id_style = 5 AND id_mode = 0 and id_stylesheet = 215;
DELETE FROM core_style_mode_stylesheet WHERE id_style = 6 AND id_mode = 0 and id_stylesheet = 217;
DELETE FROM core_style_mode_stylesheet WHERE id_style = 7 AND id_mode = 0 and id_stylesheet = 253;
DELETE FROM core_style_mode_stylesheet WHERE id_style = 8 AND id_mode = 1 and id_stylesheet = 279;

DELETE FROM core_stylesheet where id_stylesheet = 211;
DELETE FROM core_stylesheet where id_stylesheet = 213;
DELETE FROM core_stylesheet where id_stylesheet = 215;
DELETE FROM core_stylesheet where id_stylesheet = 217;
DELETE FROM core_stylesheet where id_stylesheet = 253;
DELETE FROM core_stylesheet where id_stylesheet = 279;

DELETE FROM core_style WHERE id_style = 3 and id_portal_component = 3;
DELETE FROM core_style WHERE id_style = 4 and id_portal_component = 4;
DELETE FROM core_style WHERE id_style = 5 and id_portal_component = 5;
DELETE FROM core_style WHERE id_style = 6 and id_portal_component = 6;
DELETE FROM core_style WHERE id_style = 7 and id_portal_component = 7;
DELETE FROM core_style WHERE id_style = 8 and id_portal_component = 8;

DELETE FROM core_portal_component WHERE id_portal_component = 3;
DELETE FROM core_portal_component WHERE id_portal_component = 4;
DELETE FROM core_portal_component WHERE id_portal_component = 5;
DELETE FROM core_portal_component WHERE id_portal_component = 6;
DELETE FROM core_portal_component WHERE id_portal_component = 7;
DELETE FROM core_portal_component WHERE id_portal_component = 8;

DELETE FROM core_user_right WHERE id_right = 'CORE_STYLES_MANAGEMENT';
DELETE FROM core_user_right WHERE id_right = 'CORE_STYLESHEET_MANAGEMENT';

DELETE FROM core_admin_right WHERE id_right = 'CORE_STYLES_MANAGEMENT';
DELETE FROM core_admin_right WHERE id_right = 'CORE_STYLESHEET_MANAGEMENT';

DROP TABLE IF EXISTS core_xsl_export;
DELETE FROM core_user_right WHERE id_right = 'CORE_XSL_EXPORT_MANAGEMENT';
DELETE FROM core_admin_right WHERE id_right = 'CORE_XSL_EXPORT_MANAGEMENT';
DELETE FROM core_admin_role_resource where resource_type = 'XSL_EXPORT';
DELETE FROM core_admin_dashboard WHERE dashboard_name = 'xslExportAdminDashboardComponent';

-- changeset core:update_db_lutece_core-8.0.1-8.0.2-rev2.sql
DELETE FROM core_datastore WHERE entity_key = 'portal.theme.site_property.sessiontimeout.enabled.checkbox';
INSERT INTO core_datastore VALUES ('portal.theme.site_property.sessiontimeout.enabled.checkbox', '1');
DELETE FROM core_datastore WHERE entity_key = 'portal.theme.site_property.sessiontimeout.duration';
INSERT INTO core_datastore VALUES ('portal.theme.site_property.sessiontimeout.duration', '1800');
DELETE FROM core_datastore WHERE entity_key = 'portal.theme.site_property.sessiontimeout.warningDelay';
INSERT INTO core_datastore VALUES ('portal.theme.site_property.sessiontimeout.warningDelay', '120');
DELETE FROM core_datastore WHERE entity_key = 'portal.theme.site_property.sessiontimeout.keepAliveUrl';
INSERT INTO core_datastore VALUES ('portal.theme.site_property.sessiontimeout.keepAliveUrl', '');
DELETE FROM core_datastore WHERE entity_key = 'portal.theme.site_property.sessiontimeout.maxExtensions';
INSERT INTO core_datastore VALUES ('portal.theme.site_property.sessiontimeout.maxExtensions', '10');
DELETE FROM core_datastore WHERE entity_key = 'portal.theme.site_property.sessiontimeout.position';
INSERT INTO core_datastore VALUES ('portal.theme.site_property.sessiontimeout.position', 'top-0 end-0');

DELETE FROM core_datastore WHERE entity_key = 'portal.theme.site_property.layout.footer.logoFooter';
INSERT INTO core_datastore VALUES ('portal.theme.site_property.layout.footer.logoFooter', '');
DELETE FROM core_datastore WHERE entity_key = 'portal.theme.site_property.layout.footer.logoFooterAlt';
INSERT INTO core_datastore VALUES ('portal.theme.site_property.layout.footer.logoFooterAlt', '');
DELETE FROM core_datastore WHERE entity_key='portal.theme.site_property.layout.footer.logoFooterUrl';
INSERT INTO core_datastore VALUES ('portal.theme.site_property.layout.footer.logoFooterUrl', '');

DELETE FROM core_datastore WHERE entity_key = 'portal.theme.site_property.consent.platform.select';
INSERT INTO core_datastore VALUES ('portal.theme.site_property.consent.platform.select', 'tarte_au_citron');
DELETE FROM core_datastore WHERE entity_key = 'portal.theme.site_property.consent.select.options';
INSERT INTO core_datastore VALUES ('portal.theme.site_property.consent.select.options', 'tarte_au_citron|orejime');

-- changeset core:update_db_lutece_core-8.0.1-8.0.2.sql-rev2.sql
-- Migrate the form validation error icon from the Paris.fr SVG sprite to a Tabler font icon
DELETE FROM core_datastore WHERE entity_key = 'portal.theme.site_property.formvalidation.errorIconSvg';
INSERT INTO core_datastore VALUES ('portal.theme.site_property.formvalidation.errorIconSvg', '<i class="ti ti-alert-triangle main-danger-color me-xs" aria-hidden="true"></i>');
DELETE FROM core_datastore WHERE entity_key='portal.theme.site_property.menu.userthemeswitch.checkbox';
DELETE FROM core_datastore WHERE entity_key='portal.theme.site_property.menu.user.themes.switch.checkbox';
DELETE FROM core_datastore WHERE entity_key='portal.theme.site_property.menu.user.themes.color.checkbox';
DELETE FROM core_datastore WHERE entity_key='portal.theme.site_property.menu.user.themes.density.checkbox';
INSERT INTO core_datastore VALUES ('portal.theme.site_property.menu.user.theme.switch.checkbox', '0');
INSERT INTO core_datastore VALUES ('portal.theme.site_property.menu.user.themes.color.checkbox', '0');
INSERT INTO core_datastore VALUES ('portal.theme.site_property.menu.user.themes.density.checkbox', '0');

-- changeset core:update_db_lutece_core-8.0.1-8.0.2-rev3.sql
DELETE FROM core_datastore WHERE entity_key='portal.theme.site_property.menu.translate.lang';
INSERT INTO core_datastore VALUES ('portal.theme.site_property.menu.translate.lang', 'fr');

-- changeset core:update_db_lutece_core-8.0.1-8.0.2-rev4.sql
DELETE FROM core_datastore WHERE entity_key='portal.theme.site_property.forms.showResponseFormWarning.checkbox';
INSERT INTO core_datastore VALUES ('portal.theme.site_property.forms.showResponseFormWarning.checkbox', '1');
DELETE FROM core_datastore WHERE entity_key='portal.theme.site_property.forms.labelInfoResponse';
INSERT INTO core_datastore VALUES ('portal.theme.site_property.forms.labelInfoResponse','Formulaire enregistré !');

-- changeset core:update_db_lutece_core-8.0.1-8.0.2-rev5.sql
DELETE FROM core_datastore WHERE entity_key='portal.theme.site_property.showSiteImg.checkbox';
DELETE FROM core_datastore WHERE entity_key='portal.theme.site_property.banner.title.checkbox';
DELETE FROM core_datastore WHERE entity_key='portal.theme.site_property.banner.title';
INSERT INTO core_datastore VALUES ('portal.theme.site_property.banner.showSiteImg.checkbox', '0');
INSERT INTO core_datastore VALUES ('portal.theme.site_property.banner.title.checkbox', '1');
INSERT INTO core_datastore VALUES ('portal.theme.site_property.banner.title', '');

-- changeset core:update_db_lutece_core-8.0.1-8.0.2-rev6.sql
-- LUT : Add site property to enable/disable admin home dashboard widget management
DELETE FROM core_datastore WHERE entity_key='portal.site.site_property.bo.widget.checkbox';
INSERT INTO core_datastore VALUES ('portal.site.site_property.bo.widget.checkbox', '1');

-- changeset core:update_db_lutece_core-8.0.1-8.0.2-rev7.sql
-- LUT : Add site property to enable/disable admin home dashboard widget management
DELETE FROM core_datastore WHERE entity_key='portal.theme.site_property.banner.showSiteImgEverywhere.checkbox';
INSERT INTO core_datastore VALUES ('portal.theme.site_property.banner.onlyhome.checkbox', '0');

-- changeset core:update_db_lutece_core-8.0.1-8.0.2-rev8.sql
-- Fix misspelled user theme menu keys inserted by rev2 (theme/themes, color/colors),
-- realigning them with the keys read by global_theme_commons.ftl
DELETE FROM core_datastore WHERE entity_key='portal.theme.site_property.menu.user.theme.switch.checkbox';
DELETE FROM core_datastore WHERE entity_key='portal.theme.site_property.menu.user.themes.switch.checkbox';
INSERT INTO core_datastore VALUES ('portal.theme.site_property.menu.user.themes.switch.checkbox', '0');
DELETE FROM core_datastore WHERE entity_key='portal.theme.site_property.menu.user.themes.color.checkbox';
DELETE FROM core_datastore WHERE entity_key='portal.theme.site_property.menu.user.themes.colors.checkbox';
INSERT INTO core_datastore VALUES ('portal.theme.site_property.menu.user.themes.colors.checkbox', '0');

-- changeset core:update_db_lutece_core-8.0.1-8.0.2-rev9.sql
-- Databases upgraded from 7.x still reference skin/site/page_home_demo.html (7.0.9/7.0.10 scripts),
-- renamed to page_demo.html in 8.x: realign the page template reference
UPDATE core_page_template SET file_name = 'skin/site/page_demo.html' WHERE file_name = 'skin/site/page_home_demo.html';

-- changeset core:update_db_lutece_core-8.0.1-8.0.2-rev10.sql
-- The consent platform options key was misnamed (rev2): the site properties page builds the
-- options key as '<select key>.options', so the consent platform select was rendered empty
DELETE FROM core_datastore WHERE entity_key='portal.theme.site_property.consent.platform.select.options';
UPDATE core_datastore SET entity_key='portal.theme.site_property.consent.platform.select.options' WHERE entity_key='portal.theme.site_property.consent.select.options';

-- changeset core:update_db_lutece_core-8.0.1-8.0.2-rev11.sql
-- Add default value for the active user's favorites menu checkbox
DELETE FROM core_datastore WHERE entity_key='portal.theme.site_property.menu.activeUserFavs.checkbox';
INSERT INTO core_datastore VALUES ('portal.theme.site_property.menu.activeUserFavs.checkbox', '0');

-- changeset core:update_db_lutece_core-8.0.1-8.0.2-rev12.sql
-- preconditions onFail:MARK_RAN onError:WARN
-- comment FreeMarker template chosen for each portlet : replaces the XSL styles removed from the core. 0 means the default template of the portlet type.
-- precondition-sql-check expectedResult:0 SELECT COUNT(*) FROM information_schema.columns WHERE table_schema = database() AND table_name = 'core_portlet' AND column_name = 'id_template'
ALTER TABLE core_portlet ADD COLUMN id_template int default 0 NOT NULL;

-- changeset core:update_db_lutece_core-8.0.1-8.0.2-rev13.sql
-- preconditions onFail:MARK_RAN onError:WARN
-- comment The FreeMarker templates available to render the portlets of a type, managed from the back office
-- precondition-sql-check expectedResult:0 SELECT COUNT(*) FROM information_schema.tables WHERE table_schema = database() AND table_name = 'core_portlet_template'
CREATE TABLE IF NOT EXISTS core_portlet_template (
	id_template int AUTO_INCREMENT NOT NULL,
	id_portlet_type varchar(50) default NULL,
	description varchar(255) default NULL,
	template_path varchar(255) default NULL,
	PRIMARY KEY (id_template)
);

-- changeset core:update_db_lutece_core-8.0.1-8.0.2-rev14.sql
-- preconditions onFail:MARK_RAN onError:WARN
-- comment Admin feature : management of the portlet templates
DELETE FROM core_user_right WHERE id_right = 'CORE_PORTLET_TEMPLATE_MANAGEMENT';
DELETE FROM core_admin_right WHERE id_right = 'CORE_PORTLET_TEMPLATE_MANAGEMENT';
INSERT INTO core_admin_right VALUES ('CORE_PORTLET_TEMPLATE_MANAGEMENT', 'portal.style.adminFeature.portlet_template_management.name', 0, 'jsp/admin/style/ManagePortletTemplates.jsp', 'portal.style.adminFeature.portlet_template_management.description', 0, '', 'STYLE', 'ti ti-template', NULL, 2, 0);
INSERT INTO core_user_right VALUES ('CORE_PORTLET_TEMPLATE_MANAGEMENT',1);
