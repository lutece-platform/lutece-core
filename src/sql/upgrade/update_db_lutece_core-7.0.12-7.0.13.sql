--liquibase formatted sql
--changeset core:update_db_lutece_core-7.0.12-7.0.13.sql
--preconditions onFail:MARK_RAN onError:WARN
INSERT INTO core_admin_right VALUES ('CORE_LEVEL_MANAGEMENT', 'portal.level.adminFeature.level_management.name', 0, NULL, 'portal.level.adminFeature.level_management.description', 1, '', 'SYSTEM', 'ti ti-gavel', NULL, 11, 0);

INSERT INTO core_user_right VALUES ('CORE_LEVEL_MANAGEMENT',1);