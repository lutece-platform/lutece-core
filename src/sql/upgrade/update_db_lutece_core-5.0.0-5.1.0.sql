-- liquibase formatted sql
-- changeset core:update_db_lutece_core-5.0.0-5.1.0.sql
-- preconditions onFail:MARK_RAN onError:WARN
-- LUTECE 
DELETE FROM core_datastore WHERE entity_key='portal.site.site_property.avatar_default';
INSERT INTO core_datastore VALUES ('portal.site.site_property.avatar_default', 'images/admin/skin/unknown.png');
DELETE FROM core_datastore WHERE entity_key='portal.site.site_property.back_images';
INSERT INTO core_datastore VALUES ('portal.site.site_property.back_images', '\'images/admin/skin/bg_login1.jpg\' , \'images/admin/skin/bg_login2.jpg\' , \'images/admin/skin/bg_login3.jpg\' , \'images/admin/skin/bg_login4.jpg\'');
