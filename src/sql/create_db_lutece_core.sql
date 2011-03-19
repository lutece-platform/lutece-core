--
-- Table structure for table core_admin_dashboard
--
DROP TABLE IF EXISTS core_admin_dashboard;
CREATE TABLE core_admin_dashboard (
	dashboard_name varchar(100) NOT NULL,
	dashboard_column int NOT NULL,
	dashboard_order int NOT NULL,
	PRIMARY KEY (dashboard_name)
);

--
-- Table structure for table core_admin_mailinglist
--
DROP TABLE IF EXISTS core_admin_mailinglist;
CREATE TABLE core_admin_mailinglist (
	id_mailinglist int default 0 NOT NULL,
	name varchar(100) NOT NULL,
	description varchar(255) NOT NULL,
	workgroup varchar(50) NOT NULL,
	PRIMARY KEY (id_mailinglist)
);

--
-- Table structure for table core_admin_mailinglist_filter
--
DROP TABLE IF EXISTS core_admin_mailinglist_filter;
CREATE TABLE core_admin_mailinglist_filter (
	id_mailinglist int default 0 NOT NULL,
	workgroup varchar(50) NOT NULL,
	role varchar(50) NOT NULL,
	PRIMARY KEY (id_mailinglist,workgroup,role)
);

--
-- Table structure for table core_admin_right
--
DROP TABLE IF EXISTS core_admin_right;
CREATE TABLE core_admin_right (
	id_right varchar(255) default '' NOT NULL,
	name varchar(255) default NULL,
	level_right smallint default NULL,
	admin_url varchar(255) default NULL,
	description varchar(255) default NULL,
	is_updatable int default 0 NOT NULL,
	plugin_name varchar(50) default NULL,
	id_feature_group varchar(50) default NULL,
	icon_url varchar(255) default NULL,
	documentation_url varchar(255) default NULL,
	id_order int default NULL,
	PRIMARY KEY (id_right)
);

CREATE INDEX index_right ON core_admin_right (level_right,admin_url);

--
-- Table structure for table core_admin_role
--
DROP TABLE IF EXISTS core_admin_role;
CREATE TABLE core_admin_role (
	role_key varchar(50) default '' NOT NULL,
	role_description varchar(100) default '' NOT NULL,
	PRIMARY KEY (role_key)
);

--
-- Table structure for table core_admin_role_resource
--
DROP TABLE IF EXISTS core_admin_role_resource;
CREATE TABLE core_admin_role_resource (
	rbac_id int default 0 NOT NULL,
	role_key varchar(50) default '' NOT NULL,
	resource_type varchar(50) default '' NOT NULL,
	resource_id varchar(50) default '' NOT NULL,
	permission varchar(50) default '' NOT NULL,
	PRIMARY KEY (rbac_id)
);

--
-- Table structure for table core_admin_user
--
DROP TABLE IF EXISTS core_admin_user;
CREATE TABLE core_admin_user (
	id_user int default 0 NOT NULL,
	access_code varchar(100) default '' NOT NULL,
	last_name varchar(100) default '' NOT NULL,
	first_name varchar(100) default '' NOT NULL,
	email varchar(100) default '0' NOT NULL,
	status smallint default 0 NOT NULL,
	password varchar(100) default NULL,
	locale varchar(10) default 'fr' NOT NULL,
	level_user smallint default 0 NOT NULL,
	reset_password smallint default 0 NOT NULL,
	PRIMARY KEY (id_user)
);


--
-- Table structure for table core_admin_workgroup 
--
DROP TABLE IF EXISTS core_admin_workgroup;
CREATE TABLE core_admin_workgroup (
	workgroup_key varchar(50) default NULL,
	workgroup_description varchar(255) default NULL,
	PRIMARY KEY (workgroup_key)
);

