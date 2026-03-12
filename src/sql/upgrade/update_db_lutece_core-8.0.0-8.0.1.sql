-- liquibase formatted sql
-- changeset core:update_db_lutece_core-8.0.0-8.0.1.sql
-- preconditions onFail:MARK_RAN onError:WARN

DELETE FROM core_datastore WHERE entity_key = 'portal.theme.site_property.menu.logo.alt';
INSERT INTO core_datastore VALUES ('portal.theme.site_property.menu.logo.alt', '');

-- changeset core:update_db_lutece_core-8.0.0-8.0.1-rev1.sql
DELETE FROM core_datastore WHERE entity_key = 'portal.theme.site_property.xss.xssMsg';
INSERT INTO core_datastore VALUES ('portal.theme.site_property.xss.xssMsg', 'Les caract\\u00e8res &#60; &#62; &#35; et &#34;  &amp; sont interdits dans le contenu de votre message.');

-- changeset core:update_db_lutece_core-8.0.0-8.0.1-rev2.sql
DELETE FROM core_datastore WHERE entity_key = 'portal.site.site_property.layout.fluid.checkbox';
INSERT INTO core_datastore VALUES ('portal.site.site_property.layout.fluid.checkbox', '1');

-- changeset core:update_db_lutece_core-8.0.0-8.0.1-rev3.sql
-- preconditions onFail:MARK_RAN onError:WARN
-- precondition-sql-check expectedResult:0 SELECT COUNT(1) from INFORMATION_SCHEMA.TABLES WHERE TABLE_NAME='core_admin_security_header_config_item';
CREATE TABLE core_admin_security_header_config_item (
  id_config_item SMALLINT AUTO_INCREMENT NOT NULL,
  id_security_header SMALLINT NOT NULL,
  header_custom_value VARCHAR(1024) NULL,
  url_pattern VARCHAR(1024) NOT NULL,
  PRIMARY KEY  (id_config_item)
);