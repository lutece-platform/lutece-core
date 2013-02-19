ALTER TABLE core_portlet CHANGE COLUMN name name VARCHAR(70) NULL DEFAULT NULL COLLATE 'utf8_unicode_ci' AFTER id_page;
ALTER TABLE core_xsl_export ADD COLUMN plugin VARCHAR(255) DEFAULT '';