--
-- Table structure for table core_admin_workgroup_user 
--
DROP TABLE IF EXISTS core_admin_workgroup_user;
CREATE TABLE core_admin_workgroup_user (
	workgroup_key varchar(50) default NULL,
	id_user int default NULL,
	PRIMARY KEY (workgroup_key,id_user)
);

--
-- Table structure for table core_connections_log
--
DROP TABLE IF EXISTS core_connections_log;
CREATE TABLE core_connections_log (
	access_code varchar(100) default NULL,
	ip_address varchar(63) default NULL,
	date_login timestamp default CURRENT_TIMESTAMP NOT NULL,
	login_status int default NULL
);

CREATE INDEX index_connections_log ON core_connections_log (ip_address,date_login);

--
-- Table structure for table core_admin_dashboard
--
DROP TABLE IF EXISTS core_dashboard;
CREATE TABLE core_dashboard (
	dashboard_name varchar(100) NOT NULL,
	dashboard_column int NOT NULL,
	dashboard_order int NOT NULL,
	PRIMARY KEY (dashboard_name)
);

--
-- Table structure for table core_feature_group
--
DROP TABLE IF EXISTS core_feature_group;
CREATE TABLE core_feature_group (
	id_feature_group varchar(50) default '' NOT NULL,
	feature_group_description varchar(255) default NULL,
	feature_group_label varchar(100) default NULL,
	feature_group_order int default NULL,
	PRIMARY KEY (id_feature_group)
);

--
-- Table structure for table core_file
--
DROP TABLE IF EXISTS core_file;
CREATE TABLE core_file (
	id_file INT DEFAULT 0 NOT NULL,
	title LONG VARCHAR DEFAULT NULL, 
	id_physical_file INT DEFAULT NULL,  
	file_size  INT DEFAULT NULL,
	mime_type VARCHAR(255) DEFAULT NULL,
	PRIMARY KEY (id_file)
);

--
-- Table structure for table core_indexer_action
--
DROP TABLE IF EXISTS core_indexer_action;
CREATE TABLE  core_indexer_action (
  id_action int default 0 NOT NULL,
  id_document varchar(255) NOT NULL,
  id_task int default 0 NOT NULL,
  indexer_name varchar(255) NOT NULL,
  id_portlet int default 0 NOT NULL,
  PRIMARY KEY (id_action)
);


--
-- Table structure for table core_level_right
--
DROP TABLE IF EXISTS core_level_right;
CREATE TABLE core_level_right (
	id_level smallint default 0 NOT NULL,
	name varchar(80) default NULL,
	PRIMARY KEY (id_level)
);


--
-- Table structure for table core_mail_queue
--
DROP TABLE IF EXISTS core_mail_queue;
CREATE TABLE core_mail_queue (
	id_mail_queue int default 0 NOT NULL,
	mail_item long varbinary,
	is_locked smallint default 0,
	PRIMARY KEY (id_mail_queue)
);

--
-- Table structure for table core_mode
--
DROP TABLE IF EXISTS core_mode;
CREATE TABLE core_mode (
	id_mode int default 0 NOT NULL,
	description_mode varchar(255),
	path varchar(50) default '' NOT NULL,
	output_xsl_method varchar(50) default NULL,
	output_xsl_version varchar(50) default NULL,
	output_xsl_media_type varchar(50) default NULL,
	output_xsl_encoding varchar(50) default NULL,
	output_xsl_indent varchar(50) default NULL,
	output_xsl_omit_xml_dec varchar(50) default NULL,
	output_xsl_standalone varchar(50) default NULL,
	PRIMARY KEY (id_mode)
);

