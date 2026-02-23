--liquibase formatted sql
--changeset core:update_db_lutece_core-8.0.0-8.0.1.sql
--preconditions onFail:MARK_RAN onError:WARN

DELETE FROM core_datastore WHERE entity_key = 'portal.theme.site_property.menu.logo.alt';
INSERT INTO core_datastore VALUES ('portal.theme.site_property.menu.logo.alt', '');