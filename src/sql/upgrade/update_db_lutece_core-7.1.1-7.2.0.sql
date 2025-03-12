--
-- Switch tinymce to Tui Editor Markdown
--
INSERT INTO core_text_editor VALUES ('tuieditor3', 'portal.admindashboard.editors.labelBackTuiEditor3', 1);
UPDATE core_datastore SET entity_value='tuieditor3' where entity_key ='core.backOffice.defaultEditor' and entity_value = 'tinymce6';
