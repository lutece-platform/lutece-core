-- liquibase formatted sql
-- changeset core:upgrade_db_lutece_core-7.1.1-7.1.8.sql
-- preconditions onFail:MARK_RAN onError:WARN
DROP TABLE IF EXISTS core_admin_security_header_config_item;
CREATE TABLE core_admin_security_header_config_item (
  id_config_item SMALLINT AUTO_INCREMENT NOT NULL,
  id_security_header SMALLINT NOT NULL,
  header_custom_value VARCHAR(1024) NULL,
  url_pattern VARCHAR(1024) NOT NULL,
  PRIMARY KEY  (id_config_item)
);