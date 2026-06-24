-- liquibase formatted sql
-- changeset core:update_db_lutece_core-8.0.2-8.0.3.sql
-- preconditions onFail:MARK_RAN onError:WARN
-- Route theme management through the admin MVC front-controller instead of the legacy JSP
UPDATE core_admin_right SET admin_url = 'jsp/admin/mvc/theme' WHERE id_right = 'CORE_THEME_MANAGEMENT';
UPDATE core_admin_right SET admin_url = 'jsp/admin/mvc/securityheader' WHERE id_right = 'CORE_SECURITY_HEADER_MANAGEMENT';
UPDATE core_admin_right SET admin_url = 'jsp/admin/mvc/cache' WHERE id_right = 'CORE_CACHE_MANAGEMENT';
UPDATE core_admin_right SET admin_url = 'jsp/admin/mvc/rights' WHERE id_right = 'CORE_RIGHT_MANAGEMENT';
UPDATE core_admin_right SET admin_url = 'jsp/admin/mvc/daemons' WHERE id_right = 'CORE_DAEMONS_MANAGEMENT';
UPDATE core_admin_right SET admin_url = 'jsp/admin/mvc/pagerole' WHERE id_right = 'CORE_ROLES_MANAGEMENT';
UPDATE core_admin_right SET admin_url = 'jsp/admin/mvc/workgroups' WHERE id_right = 'CORE_WORKGROUPS_MANAGEMENT';
UPDATE core_admin_right SET admin_url = 'jsp/admin/mvc/mailinglists' WHERE id_right = 'CORE_MAILINGLISTS_MANAGEMENT';
UPDATE core_admin_right SET admin_url = 'jsp/admin/mvc/pagetemplates' WHERE id_right = 'CORE_PAGE_TEMPLATE_MANAGEMENT';
UPDATE core_admin_right SET admin_url = 'jsp/admin/mvc/rbac' WHERE id_right = 'CORE_RBAC_MANAGEMENT';
UPDATE core_admin_right SET admin_url = 'jsp/admin/mvc/plugins' WHERE id_right = 'CORE_PLUGINS_MANAGEMENT';
UPDATE core_admin_right SET admin_url = 'jsp/admin/mvc/properties' WHERE id_right = 'CORE_PROPERTIES_MANAGEMENT';
UPDATE core_admin_right SET admin_url = 'jsp/admin/mvc/searchindexation' WHERE id_right = 'CORE_SEARCH_INDEXATION';
