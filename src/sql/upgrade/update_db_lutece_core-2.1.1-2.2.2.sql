--liquibase formatted sql
--changeset core:update_db_lutece_core-2.1.1-2.2.2.sql
--preconditions onFail:MARK_RAN onError:WARN
ALTER TABLE `core_admin_right` ADD COLUMN `documentation_url` varchar(255) collate utf8_unicode_ci DEFAULT NULL;
ALTER TABLE `core_admin_right` ADD COLUMN `id_order` int(11) DEFAULT NULL;
ALTER TABLE `core_admin_user` CHANGE COLUMN `password` `password` varchar(100);
ALTER TABLE `core_connections_log` CHANGE COLUMN `access_code` `access_code` varchar(100);
ALTER TABLE `core_mail_queue` ADD COLUMN `is_locked` smallint;