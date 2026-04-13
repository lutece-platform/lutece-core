-- liquibase formatted sql
-- changeset core:upgrade_db_lutece_core-7.1.8-7.1.9.sql
-- preconditions onFail:MARK_RAN onError:WARN
-- LUT-32202 : Restrict XSL Export management to admin level 0 (system admin only)
UPDATE core_admin_right SET level_right = 0 WHERE id_right = 'CORE_XSL_EXPORT_MANAGEMENT';

-- changeset core:upgrade_db_lutece_core-7.1.8-7.1.9-rev1.sql
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

