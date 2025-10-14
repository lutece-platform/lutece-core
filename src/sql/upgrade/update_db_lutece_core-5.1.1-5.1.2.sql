-- liquibase formatted sql
-- changeset core:update_db_lutece_core-5.1.1-5.1.2.sql
-- preconditions onFail:MARK_RAN onError:WARN
ALTER TABLE core_admin_right MODIFY COLUMN is_external_feature SMALLINT default 0;
