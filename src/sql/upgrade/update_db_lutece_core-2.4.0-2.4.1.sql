--
-- Table structure for table core_user_parameters
--
DROP TABLE IF EXISTS core_admin_dashboard;
CREATE TABLE core_admin_dashboard (
	dashboard_name varchar(100) NOT NULL,
	dashboard_column int NOT NULL,
	dashboard_order int NOT NULL,
	PRIMARY KEY (dashboard_name)
);

INSERT INTO core_admin_dashboard(dashboard_name, dashboard_column, dashboard_order) VALUES('usersAdminDashboardComponent', 1, 1);

INSERT INTO core_admin_right VALUES ('CORE_ADMINDASHBOARD_MANAGEMENT', 'portal.admindashboard.adminFeature.right_management.name', 0, 'jsp/admin/admindashboard/ManageAdminDashboards.jsp', 'portal.admindashboard.adminFeature.right_management.description', 0, NULL, 'SYSTEM', 'images/admin/skin/features/manage_admindashboards.png', NULL, 7);
INSERT INTO core_user_right VALUES ('CORE_ADMINDASHBOARD_MANAGEMENT', 1);

--
-- Table structure for table core_attribute
--
DROP TABLE IF EXISTS core_attribute;
CREATE TABLE core_attribute (
	id_attribute INT DEFAULT 0 NOT NULL,
	type_class_name VARCHAR(255) DEFAULT NULL,
	title LONG VARCHAR DEFAULT NULL,
	help_message LONG VARCHAR DEFAULT NULL,
	is_mandatory SMALLINT DEFAULT 0,
	is_shown_in_search SMALLINT DEFAULT 0,
	attribute_position INT DEFAULT 0,
	plugin_name VARCHAR(255) DEFAULT NULL,
	PRIMARY KEY (id_attribute)
);

--
-- Table structure for table core_attribute_field
--
DROP TABLE IF EXISTS core_attribute_field;
CREATE TABLE core_attribute_field (
	id_field INT DEFAULT 0 NOT NULL,
	id_attribute INT DEFAULT NULL,
	title VARCHAR(255) DEFAULT NULL,
	DEFAULT_value LONG VARCHAR DEFAULT NULL,
	is_DEFAULT_value SMALLINT DEFAULT 0,
	height INT DEFAULT NULL,
	width INT DEFAULT NULL,
	max_size_enter INT DEFAULT NULL,
	is_multiple SMALLINT DEFAULT 0,
	field_position INT DEFAULT NULL,
	PRIMARY KEY (id_field)
);

--
-- Table structure for table core_admin_user_field
--
DROP TABLE IF EXISTS core_admin_user_field;
CREATE TABLE core_admin_user_field (
	id_user_field INT DEFAULT 0 NOT NULL,
	id_user INT DEFAULT NULL,
	id_attribute INT DEFAULT NULL,
	id_field INT DEFAULT NULL,
	user_field_value LONG VARCHAR DEFAULT NULL,
	PRIMARY KEY (id_user_field)
);

ALTER TABLE core_admin_user ADD COLUMN reset_password SMALLINT DEFAULT 0 NOT NULL;

-- core_connection_logs
ALTER TABLE core_connections_log MODIFY COLUMN ip_address varchar(63) default NULL;

-- core_role
ALTER TABLE core_role ADD COLUMN workgroup_key varchar(50) DEFAULT '' NOT NULL;
