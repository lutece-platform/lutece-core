--liquibase formatted sql
--changeset core:update_db_lutece_core-7.1.x-8.0.0.sql
--preconditions onFail:MARK_RAN onError:WARN
ALTER TABLE core_level_right ALTER COLUMN id_level DROP DEFAULT;
DROP TABLE IF EXISTS core_mode;
