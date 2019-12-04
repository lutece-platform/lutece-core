
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

CREATE TABLE core_temporary_file (
	id_file INT AUTO_INCREMENT,
	id_user int DEFAULT 0 NOT NULL,
	title LONG VARCHAR DEFAULT NULL, 
	description LONG VARCHAR DEFAULT NULL, 
	id_physical_file INT DEFAULT NULL,  
	file_size  INT DEFAULT NULL,
	mime_type VARCHAR(255) DEFAULT NULL,
	date_creation timestamp default CURRENT_TIMESTAMP NOT NULL,
	PRIMARY KEY (id_file)
);
