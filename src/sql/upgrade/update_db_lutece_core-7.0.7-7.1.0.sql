ALTER TABLE core_feature_group ADD COLUMN feature_group_icon VARCHAR(255) DEFAULT NULL;

INSERT INTO `core_datastore` (`entity_key`, `entity_value`) VALUES ('portal.site.site_property.layout.class', 'sidebar');
INSERT INTO `core_datastore` (`entity_key`, `entity_value`) VALUES ('portal.site.site_property.layout.code', 'header');
