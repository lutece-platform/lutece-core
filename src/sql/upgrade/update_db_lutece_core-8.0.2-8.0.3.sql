-- liquibase formatted sql
-- changeset core:update_db_lutece_core-8.0.2-8.0.3.sql
-- preconditions onFail:MARK_RAN onError:WARN
-- Session timeout accessibility warning (WCAG 2.2.1 / 2.2.6)
INSERT INTO core_datastore VALUES ('portal.theme.site_property.sessiontimeout.enabled.checkbox', '1');
INSERT INTO core_datastore VALUES ('portal.theme.site_property.sessiontimeout.duration', '1800');
INSERT INTO core_datastore VALUES ('portal.theme.site_property.sessiontimeout.warningDelay', '120');
INSERT INTO core_datastore VALUES ('portal.theme.site_property.sessiontimeout.keepAliveUrl', '');
INSERT INTO core_datastore VALUES ('portal.theme.site_property.sessiontimeout.maxExtensions', '10');
INSERT INTO core_datastore VALUES ('portal.theme.site_property.sessiontimeout.position', 'top-0 end-0');
