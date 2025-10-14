-- liquibase formatted sql
-- changeset core:update_db_lutece_core-2.9.9-3.0.0.sql
-- preconditions onFail:MARK_RAN onError:WARN
--
-- Dumping data into table core_user_parameter
--
INSERT INTO core_user_parameter VALUES ('email_pattern', '^[\\w_.\\-]+@[\\w_.\\-]+\\.[\\w]+$');
INSERT INTO core_user_parameter VALUES ('email_pattern_verify_by', '');
