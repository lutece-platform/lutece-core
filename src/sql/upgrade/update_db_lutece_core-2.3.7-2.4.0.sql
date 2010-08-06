ALTER TABLE core_theme ADD COLUMN path_js varchar(255) default NULL;

UPDATE core_theme SET path_css='themes/black/css', path_js='theme/black/js' WHERE code_theme='black';
UPDATE core_theme SET path_css='css', path_js='js' WHERE code_theme='blue';

INSERT INTO core_admin_right VALUES ( 'CORE_RIGHT_MANAGEMENT', 'portal.users.adminFeature.right_management.name', 0, 'jsp/admin/features/ManageRights.jsp', 'portal.users.adminFeature.right_management.description', 0, NULL, 'MANAGERS', 'images/admin/skin/features/manage_rights_levels.png', NULL, 5 );

INSERT INTO core_user_right VALUES ('CORE_RIGHT_MANAGEMENT',1);
INSERT INTO core_user_right VALUES ('CORE_RIGHT_MANAGEMENT',2);