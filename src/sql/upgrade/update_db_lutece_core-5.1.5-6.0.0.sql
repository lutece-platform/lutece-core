ALTER TABLE core_admin_user MODIFY COLUMN password LONG VARCHAR default NULL;
ALTER TABLE core_user_password_history MODIFY COLUMN password LONG VARCHAR NOT NULL;

-- update password storage
-- for PostgreSQL, replace 'REGEXP' by '~*' and 'NOT REGEXP' by '!~*'
UPDATE core_admin_user SET password =
CONCAT(COALESCE(
	(SELECT CONCAT(cd1.entity_value,':') FROM core_datastore cd1 CROSS JOIN core_datastore cd2	WHERE cd1.entity_key = 'core.advanced_parameters.encryption_algorithm' AND cd2.entity_key = 'core.advanced_parameters.enable_password_encryption' AND LOWER(cd2.entity_value) = 'true'),
	'PLAINTEXT:')
,password) WHERE password NOT REGEXP '^(MD5:[0-9a-f]{32}|SHA-1:[0-9a-f]{40}|SHA-256:[0-9a-f]{64}|PLAINTEXT:.*|PBKDF2:[0-9]+:[0-9a-f]{32}:[0-9a-f]{128}[0-9a-f]{128}|PBKDF2WITHHMACSHA512:[0-9]+:[0-9a-f]{32}:[0-9a-f]{128}[0-9a-f]{128})$';

-- updating password history with best effort to guess format
-- for PostgreSQL, replace 'REGEXP' by '~*' and 'NOT REGEXP' by '!~*'
UPDATE core_user_password_history SET password = CONCAT('MD5:',password) WHERE password REGEXP '^[0-9a-f]{32}$';
UPDATE core_user_password_history SET password = CONCAT('SHA-1:',password) WHERE password REGEXP '^[0-9a-f]{40}$';
UPDATE core_user_password_history SET password = CONCAT('SHA-256:',password) WHERE password REGEXP '^[0-9a-f]{64}$';
UPDATE core_user_password_history SET password = CONCAT('PLAINTEXT:',password) WHERE password NOT REGEXP '^(MD5:[0-9a-f]{32}|SHA-1:[0-9a-f]{40}|SHA-256:[0-9a-f]{64}|PLAINTEXT:.*|PBKDF2:[0-9]+:[0-9a-f]{32}:[0-9a-f]{128}[0-9a-f]{128}|PBKDF2WITHHMACSHA512:[0-9]+:[0-9a-f]{32}:[0-9a-f]{128}[0-9a-f]{128})$';

DELETE FROM core_datastore WHERE entity_key = 'core.advanced_parameters.encryption_algorithm';
DELETE FROM core_datastore WHERE entity_key = 'core.advanced_parameters.enable_password_encryption';

DELETE FROM core_admin_role_resource WHERE resource_type = 'ADMIN_USER' AND permission = 'MANAGE_ENCRYPTED_PASSWORD';

INSERT INTO core_datastore VALUES ('core.advanced_parameters.reset_token_validity', '60');
INSERT INTO core_datastore VALUES ('core.advanced_parameters.lock_reset_token_to_session', 'false');
