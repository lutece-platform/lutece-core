
-- removed or updated entries
DELETE FROM core_admin_dashboard WHERE dashboard_name = 'editorAdminDashboardComponent';

DELETE FROM core_admin_right WHERE id_right = 'CORE_SEARCH_MANAGEMENT';
DELETE FROM core_admin_right WHERE id_right = 'CORE_MODES_MANAGEMENT';
DELETE FROM core_admin_right WHERE id_right = 'CORE_FEATURES_MANAGEMENT';
DELETE FROM core_admin_right WHERE id_right = 'CORE_LEVEL_RIGHT_MANAGEMENT';
DELETE FROM core_admin_right WHERE id_right = 'CORE_ADMINDASHBOARD_MANAGEMENT';
DELETE FROM core_admin_right WHERE id_right = 'CORE_DASHBOARD_MANAGEMENT';
DELETE FROM core_admin_right WHERE id_right = 'CORE_XSL_EXPORT_MANAGEMENT';
DELETE FROM core_admin_right WHERE id_right = 'CORE_GLOBAL_MANAGEMENT';
DELETE FROM core_admin_right WHERE id_right = 'CORE_EXTERNAL_FEATURES_MANAGEMENT';

DELETE FROM core_user_right WHERE id_right = 'CORE_EXTERNAL_FEATURES_MANAGEMENT';
DELETE FROM core_user_right WHERE id_right = 'CORE_EXTERNAL_FEATURES_MANAGEMENT';
DELETE FROM core_user_right WHERE id_right = 'CORE_GLOBAL_MANAGEMENT';

TRUNCATE TABLE core_text_editor ;


-- new or updated entries
INSERT INTO core_admin_dashboard(dashboard_name, dashboard_column, dashboard_order) VALUES('editorAdminDashboardComponent', 1, 3);
INSERT INTO core_admin_dashboard(dashboard_name, dashboard_column, dashboard_order) VALUES('autoIncludesAdminDashboardComponent', 1, 4);
INSERT INTO core_admin_dashboard(dashboard_name, dashboard_column, dashboard_order) VALUES('featuresAdminDashboardComponent', 1, 5);
INSERT INTO core_admin_dashboard(dashboard_name, dashboard_column, dashboard_order) VALUES('xslExportAdminDashboardComponent', 1, 6);


INSERT INTO core_admin_right VALUES ('CORE_SEARCH_MANAGEMENT','portal.search.adminFeature.search_management.name',0,NULL,'portal.search.adminFeature.search_management.description',0,NULL,'SYSTEM',NULL,NULL,3, 0);
INSERT INTO core_admin_right VALUES ('CORE_FEATURES_MANAGEMENT','portal.admin.adminFeature.features_management.name',0,NULL,'portal.admin.adminFeature.features_management.description',0,NULL,'SYSTEM','images/admin/skin/features/manage_features.png',NULL,6, 0);
INSERT INTO core_admin_right VALUES ('CORE_LEVEL_RIGHT_MANAGEMENT','portal.users.adminFeature.level_right_management.name',2,NULL,'portal.users.adminFeature.level_right_management.description',0,NULL,'MANAGERS','images/admin/skin/features/manage_rights_levels.png',NULL,5, 0);
INSERT INTO core_admin_right VALUES ('CORE_ADMINDASHBOARD_MANAGEMENT', 'portal.admindashboard.adminFeature.right_management.name', 0, NULL, 'portal.admindashboard.adminFeature.right_management.description', 0, NULL, 'SYSTEM', 'images/admin/skin/features/manage_admindashboards.png', NULL, 8, 0);
INSERT INTO core_admin_right VALUES ('CORE_DASHBOARD_MANAGEMENT', 'portal.dashboard.adminFeature.dashboard_management.name', 0, NULL, 'portal.dashboard.adminFeature.dashboard_management.description', 0, NULL, 'SYSTEM', 'images/admin/skin/features/manage_dashboards.png', NULL, 9, 0);
INSERT INTO core_admin_right VALUES ('CORE_XSL_EXPORT_MANAGEMENT', 'portal.xsl.adminFeature.xsl_export_management.name', 2, NULL, 'portal.xsl.adminFeature.xsl_export_management.description', 1, NULL, 'SYSTEM', NULL, NULL, 10, 0);
INSERT INTO core_admin_right VALUES ('CORE_TEMPLATES_AUTO_INCLUDES_MANAGEMENT', 'portal.templates.adminFeature.ManageAutoIncludes.name', 1, NULL,'portal.templates.adminFeature.ManageAutoIncludes.description',1,NULL,'STYLE','images/admin/skin/features/manage_templates.png',NULL,4, 0);
INSERT INTO core_admin_right VALUES ('CORE_EDITORS_MANAGEMENT', 'portal.admindashboard.editorManagement.right.name', 2, NULL, 'portal.admindashboard.editorManagement.right.description', 1, NULL, 'SYSTEM', NULL, NULL, 10, 0);

