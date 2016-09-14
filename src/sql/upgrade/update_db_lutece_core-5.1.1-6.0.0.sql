ALTER TABLE core_admin_user MODIFY COLUMN password LONG VARCHAR default NULL;
ALTER TABLE core_user_password_history MODIFY COLUMN password LONG VARCHAR NOT NULL;

-- update password storage
UPDATE core_admin_user SET password =
CONCAT(COALESCE(
	(SELECT CONCAT(cd1.entity_value,':') FROM core_datastore cd1 CROSS JOIN core_datastore cd2	WHERE cd1.entity_key = 'core.advanced_parameters.encryption_algorithm' AND cd2.entity_key = 'core.advanced_parameters.enable_password_encryption' AND LOWER(cd2.entity_value) = 'true'),
	'PLAINTEXT:')
,password);

-- updating password history with best effort to guess format
-- for PostgreSQL, replace 'REGEXP' by '~*' and 'NOT REGEXP' by '!~*'
UPDATE core_user_password_history SET password = CONCAT('MD5:',password) WHERE password REGEXP '^[0-9a-f]{32}$';
UPDATE core_user_password_history SET password = CONCAT('SHA-1:',password) WHERE password REGEXP '^[0-9a-f]{40}$';
UPDATE core_user_password_history SET password = CONCAT('SHA-256:',password) WHERE password REGEXP '^[0-9a-f]{64}$';
UPDATE core_user_password_history SET password = CONCAT('PLAINTEXT:',password) WHERE password NOT REGEXP '^(MD5:[0-9a-f]{32}|SHA-1:[0-9a-f]{40}|SHA-256:[0-9a-f]{64})$';

DELETE FROM core_datastore WHERE entity_key = 'core.advanced_parameters.encryption_algorithm';
DELETE FROM core_datastore WHERE entity_key = 'core.advanced_parameters.enable_password_encryption';

DELETE FROM core_admin_role_resource WHERE resource_type = 'ADMIN_USER' AND permission = 'MANAGE_ENCRYPTED_PASSWORD';

-- updating core_admin_right and core_user_right for external features management 

-- on core_admin_right 
ALTER TABLE core_admin_right ADD COLUMN is_external_feature BOOLEAN DEFAULT NULL;
UPDATE core_admin_right SET is_external_feature = false ;
INSERT INTO core_admin_right VALUES ('CORE_EXTERNAL_FEATURES_MANAGEMENT', 'portal.system.adminFeature.external_features_management.name', 1, 'jsp/admin/features/ManageExternalFeatures.jsp', 'portal.system.adminFeature.external_features_management.description', 1, NULL, 'SYSTEM', NULL, NULL, 11, false);

-- on core_user_right
INSERT INTO core_user_right VALUES ('CORE_EXTERNAL_FEATURES_MANAGEMENT',1);
INSERT INTO core_user_right VALUES ('CORE_EXTERNAL_FEATURES_MANAGEMENT',2);