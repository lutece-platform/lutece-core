ALTER TABLE core_page ADD COLUMN id_authorization_node int default NULL;
ALTER TABLE core_page DROP COLUMN workgroup_key;