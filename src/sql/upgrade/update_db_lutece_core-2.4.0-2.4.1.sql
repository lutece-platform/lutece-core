---
--- Table structure for table core_attribute
---
DROP TABLE IF EXISTS core_attribute;
CREATE TABLE core_attribute (
	id_attribute INT DEFAULT 0 NOT NULL,
	type_class_name VARCHAR(255) DEFAULT NULL,
	title LONG VARCHAR DEFAULT NULL,
	help_message LONG VARCHAR DEFAULT NULL,
	is_mandatory SMALLINT DEFAULT 0,
	is_show_in_search SMALLINT DEFAULT 0,
	attribute_position INT DEFAULT 0,
	plugin_name VARCHAR(255) DEFAULT NULL,
	PRIMARY KEY (id_attribute)
);

---
--- Table structure for table core_attribute_field
---
DROP TABLE IF EXISTS core_attribute_field;
CREATE TABLE core_attribute_field (
	id_field INT DEFAULT 0 NOT NULL,
	id_attribute INT DEFAULT NULL,
	title VARCHAR(255) DEFAULT NULL,
	DEFAULT_value LONG VARCHAR DEFAULT NULL,
	is_DEFAULT_value SMALLINT DEFAULT 0,
	height INT DEFAULT NULL,
	width INT DEFAULT NULL,
	max_size_enter INT DEFAULT NULL,
	is_multiple SMALLINT DEFAULT 0,
	field_position INT DEFAULT NULL,
	PRIMARY KEY (id_field)
);

---
--- Table structure for table core_admin_user_field
---
DROP TABLE IF EXISTS core_admin_user_field;
CREATE TABLE core_admin_user_field (
	id_user_field INT DEFAULT 0 NOT NULL,
	id_user INT DEFAULT NULL,
	id_attribute INT DEFAULT NULL,
	id_field INT DEFAULT NULL,
	user_field_value LONG VARCHAR DEFAULT NULL,
	PRIMARY KEY (id_user_field)
);
