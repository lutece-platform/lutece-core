-- liquibase formatted sql
-- changeset core:update_db_lutece_core-8.0.1-8.0.2.sql
-- preconditions onFail:MARK_RAN onError:WARN
-- LUT-32220 : Restrict XSL Export management to admin level 0 (system admin only)
UPDATE core_admin_right SET level_right = 0 WHERE id_right = 'CORE_XSL_EXPORT_MANAGEMENT';
