--liquibase formatted sql
--changeset core:update_db_lutece_core-7.0.6-7.0.7.sql
--preconditions onFail:MARK_RAN onError:WARN
ALTER TABLE core_file ADD COLUMN origin VARCHAR(255) DEFAULT NULL;
