-- liquibase formatted sql
-- changeset core:update_db_lutece_core-2.2.6-2.2.7.sql
-- preconditions onFail:MARK_RAN onError:WARN
INSERT INTO core_admin_right VALUES ('CORE_THEMES_MANAGEMENT','portal.style.adminFeature.themes_management.name',0,'jsp/admin/style/ManageThemes.jsp','portal.style.adminFeature.themes_management.description',1,NULL,'STYLE','images/admin/skin/features/manage_styles.png',NULL,4);
