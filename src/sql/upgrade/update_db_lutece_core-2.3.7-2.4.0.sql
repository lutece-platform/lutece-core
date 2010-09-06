ALTER TABLE core_theme ADD COLUMN path_js varchar(255) default NULL;

UPDATE core_theme SET path_css='themes/black/css', path_js='theme/black/js' WHERE code_theme='black';
UPDATE core_theme SET path_css='css', path_js='js' WHERE code_theme='blue';

INSERT INTO core_admin_right VALUES ( 'CORE_RIGHT_MANAGEMENT', 'portal.users.adminFeature.right_management.name', 0, 'jsp/admin/features/ManageRights.jsp', 'portal.users.adminFeature.right_management.description', 0, NULL, 'MANAGERS', 'images/admin/skin/features/manage_rights_levels.png', NULL, 5 );

INSERT INTO core_user_right VALUES ('CORE_RIGHT_MANAGEMENT',1);

DROP TABLE IF EXISTS core_parameters;

--
-- Table structure for table core_user_parameters
--
DROP TABLE IF EXISTS core_user_parameter;
CREATE TABLE core_user_parameter (
	parameter_key varchar(100) NOT NULL,
	parameter_value varchar(100) NOT NULL,
	PRIMARY KEY (parameter_key)
);

INSERT INTO core_user_parameter VALUES ('password_duration', '120');
INSERT INTO core_user_parameter VALUES ('enable_password_encryption', 'false');
INSERT INTO core_user_parameter VALUES ('encryption_algorithm', '');
INSERT INTO core_user_parameter VALUES ('default_user_level', '0');
INSERT INTO core_user_parameter VALUES ('default_user_notification', '1');
INSERT INTO core_user_parameter VALUES ('default_user_language', 'fr');
INSERT INTO core_user_parameter VALUES ('default_user_status', '0');

INSERT INTO core_admin_role_resource VALUES (111, 'all_site_manager', 'ADMIN_USER', '*', '*');
INSERT INTO core_admin_role_resource VALUES (120, 'all_site_manager', 'ADVANCED_PARAMETER', '*', '*');

DELETE FROM core_admin_right WHERE id_right = 'CORE_GROUPS_MANAGEMENT';
