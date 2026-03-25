-- liquibase formatted sql
-- changeset core:upgrade_db_lutece_core-7.1.8-7.1.9.sql
-- preconditions onFail:MARK_RAN onError:WARN
-- LUT-32202 : Restrict XSL Export management to admin level 0 (system admin only)
UPDATE core_admin_right SET level_right = 0 WHERE id_right = 'CORE_XSL_EXPORT_MANAGEMENT';
