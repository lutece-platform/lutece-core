--liquibase formatted sql
--changeset core:update_db_lutece_core-2.0.0-2.1.0.sql
--preconditions onFail:MARK_RAN onError:WARN
ALTER TABLE core_mode change output_xsl_omit_xml_declaration output_xsl_omit_xml_dec varchar(50) character SET utf8 collate utf8_unicode_ci NULL;