INSERT INTO core_user_right VALUES ('CORE_TEMPLATES_AUTO_INCLUDES_MANAGEMENT', 1);
INSERT INTO core_user_right VALUES ('CORE_EDITORS_MANAGEMENT', 1);

INSERT INTO core_text_editor VALUES ( 'tinymce', 'portal.admindashboard.editors.labelBackTinyMCE', 1 );
INSERT INTO core_text_editor VALUES ( '', 'portal.admindashboard.editors.labelBackNoEditor', 1 );
INSERT INTO core_text_editor VALUES ( '', 'portal.admindashboard.editors.labelFrontNoEditor', 0 );
INSERT INTO core_text_editor VALUES ( 'markitupbbcode', 'portal.admindashboard.editors.labelFrontMarkitupBBCode', 0 );

ALTER TABLE core_admin_mailinglist modify COLUMN id_mailinglist int AUTO_INCREMENT NOT NULL;
ALTER TABLE core_admin_role_resource modify COLUMN rbac_id int AUTO_INCREMENT NOT NULL;
ALTER TABLE core_admin_user modify COLUMN id_user int AUTO_INCREMENT NOT NULL;
ALTER TABLE core_file modify COLUMN id_file int AUTO_INCREMENT NOT NULL;
ALTER TABLE core_indexer_action modify COLUMN id_action int AUTO_INCREMENT NOT NULL;
ALTER TABLE core_page modify COLUMN id_page int AUTO_INCREMENT NOT NULL;
ALTER TABLE core_page_template modify COLUMN id_template int AUTO_INCREMENT NOT NULL;
ALTER TABLE core_physical_file modify COLUMN id_physical_file int AUTO_INCREMENT NOT NULL;
ALTER TABLE core_portlet modify COLUMN id_portlet int AUTO_INCREMENT NOT NULL;
ALTER TABLE core_stylesheet modify COLUMN id_stylesheet int AUTO_INCREMENT NOT NULL;
ALTER TABLE core_attribute modify COLUMN id_attribute int AUTO_INCREMENT NOT NULL;
ALTER TABLE core_attribute_field modify COLUMN id_field int AUTO_INCREMENT NOT NULL;
ALTER TABLE core_admin_user_field modify COLUMN id_user_field int AUTO_INCREMENT NOT NULL;
ALTER TABLE core_xsl_export modify COLUMN id_xsl_export int AUTO_INCREMENT NOT NULL;

UPDATE core_mode SET output_xsl_standalone=NULL WHERE output_xsl_omit_xml_dec='yes';

DELETE FROM `core_datastore` WHERE `entity_key`='portal.site.site_property.menu.position';
DELETE FROM `core_datastore` WHERE `entity_key`='portal.site.site_property.back_images';
INSERT INTO `core_datastore` (`entity_key`, `entity_value`) VALUES ('portal.site.site_property.back_images', '\'images/admin/skin/bg_login1.svg\' , \'images/admin/skin/bg_login2.svg\' , \'images/admin/skin/bg_login3.svg\' , \'images/admin/skin/bg_login4.svg\'');