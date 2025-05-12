--liquibase formatted sql
--changeset core:update_db_lutece_core-2.1.0-2.1.1.sql
--preconditions onFail:MARK_RAN onError:WARN
ALTER TABLE core_admin_right ADD COLUMN documentation_url varchar(255) DEFAULT NULL;
ALTER TABLE core_admin_right ADD COLUMN id_order int(11) DEFAULT NULL;