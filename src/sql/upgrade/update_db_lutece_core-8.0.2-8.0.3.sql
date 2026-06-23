-- liquibase formatted sql
-- changeset core:update_db_lutece_core-8.0.2-8.0.3.sql
-- preconditions onFail:MARK_RAN onError:WARN
-- Route theme management through the admin MVC front-controller instead of the legacy JSP
UPDATE core_admin_right SET admin_url = 'jsp/admin/mvc/theme' WHERE id_right = 'CORE_THEME_MANAGEMENT';
UPDATE core_admin_right SET admin_url = 'jsp/admin/mvc/securityheader' WHERE id_right = 'CORE_SECURITY_HEADER_MANAGEMENT';
