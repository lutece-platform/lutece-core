--
-- Switch tinymce to tinymce6
--
INSERT INTO core_text_editor VALUES ('tinymce6', 'portal.admindashboard.editors.labelBackTinyMCE6', 1);
UPDATE core_datastore SET entity_value='tinymce6' where entity_key ='core.backOffice.defaultEditor' and entity_value = 'tinymce';
DELETE FROM core_text_editor WHERE editor_name='tinymce';
UPDATE core_datastore SET entity_key='portal.site.site_property.layout.login.image' WHERE entity_key='portal.site.site_property.login.image';
DELETE FROM core_datastore WHERE entity_key='portal.site.site_property.login.image';

--
-- Remove log page
--
DELETE FROM core_admin_right WHERE id_right = 'CORE_LOGS_VISUALISATION';
DELETE FROM core_user_right WHERE id_right = 'CORE_LOGS_VISUALISATION';