--
-- Table structure for table core_page
--
DROP TABLE IF EXISTS core_page;
CREATE TABLE core_page (
	id_page int default 0 NOT NULL,
	id_parent int default 0,
	name varchar(50) default '' NOT NULL,
	description long varchar,
	date_update timestamp default CURRENT_TIMESTAMP NOT NULL,
	status smallint default NULL,
	page_order int default 0,
	id_template int default NULL,
	date_creation timestamp default '0000-00-00 00:00:00' NOT NULL,
	role varchar(50) default NULL,
	code_theme varchar(80) default NULL,
	node_status smallint default 1 NOT NULL,
	image_content long varbinary,
	mime_type varchar(255) default 'NULL',
	workgroup_key varchar(50) default NULL,
	meta_keywords varchar(255) default NULL,
	meta_description varchar(255) default NULL,
	PRIMARY KEY (id_page)
);

CREATE INDEX index_page ON core_page (id_template,id_parent);
CREATE INDEX index_childpage ON core_page (id_parent,page_order);

--
-- Table structure for table core_page_template
--
DROP TABLE IF EXISTS core_page_template;
CREATE TABLE core_page_template (
	id_template int default 0 NOT NULL,
	description varchar(50) default NULL,
	file_name varchar(100) default NULL,
	picture varchar(50) default NULL,
	PRIMARY KEY (id_template)
);

--
-- Table structure for table core_physical_file
--
DROP TABLE IF EXISTS core_physical_file;
CREATE TABLE core_physical_file (
	id_physical_file INT DEFAULT 0 NOT NULL,
	file_value LONG VARBINARY,  
	PRIMARY KEY (id_physical_file)
);

--
-- Table structure for table core_portal_component
--
DROP TABLE IF EXISTS core_portal_component;
CREATE TABLE core_portal_component (
	id_portal_component int default 0 NOT NULL,
	name varchar(50) default NULL,
	PRIMARY KEY (id_portal_component)
);

--
-- Table structure for table core_portlet
--
DROP TABLE IF EXISTS core_portlet;
CREATE TABLE core_portlet (
	id_portlet int default 0 NOT NULL,
	id_portlet_type varchar(50) default NULL,
	id_page int default NULL,
	name varchar(40) default NULL,
	date_update timestamp default CURRENT_TIMESTAMP NOT NULL,
	status smallint default 0 NOT NULL,
	portlet_order int default NULL,
	column_no int default NULL,
	id_style int default NULL,
	accept_alias smallint default NULL,
	date_creation timestamp default '0000-00-00 00:00:00' NOT NULL,
	display_portlet_title int default 0 NOT NULL,
	role varchar(50) default NULL,
	PRIMARY KEY (id_portlet)
);

CREATE INDEX index_portlet ON core_portlet (id_page,id_portlet_type,id_style);

--
-- Table structure for table core_portlet_alias
--
DROP TABLE IF EXISTS core_portlet_alias;
CREATE TABLE core_portlet_alias (
	id_portlet int default 0 NOT NULL,
	id_alias int default 0 NOT NULL,
	PRIMARY KEY (id_portlet,id_alias)
);

--
-- Table structure for table core_portlet_type
--
DROP TABLE IF EXISTS core_portlet_type;
CREATE TABLE core_portlet_type (
	id_portlet_type varchar(50) default '0' NOT NULL,
	name varchar(255) default NULL,
	url_creation varchar(255) default NULL,
	url_update varchar(255) default NULL,
	home_class varchar(255) default NULL,
	plugin_name varchar(50) default NULL,
	url_docreate varchar(255) default NULL,
	create_script varchar(255) default NULL,
	create_specific varchar(255) default NULL,
	create_specific_form varchar(255) default NULL,
	url_domodify varchar(255) default NULL,
	modify_script varchar(255) default NULL,
	modify_specific varchar(255) default NULL,
	modify_specific_form varchar(255) default NULL,
	PRIMARY KEY (id_portlet_type)
);

--
-- Table structure for table core_role
--
DROP TABLE IF EXISTS core_role;
CREATE TABLE core_role (
	role varchar(50) default '' NOT NULL,
	role_description varchar(255) default NULL,
	workgroup_key varchar(50) DEFAULT '' NOT NULL,
	PRIMARY KEY (role)
);

