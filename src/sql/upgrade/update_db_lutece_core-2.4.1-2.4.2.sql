--
-- Table structure for table core_admin_dashboard
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
