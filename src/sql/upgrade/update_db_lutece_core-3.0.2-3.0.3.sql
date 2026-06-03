-- liquibase formatted sql
-- changeset core:update_db_lutece_core-3.0.2-3.0.3.sql
-- preconditions onFail:MARK_RAN onError:WARN
ALTER TABLE core_page ADD COLUMN id_authorization_node int default NULL;
ALTER TABLE core_page DROP COLUMN workgroup_key;
UPDATE core_page SET id_authorization_node=1; 