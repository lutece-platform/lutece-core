--
-- Table structure for table core_mail_queue
--
DROP TABLE IF EXISTS core_mail_queue;
CREATE TABLE core_mail_queue (
	id_mail_queue int default 0 NOT NULL,
	is_locked smallint default 0,
	PRIMARY KEY (id_mail_queue)
);

CREATE INDEX is_locked_core_mail_queue ON core_mail_queue (is_locked);

--
-- Table structure for table core_mail_item
--
DROP TABLE IF EXISTS core_mail_item;
CREATE TABLE core_mail_item (
	id_mail_queue int default 0 NOT NULL,
	mail_item long varbinary,
	PRIMARY KEY (id_mail_queue)
);

DELETE FROM core_user_right WHERE id_right = 'CORE_GROUPS_MANAGEMENT';