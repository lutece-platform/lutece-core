-- liquibase formatted sql
-- changeset core:update_db_lutece_core-6.0.1-6.1.0.sql
-- preconditions onFail:MARK_RAN onError:WARN
ALTER TABLE core_page add COLUMN display_date_update smallint default 0 NOT NULL;
ALTER TABLE core_page add COLUMN is_manual_date_update smallint default 0 NOT NULL;
ALTER TABLE core_mail_queue modify COLUMN id_mail_queue int AUTO_INCREMENT;