--
-- Table structure for table core_style
--
DROP TABLE IF EXISTS core_style;
CREATE TABLE core_style (
	id_style int default 0 NOT NULL,
	description_style varchar(100) default '' NOT NULL,
	id_portlet_type varchar(50) default NULL,
	id_portal_component int default 0 NOT NULL,
	PRIMARY KEY (id_style)
);

CREATE INDEX index_style ON core_style (id_portlet_type);

--
-- Table structure for table core_style_mode_stylesheet
--
DROP TABLE IF EXISTS core_style_mode_stylesheet;
CREATE TABLE core_style_mode_stylesheet (
	id_style int default 0 NOT NULL,
	id_mode int default 0 NOT NULL,
	id_stylesheet int default 0 NOT NULL,
	PRIMARY KEY (id_style,id_mode,id_stylesheet)
);

CREATE INDEX index_style_mode_stylesheet ON core_style_mode_stylesheet (id_stylesheet,id_mode);

--
-- Table structure for table core_stylesheet
--
DROP TABLE IF EXISTS core_stylesheet;
CREATE TABLE core_stylesheet (
	id_stylesheet int default 0 NOT NULL,
	description varchar(255),
	file_name varchar(255),
	source long varbinary,
	PRIMARY KEY (id_stylesheet)
);

--
-- Table structure for table core_user_right
--
DROP TABLE IF EXISTS core_user_right;
CREATE TABLE core_user_right (
	id_right varchar(255) default '' NOT NULL,
	id_user int default 0 NOT NULL,
	PRIMARY KEY (id_right,id_user)
);

CREATE INDEX index_user_right ON core_user_right (id_user);

--
-- Table structure for table core_user_role
--
DROP TABLE IF EXISTS core_user_role;
CREATE TABLE core_user_role (
	role_key varchar(50) default '' NOT NULL,
	id_user int default 0 NOT NULL,
	PRIMARY KEY (role_key,id_user)
);

--
--
--
DROP TABLE IF EXISTS core_id_generator;
CREATE TABLE core_id_generator (
	class_name varchar(250) default '' NOT NULL,
	current_value int default 0 NOT NULL,
	PRIMARY KEY (class_name)
);

--
-- Table structure for table core_user_parameters
--
DROP TABLE IF EXISTS core_user_parameter;
CREATE TABLE core_user_parameter (
	parameter_key varchar(100) NOT NULL,
	parameter_value varchar(100) NOT NULL,
	PRIMARY KEY (parameter_key)
);

--
-- Table structure for table core_attribute
--
DROP TABLE IF EXISTS core_attribute;
CREATE TABLE core_attribute (
	id_attribute INT DEFAULT 0 NOT NULL,
	type_class_name VARCHAR(255) DEFAULT NULL,
	title LONG VARCHAR DEFAULT NULL,
	help_message LONG VARCHAR DEFAULT NULL,
	is_mandatory SMALLINT DEFAULT 0,
	is_shown_in_search SMALLINT DEFAULT 0,
	is_shown_in_result_list SMALLINT DEFAULT 0,
	is_field_in_line SMALLINT DEFAULT 0,
	attribute_position INT DEFAULT 0,
	plugin_name VARCHAR(255) DEFAULT NULL,
	PRIMARY KEY (id_attribute)
);

--
-- Table structure for table core_attribute_field
--
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

--
-- Table structure for table core_admin_user_field
--
DROP TABLE IF EXISTS core_admin_user_field;
CREATE TABLE core_admin_user_field (
	id_user_field INT DEFAULT 0 NOT NULL,
	id_user INT DEFAULT NULL,
	id_attribute INT DEFAULT NULL,
	id_field INT DEFAULT NULL,
	id_file INT DEFAULT NULL,
	user_field_value LONG VARCHAR DEFAULT NULL,
	PRIMARY KEY (id_user_field)
);
