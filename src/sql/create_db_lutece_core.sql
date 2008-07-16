-- MySQL dump 10.10
--
-- Host: localhost    Database: db_lutece
-- ------------------------------------------------------
-- Server version	5.0.24a-community-nt

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;


--
-- Table structure for table `core_admin_auth_db_module`
--

DROP TABLE IF EXISTS `core_admin_auth_db_module`;
CREATE TABLE `core_admin_auth_db_module` (
  `access_code` varchar(16) collate utf8_unicode_ci NOT NULL default '',
  `password` varchar(16) collate utf8_unicode_ci NOT NULL default '',
  `date_valid_password` date NOT NULL default '0000-00-00',
  `last_password` varchar(16) collate utf8_unicode_ci NOT NULL default '',
  `last_name` varchar(100) collate utf8_unicode_ci NOT NULL default '',
  `first_name` varchar(100) collate utf8_unicode_ci NOT NULL default '',
  `email` varchar(100) collate utf8_unicode_ci NOT NULL default '',
  PRIMARY KEY  (`access_code`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

--
-- Table structure for table `core_admin_mailinglist`
--

DROP TABLE IF EXISTS `core_admin_mailinglist`;
CREATE TABLE `core_admin_mailinglist` (
  `id_mailinglist` int(11) unsigned NOT NULL default '0',
  `name` varchar(100) NOT NULL,
  `description` varchar(255) NOT NULL,
  `workgroup` varchar(50) NOT NULL,
  PRIMARY KEY  (`id_mailinglist`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;


--
-- Table structure for table `core_admin_mailinglist_users_filter`
--

DROP TABLE IF EXISTS `core_admin_mailinglist_filter`;
CREATE TABLE `core_admin_mailinglist_filter` (
  `id_mailinglist` int(11) unsigned NOT NULL default '0',
  `workgroup` varchar(50) NOT NULL,
  `role` varchar(50) NOT NULL,
  PRIMARY KEY  (`id_mailinglist`,`workgroup`,`role`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;


--
-- Table structure for table `core_admin_right`
--

DROP TABLE IF EXISTS `core_admin_right`;
CREATE TABLE `core_admin_right` (
  `id_right` varchar(50) collate utf8_unicode_ci NOT NULL default '',
  `name` varchar(255) collate utf8_unicode_ci default NULL,
  `level_right` smallint(6) default NULL,
  `admin_url` varchar(255) collate utf8_unicode_ci default NULL,
  `description` varchar(255) collate utf8_unicode_ci default NULL,
  `is_updatable` int(11) NOT NULL default '0',
  `plugin_name` varchar(50) collate utf8_unicode_ci default NULL,
  `id_feature_group` varchar(50) collate utf8_unicode_ci default NULL,
  `icon_url` varchar(255) collate utf8_unicode_ci default NULL,
  `documentation_url` varchar(255) collate utf8_unicode_ci default NULL,
  `id_order` int(11) default NULL,
  PRIMARY KEY  (`id_right`),
  KEY `index_right` (`level_right`,`admin_url`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

--
-- Table structure for table `core_admin_role`
--

DROP TABLE IF EXISTS `core_admin_role`;
CREATE TABLE `core_admin_role` (
  `role_key` varchar(50) collate utf8_unicode_ci NOT NULL default '',
  `role_description` varchar(100) collate utf8_unicode_ci NOT NULL default '',
  PRIMARY KEY  (`role_key`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

--
-- Table structure for table `core_admin_role_resource`
--

DROP TABLE IF EXISTS `core_admin_role_resource`;
CREATE TABLE `core_admin_role_resource` (
  `rbac_id` int(11) unsigned NOT NULL default '0',
  `role_key` varchar(50) collate utf8_unicode_ci NOT NULL default '',
  `resource_type` varchar(50) collate utf8_unicode_ci NOT NULL default '',
  `resource_id` varchar(50) collate utf8_unicode_ci NOT NULL default '',
  `permission` varchar(50) collate utf8_unicode_ci NOT NULL default '',
  PRIMARY KEY  (`rbac_id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

--
-- Table structure for table `core_admin_user`
--

DROP TABLE IF EXISTS `core_admin_user`;
CREATE TABLE `core_admin_user` (
  `id_user` int(11) unsigned NOT NULL default '0',
  `access_code` varchar(100) collate utf8_unicode_ci NOT NULL default '',
  `last_name` varchar(100) collate utf8_unicode_ci NOT NULL default '',
  `first_name` varchar(100) collate utf8_unicode_ci NOT NULL default '',
  `email` varchar(100) collate utf8_unicode_ci NOT NULL default '0',
  `status` smallint(1) NOT NULL default '0',
  `password` varchar(10) collate utf8_unicode_ci default NULL,
  `locale` varchar(10) collate utf8_unicode_ci NOT NULL default 'fr',
  `level_user` smallint(1) unsigned NOT NULL default '0',
  PRIMARY KEY  (`id_user`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;


--
-- Table structure for table core_admin_workgroup 
--
CREATE TABLE core_admin_workgroup (
  workgroup_key varchar(50) default NULL,
  workgroup_description varchar(255) default NULL,
  PRIMARY KEY  (workgroup_key)
) ENGINE=MyISAM DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;


--
-- Table structure for table core_admin_workgroup_user 
--
CREATE TABLE core_admin_workgroup_user (
  workgroup_key varchar(50) default NULL,
  id_user int(11) default NULL,
  PRIMARY KEY  (workgroup_key,id_user)
) ENGINE=MyISAM DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;


--
-- Table structure for table `core_connections_log`
--

DROP TABLE IF EXISTS `core_connections_log`;
CREATE TABLE `core_connections_log` (
  `access_code` varchar(100) collate utf8_unicode_ci default NULL,
  `ip_address` varchar(16) collate utf8_unicode_ci default NULL,
  `date_login` timestamp NOT NULL default CURRENT_TIMESTAMP on update CURRENT_TIMESTAMP,
  `login_status` int(11) default NULL,
  KEY `index_connections_log` (`ip_address`,`date_login`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

--
-- Table structure for table `core_feature_group`
--

DROP TABLE IF EXISTS `core_feature_group`;
CREATE TABLE `core_feature_group` (
  `id_feature_group` varchar(50) collate utf8_unicode_ci NOT NULL default '',
  `feature_group_description` varchar(255) collate utf8_unicode_ci default NULL,
  `feature_group_label` varchar(100) collate utf8_unicode_ci default NULL,
  `feature_group_order` int(11) default NULL,
  PRIMARY KEY  (`id_feature_group`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

--
-- Table struture for table `core_group`
--

DROP TABLE IF EXISTS `core_group`;
CREATE TABLE `core_group` (
  `group_key` varchar(100) collate utf8_unicode_ci NOT NULL default '0',
  `group_description` varchar(200) collate utf8_unicode_ci default NULL,
  PRIMARY KEY  (`group_key`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

--
-- Table struture for table `core_group_role`
--

DROP TABLE IF EXISTS `core_group_role`;
CREATE TABLE core_group_role (
  `group_key` varchar(100) collate utf8_unicode_ci NOT NULL default '0',
  `role_key` varchar(50) collate utf8_unicode_ci NOT NULL default '0',
  PRIMARY KEY  (`group_key`,`role_key`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

--
-- Table structure for table `core_level_right`
--

DROP TABLE IF EXISTS `core_level_right`;
CREATE TABLE `core_level_right` (
  `id_level` smallint(6) NOT NULL default '0',
  `name` varchar(80) collate utf8_unicode_ci default NULL,
  PRIMARY KEY  (`id_level`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

--
-- Table structure for table `core_mode`
--

DROP TABLE IF EXISTS `core_mode`;
CREATE TABLE `core_mode` (
  `id_mode` int(11) NOT NULL default '0',
  `description_mode` tinytext collate utf8_unicode_ci,
  `path` varchar(50) collate utf8_unicode_ci NOT NULL default '',
  `output_xsl_method` varchar(50) collate utf8_unicode_ci default NULL,
  `output_xsl_version` varchar(50) collate utf8_unicode_ci default NULL,
  `output_xsl_media_type` varchar(50) collate utf8_unicode_ci default NULL,
  `output_xsl_encoding` varchar(50) collate utf8_unicode_ci default NULL,
  `output_xsl_indent` varchar(50) collate utf8_unicode_ci default NULL,
  `output_xsl_omit_xml_dec` varchar(50) collate utf8_unicode_ci default NULL,
  `output_xsl_standalone` varchar(50) collate utf8_unicode_ci default NULL,
  PRIMARY KEY  (`id_mode`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

--
-- Table structure for table `core_page`
--

DROP TABLE IF EXISTS `core_page`;
CREATE TABLE `core_page` (
  `id_page` int(11) NOT NULL default '0',
  `id_parent` int(11) default '0',
  `name` varchar(50) collate utf8_unicode_ci NOT NULL default '',
  `description` text collate utf8_unicode_ci,
  `date_update` timestamp NOT NULL default CURRENT_TIMESTAMP on update CURRENT_TIMESTAMP,
  `status` smallint(6) default NULL,
  `page_order` int(11) default '0',
  `id_template` int(11) default NULL,
  `date_creation` timestamp NOT NULL default '0000-00-00 00:00:00',
  `role` varchar(50) collate utf8_unicode_ci default NULL,
  `code_theme` varchar(80) collate utf8_unicode_ci default NULL,
  `node_status` smallint(6) NOT NULL default '1',
  `image_content` longblob,
  `mime_type` varchar(255) collate utf8_unicode_ci default 'NULL',
  PRIMARY KEY  (`id_page`),
  KEY `index_page` (`id_template`,`id_parent`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

--
-- Table structure for table `core_page_template`
--

DROP TABLE IF EXISTS `core_page_template`;
CREATE TABLE `core_page_template` (
  `id_template` int(11) NOT NULL default '0',
  `description` varchar(50) collate utf8_unicode_ci default NULL,
  `file_name` varchar(100) collate utf8_unicode_ci default NULL,
  `picture` varchar(50) collate utf8_unicode_ci default NULL,
  PRIMARY KEY  (`id_template`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

--
-- Table structure for table `core_parameters`
--

DROP TABLE IF EXISTS `core_parameters`;
CREATE TABLE `core_parameters` (
  `password_duration` int(11) NOT NULL default '0'
) ENGINE=MyISAM DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

--
-- Table structure for table `core_portal_component`
--

DROP TABLE IF EXISTS `core_portal_component`;
CREATE TABLE `core_portal_component` (
  `id_portal_component` int(11) NOT NULL default '0',
  `name` varchar(50) collate utf8_unicode_ci default NULL,
  PRIMARY KEY  (`id_portal_component`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

--
-- Table structure for table `core_portlet`
--

DROP TABLE IF EXISTS `core_portlet`;
CREATE TABLE `core_portlet` (
  `id_portlet` int(11) NOT NULL default '0',
  `id_portlet_type` varchar(50) collate utf8_unicode_ci default NULL,
  `id_page` int(11) default NULL,
  `name` varchar(40) collate utf8_unicode_ci default NULL,
  `date_update` timestamp NOT NULL default CURRENT_TIMESTAMP on update CURRENT_TIMESTAMP,
  `status` smallint(6) NOT NULL default '0',
  `portlet_order` int(11) default NULL,
  `column_no` int(11) default NULL,
  `id_style` int(11) default NULL,
  `accept_alias` smallint(1) default NULL,
  `date_creation` timestamp NOT NULL default '0000-00-00 00:00:00',
  `display_portlet_title` int(11) NOT NULL default '0',
  PRIMARY KEY  (`id_portlet`),
  KEY `index_portlet` (`id_page`,`id_portlet_type`,`id_style`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

--
-- Table structure for table `core_portlet_alias`
--

DROP TABLE IF EXISTS `core_portlet_alias`;
CREATE TABLE `core_portlet_alias` (
  `id_portlet` int(11) NOT NULL default '0',
  `id_alias` int(11) NOT NULL default '0',
  PRIMARY KEY  (`id_portlet`,`id_alias`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

--
-- Table structure for table `core_portlet_type`
--

DROP TABLE IF EXISTS `core_portlet_type`;
CREATE TABLE `core_portlet_type` (
  `id_portlet_type` varchar(50) collate utf8_unicode_ci NOT NULL default '0',
  `name` varchar(255) collate utf8_unicode_ci default NULL,
  `url_creation` varchar(255) collate utf8_unicode_ci default NULL,
  `url_update` varchar(255) collate utf8_unicode_ci default NULL,
  `home_class` varchar(255) collate utf8_unicode_ci default NULL,
  `plugin_name` varchar(50) collate utf8_unicode_ci default NULL,
  `url_docreate` varchar(255) collate utf8_unicode_ci default NULL,
  `create_script` varchar(255) collate utf8_unicode_ci default NULL,
  `create_specific` varchar(255) collate utf8_unicode_ci default NULL,
  `create_specific_form` varchar(255) collate utf8_unicode_ci default NULL,
  `url_domodify` varchar(255) collate utf8_unicode_ci default NULL,
  `modify_script` varchar(255) collate utf8_unicode_ci default NULL,
  `modify_specific` varchar(255) collate utf8_unicode_ci default NULL,
  `modify_specific_form` varchar(255) collate utf8_unicode_ci default NULL,
  PRIMARY KEY  (`id_portlet_type`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

--
-- Table structure for table `core_role`
--

DROP TABLE IF EXISTS `core_role`;
CREATE TABLE `core_role` (
  `role` varchar(50) collate utf8_unicode_ci NOT NULL default '',
  `role_description` varchar(255) collate utf8_unicode_ci default NULL,
  PRIMARY KEY  (`role`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

--
-- Table structure for table `core_style`
--

DROP TABLE IF EXISTS `core_style`;
CREATE TABLE `core_style` (
  `id_style` int(11) NOT NULL default '0',
  `description_style` varchar(50) collate utf8_unicode_ci NOT NULL default '',
  `id_portlet_type` varchar(50) collate utf8_unicode_ci default NULL,
  `id_portal_component` int(11) NOT NULL default '0',
  PRIMARY KEY  (`id_style`),
  KEY `index_style` (`id_portlet_type`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

--
-- Table structure for table `core_style_mode_stylesheet`
--

DROP TABLE IF EXISTS `core_style_mode_stylesheet`;
CREATE TABLE `core_style_mode_stylesheet` (
  `id_style` int(11) NOT NULL default '0',
  `id_mode` int(11) NOT NULL default '0',
  `id_stylesheet` int(11) NOT NULL default '0',
  PRIMARY KEY  (`id_style`,`id_mode`,`id_stylesheet`),
  KEY `index_style_mode_stylesheet` (`id_stylesheet`,`id_mode`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

--
-- Table structure for table `core_stylesheet`
--

DROP TABLE IF EXISTS `core_stylesheet`;
CREATE TABLE `core_stylesheet` (
  `id_stylesheet` int(11) NOT NULL default '0',
  `description` tinytext collate utf8_unicode_ci,
  `file_name` tinytext collate utf8_unicode_ci,
  `source` longblob,
  PRIMARY KEY  (`id_stylesheet`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

--
-- Table structure for table `core_user_right`
--

DROP TABLE IF EXISTS `core_user_right`;
CREATE TABLE `core_user_right` (
  `id_right` varchar(50) collate utf8_unicode_ci NOT NULL default '',
  `id_user` int(11) NOT NULL default '0',
  PRIMARY KEY  (`id_right`,`id_user`),
  KEY `index_user_right` (`id_user`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

--
-- Table structure for table `core_user_role`
--

DROP TABLE IF EXISTS `core_user_role`;
CREATE TABLE `core_user_role` (
  `role_key` varchar(50) collate utf8_unicode_ci NOT NULL default '',
  `id_user` int(11) NOT NULL default '0',
  PRIMARY KEY  (`role_key`,`id_user`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

--
-- Table structure for table 'core_mail_queue'
--

DROP TABLE IF EXISTS `core_mail_queue`;
CREATE TABLE `core_mail_queue`
(
	`id_mail_queue` int(11) NOT NULL default '0',
	`mail_item` longblob,
	`is_locked` smallint(1) default '0',
  	PRIMARY KEY  (`id_mail_queue`)
)ENGINE=MyISAM DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;


/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

