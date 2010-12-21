--
-- Table structure for table core_dashboard
--
DROP TABLE IF EXISTS core_dashboard;
CREATE TABLE core_dashboard (
	dashboard_name varchar(100) NOT NULL,
	dashboard_column int NOT NULL,
	dashboard_order int NOT NULL,
	PRIMARY KEY (dashboard_name)
);

--
-- Dumping data in table core_dashboard
--
INSERT INTO core_dashboard(dashboard_name, dashboard_column, dashboard_order) VALUES('CORE_SYSTEM', 1, 3);
INSERT INTO core_dashboard(dashboard_name, dashboard_column, dashboard_order) VALUES('CORE_USERS', 1, 1);
INSERT INTO core_dashboard(dashboard_name, dashboard_column, dashboard_order) VALUES('CORE_USER', 4, 1);
INSERT INTO core_dashboard(dashboard_name, dashboard_column, dashboard_order) VALUES('CORE_PAGES', 1, 2);

--
-- Update core_admin_resource
--
DELETE FROM core_admin_role_resource WHERE resource_type LIKE 'ADVANCED_PARAMETER';

--
-- Update core_admin_right
--
INSERT INTO core_admin_right VALUES ('CORE_DASHBOARD_MANAGEMENT', 'portal.dashboard.adminFeature.dashboard_management.name', 0, 'jsp/admin/dashboard/ManageDashboards.jsp', 'portal.dashboard.adminFeature.dashboard_management.description', 0, NULL, 'SYSTEM', 'images/admin/skin/features/manage_dashboards.png', NULL, 8);

--
-- Update core_attribute
--
ALTER TABLE core_attribute ADD is_shown_in_result_list SMALLINT DEFAULT 0 AFTER is_shown_in_search;
ALTER TABLE core_attribute ADD is_field_in_line SMALLINT DEFAULT 0 AFTER is_shown_in_result_list;

--
-- Table structure for table core_file
--
DROP TABLE IF EXISTS core_file;
CREATE TABLE core_file (
	id_file INT DEFAULT 0 NOT NULL,
	title LONG VARCHAR DEFAULT NULL, 
	id_physical_file INT DEFAULT NULL,  
	file_size  INT DEFAULT NULL,
	mime_type VARCHAR(255) DEFAULT NULL,
	PRIMARY KEY (id_file)
);

--
-- Table structure for table core_physical_file
--
DROP TABLE IF EXISTS core_physical_file;
CREATE TABLE core_physical_file (
	id_physical_file INT DEFAULT 0 NOT NULL,
	file_value LONG VARBINARY,  
	PRIMARY KEY (id_physical_file)
);

--
-- Update core_admin_user_field
--
ALTER TABLE core_admin_user_field ADD id_file INT DEFAULT NULL AFTER id_field;
