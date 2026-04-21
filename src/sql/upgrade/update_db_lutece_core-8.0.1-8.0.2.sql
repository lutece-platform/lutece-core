-- liquibase formatted sql
-- changeset core:update_db_lutece_core-8.0.1-8.0.2.sql
-- preconditions onFail:MARK_RAN onError:WARN
-- LUT-32220 : Restrict XSL Export management to admin level 0 (system admin only)
UPDATE core_admin_right SET level_right = 0 WHERE id_right = 'CORE_XSL_EXPORT_MANAGEMENT';

-- changeset core:update_db_lutece_core-8.0.1-8.0.2.sql-rev1.sql
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
INSERT INTO core_datastore VALUES ('portal.theme.site_property.sessiontimeout.enabled.checkbox', '1');
INSERT INTO core_datastore VALUES ('portal.theme.site_property.sessiontimeout.duration', '1800');
INSERT INTO core_datastore VALUES ('portal.theme.site_property.sessiontimeout.warningDelay', '120');
INSERT INTO core_datastore VALUES ('portal.theme.site_property.sessiontimeout.keepAliveUrl', '');
INSERT INTO core_datastore VALUES ('portal.theme.site_property.sessiontimeout.maxExtensions', '10');
INSERT INTO core_datastore VALUES ('portal.theme.site_property.sessiontimeout.position', 'top-0 end-0');

INSERT INTO core_datastore VALUES ('portal.theme.site_property.layout.footer.logoFooter', '');
INSERT INTO core_datastore VALUES ('portal.theme.site_property.layout.footer.logoFooterAlt', '');

INSERT INTO core_datastore VALUES ('portal.theme.site_property.consent.platform.select', 'tarte_au_citron');
INSERT INTO core_datastore VALUES ('portal.theme.site_property.consent.select.options', 'tarte_au_citron|orejime');