-- liquibase formatted sql
-- changeset core:upgrade_db_lutece_core-7.1.9-7.1.10.sql
-- preconditions onFail:MARK_RAN onError:WARN
-- Fix i18n keys of CORE_LEVEL_MANAGEMENT right (keys are defined in users_messages.properties)
UPDATE core_admin_right
SET name = 'portal.users.adminFeature.level_management.name',
    description = 'portal.users.adminFeature.level_management.description'
WHERE id_right = 'CORE_LEVEL_MANAGEMENT';
