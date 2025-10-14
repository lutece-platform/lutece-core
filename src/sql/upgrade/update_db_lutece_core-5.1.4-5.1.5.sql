-- liquibase formatted sql
-- changeset core:update_db_lutece_core-5.1.4-5.1.5.sql
-- preconditions onFail:MARK_RAN onError:WARN
CREATE INDEX core_admin_user_field_idx_file on core_admin_user_field (id_file);
