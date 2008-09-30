-- MySQL Administrator dump 1.4
--
-- ------------------------------------------------------
-- Server version	4.1.20-community-nt


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;

/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;



--
-- Dumping data for table `core_admin_auth_db_module`
--

/*!40000 ALTER TABLE `core_admin_auth_db_module` DISABLE KEYS */;
INSERT INTO `core_admin_auth_db_module` (`access_code`,`password`,`date_valid_password`,`last_password`,`last_name`,`first_name`,`email`) VALUES 
 ('admin','admin','2006-07-21','x','admin','admin','admin@lutece.fr'),
 ('lutece','lutece','2006-07-21','x','lutece','lutece','lutece@lutece.fr'),
 ('redac','redac','2006-07-21','x','redac','redac','redac@lutece.fr'),
 ('valid','valid','2006-07-21','x','valid','valid','valid@lutece.fr');
/*!40000 ALTER TABLE `core_admin_auth_db_module` ENABLE KEYS */;


--
-- Dumping data for table `core_admin_right`
--

/*!40000 ALTER TABLE `core_admin_right` DISABLE KEYS */;
INSERT INTO `core_admin_right` (`id_right`,`name`,`level_right`,`admin_url`,`description`,`is_updatable`,`plugin_name`,`id_feature_group`,`icon_url`,`documentation_url`, `id_order`) VALUES 
 ('CORE_ADMIN_SITE','portal.site.adminFeature.admin_site.name',2,'jsp/admin/site/AdminSite.jsp','portal.site.adminFeature.admin_site.description',1,NULL,'SITE','images/admin/skin/features/admin_site.png','jsp/admin/documentation/AdminDocumentation.jsp?doc=admin-site',1),
 ('CORE_CACHE_MANAGEMENT','portal.system.adminFeature.cache_management.name',0,'jsp/admin/system/ManageCaches.jsp','portal.system.adminFeature.cache_management.description',1,NULL,'SYSTEM','images/admin/skin/features/manage_caches.png',NULL,1),
 ('CORE_SEARCH_INDEXATION','portal.search.adminFeature.indexer.name',0,'jsp/admin/search/ManageSearchIndexation.jsp','portal.search.adminFeature.indexer.description',0,NULL,'SYSTEM',NULL,NULL,2),
 ('CORE_LOGS_VISUALISATION','portal.system.adminFeature.logs_visualisation.name',0,'jsp/admin/system/ManageFilesSystem.jsp','portal.system.adminFeature.logs_visualisation.description',1,NULL,'SYSTEM','images/admin/skin/features/view_logs.png',NULL,3),
 ('CORE_MODES_MANAGEMENT','portal.style.adminFeature.modes_management.name',0,'jsp/admin/style/ManageModes.jsp','portal.style.adminFeature.modes_management.description',1,NULL,'STYLE','images/admin/skin/features/manage_modes.png',NULL,1);
INSERT INTO `core_admin_right` (`id_right`,`name`,`level_right`,`admin_url`,`description`,`is_updatable`,`plugin_name`,`id_feature_group`,`icon_url`,`documentation_url`, `id_order`) VALUES 
 ('CORE_PAGE_TEMPLATE_MANAGEMENT','portal.style.adminFeature.page_template_management.name',0,'jsp/admin/style/ManagePageTemplates.jsp','portal.style.adminFeature.page_template_management.description',0,NULL,'STYLE','images/admin/skin/features/manage_page_templates.png',NULL,2),
 ('CORE_PLUGINS_MANAGEMENT','portal.system.adminFeature.plugins_management.name',0,'jsp/admin/system/ManagePlugins.jsp','portal.system.adminFeature.plugins_management.description',1,NULL,'SYSTEM','images/admin/skin/features/manage_plugins.png',NULL,4),
 ('CORE_PROPERTIES_MANAGEMENT','portal.site.adminFeature.properties_management.name',2,'jsp/admin/ManageProperties.jsp','portal.site.adminFeature.properties_management.description',0,NULL,'SITE',NULL,'jsp/admin/documentation/AdminDocumentation.jsp?doc=admin-properties',2),
 ('CORE_STYLESHEET_MANAGEMENT','portal.style.adminFeature.stylesheet_management.name',0,'jsp/admin/style/ManageStyleSheets.jsp','portal.style.adminFeature.stylesheet_management.description',1,NULL,'STYLE','images/admin/skin/features/manage_stylesheets.png',NULL,3),
 ('CORE_STYLES_MANAGEMENT','portal.style.adminFeature.styles_management.name',0,'jsp/admin/style/ManageStyles.jsp','portal.style.adminFeature.styles_management.description',1,NULL,'STYLE','images/admin/skin/features/manage_styles.png',NULL,4);
INSERT INTO `core_admin_right` (`id_right`,`name`,`level_right`,`admin_url`,`description`,`is_updatable`,`plugin_name`,`id_feature_group`,`icon_url`,`documentation_url`, `id_order`) VALUES 
 ('CORE_USERS_MANAGEMENT','portal.users.adminFeature.users_management.name',2,'jsp/admin/user/ManageUsers.jsp','portal.users.adminFeature.users_management.description',1,'','MANAGERS','images/admin/skin/features/manage_users.png',NULL,1),
 ('CORE_FEATURES_MANAGEMENT','portal.admin.adminFeature.features_management.name',0,'jsp/admin/features/ManageFeatures.jsp','portal.admin.adminFeature.features_management.description',0,NULL,'SYSTEM','images/admin/skin/features/manage_features.png',NULL,5),
 ('CORE_RBAC_MANAGEMENT','portal.rbac.adminFeature.rbac_management.name',0,'jsp/admin/rbac/ManageRoles.jsp','portal.rbac.adminFeature.rbac_management.description',0,'','MANAGERS','images/admin/skin/features/manage_rbac.png',NULL,2),
 ('CORE_DAEMONS_MANAGEMENT','portal.system.adminFeature.daemons_management.name',0,'jsp/admin/system/ManageDaemons.jsp','portal.system.adminFeature.daemons_management.description',0,NULL,'SYSTEM',NULL,NULL,6),
 ('CORE_WORKGROUPS_MANAGEMENT','portal.workgroup.adminFeature.workgroups_management.name',2,'jsp/admin/workgroup/ManageWorkgroups.jsp','portal.workgroup.adminFeature.workgroups_management.description',0,NULL,'MANAGERS','images/admin/skin/features/manage_workgroups.png',NULL,3);
INSERT INTO `core_admin_right` (`id_right`,`name`,`level_right`,`admin_url`,`description`,`is_updatable`,`plugin_name`,`id_feature_group`,`icon_url`,`documentation_url`, `id_order`) VALUES 
 ('CORE_ROLES_MANAGEMENT','portal.role.adminFeature.roles_management.name',2,'jsp/admin/role/ManagePageRole.jsp','portal.role.adminFeature.roles_management.description',0,NULL,'USERS','images/admin/skin/features/manage_roles.png',NULL,1),
 ('CORE_GROUPS_MANAGEMENT','portal.group.adminFeature.groups_management.name',2,'jsp/admin/group/ManageGroups.jsp','portal.group.adminFeature.groups_management.description',0,NULL,'USERS','images/admin/skin/features/manage_groups.png',NULL,2),
 ('CORE_MAILINGLISTS_MANAGEMENT','portal.mailinglist.adminFeature.mailinglists_management.name',2,'jsp/admin/mailinglist/ManageMailingLists.jsp','portal.mailinglist.adminFeature.mailinglists_management.description',0,NULL,'MANAGERS','images/admin/skin/features/manage_mailinglists.png',NULL,4),
 ('CORE_LEVEL_RIGHT_MANAGEMENT','portal.users.adminFeature.level_right_management.name',2,'jsp/admin/features/ManageLevels.jsp','portal.users.adminFeature.level_right_management.description',0,NULL,'MANAGERS','images/admin/skin/features/manage_rights_levels.png',NULL,5);
/*!40000 ALTER TABLE `core_admin_right` ENABLE KEYS */;


--
-- Dumping data for table `core_admin_role`
--

/*!40000 ALTER TABLE `core_admin_role` DISABLE KEYS */;
INSERT INTO `core_admin_role` (`role_key`,`role_description`) VALUES 
 ('all_site_manager','Site Manager'),
 ('super_admin','Super Administrateur');
/*!40000 ALTER TABLE `core_admin_role` ENABLE KEYS */;


--
-- Dumping data for table `core_admin_role_resource`
--

/*!40000 ALTER TABLE `core_admin_role_resource` DISABLE KEYS */;
INSERT INTO `core_admin_role_resource` (`rbac_id`,`role_key`,`resource_type`,`resource_id`,`permission`) VALUES 
 (57,'all_site_manager','PAGE','*','VIEW'),
 (58,'all_site_manager','PAGE','*','MANAGE'),
 (77,'super_admin','INSERT_SERVICE','*','*'),
 (101,'all_site_manager','PORTLET_TYPE','*','*');
/*!40000 ALTER TABLE `core_admin_role_resource` ENABLE KEYS */;


--
-- Dumping data for table `core_admin_user`
--

/*!40000 ALTER TABLE `core_admin_user` DISABLE KEYS */;
INSERT INTO `core_admin_user` (`id_user`,`access_code`,`last_name`,`first_name`,`email`,`status`,`password`,`locale`,`level_user`) VALUES 
 (1,'admin','Admin','admin','',0,'admin','fr',0),
 (2,'lutece','Lutèce','lutece','',0,'lutece','fr',1),
 (3,'redac','redac','redac','redac@mdp',0,'redac','fr',2),
 (4,'valid','valid','valid','valid@mdp',0,'valid','fr',3);
/*!40000 ALTER TABLE `core_admin_user` ENABLE KEYS */;


--
-- Dumping data for table `core_admin_workgroup`
--

/*!40000 ALTER TABLE `core_admin_workgroup` DISABLE KEYS */;
/*!40000 ALTER TABLE `core_admin_workgroup` ENABLE KEYS */;


--
-- Dumping data for table `core_admin_workgroup_user`
--

/*!40000 ALTER TABLE `core_admin_workgroup_user` DISABLE KEYS */;
/*!40000 ALTER TABLE `core_admin_workgroup_user` ENABLE KEYS */;


--
-- Dumping data for table `core_connections_log`
--

/*!40000 ALTER TABLE `core_connections_log` DISABLE KEYS */;
/*!40000 ALTER TABLE `core_connections_log` ENABLE KEYS */;


--
-- Dumping data for table `core_feature_group`
--

/*!40000 ALTER TABLE `core_feature_group` DISABLE KEYS */;
INSERT INTO `core_feature_group` (`id_feature_group`,`feature_group_description`,`feature_group_label`,`feature_group_order`) VALUES 
 ('CONTENT','portal.features.group.content.description','portal.features.group.content.label',1),
 ('APPLICATIONS','portal.features.group.applications.description','portal.features.group.applications.label',3),
 ('SYSTEM','portal.features.group.system.description','portal.features.group.system.label',7),
 ('SITE','portal.features.group.site.description','portal.features.group.site.label',2),
 ('STYLE','portal.features.group.charter.description','portal.features.group.charter.label',6),
 ('USERS','portal.features.group.users.description','portal.features.group.users.label',4),
 ('MANAGERS','portal.features.group.managers.description','portal.features.group.managers.label',5);
/*!40000 ALTER TABLE `core_feature_group` ENABLE KEYS */;


--
-- Dumping data for table `core_level_right`
--

/*!40000 ALTER TABLE `core_level_right` DISABLE KEYS */;
INSERT INTO `core_level_right` (`id_level`,`name`) VALUES 
 (0,'Niveau 0 - Droits de l\'administrateur technique'),
 (1,'Niveau 1 - Droits de l\'administrateur fonctionnel'),
 (2,'Niveau 2 - Droits du webmestre'),
 (3,'Niveau 3 - Droits de l\'assistant webmestre');
/*!40000 ALTER TABLE `core_level_right` ENABLE KEYS */;


--
-- Dumping data for table `core_mode`
--

/*!40000 ALTER TABLE `core_mode` DISABLE KEYS */;
INSERT INTO `core_mode` (`id_mode`,`description_mode`,`path`,`output_xsl_method`,`output_xsl_version`,`output_xsl_media_type`,`output_xsl_encoding`,`output_xsl_indent`,`output_xsl_omit_xml_dec`,`output_xsl_standalone`) VALUES 
 (0,'Normal','normal/','xml','1.0','text/xml','UTF-8','yes','yes','yes'),
 (1,'Administration','admin/','xml','1.0','text/xml','UTF-8','yes','yes','yes'),
 (2,'Wap','wml/','xml','1.0','text/xml','UTF-8','yes','yes','yes');
/*!40000 ALTER TABLE `core_mode` ENABLE KEYS */;


--
-- Dumping data for table `core_page`
--

/*!40000 ALTER TABLE `core_page` DISABLE KEYS */;
INSERT INTO `core_page` VALUES
 (1,0,'accueil','Page d\'accueil','2007-11-24 17:12:12',1,1,4,'2003-09-09 08:38:01','none','default',0,'','application/octet-stream','all'),
 (3,1,'Documentation','Tout ce dont vous avez besoin pour utiliser Lutece','2006-10-18 17:39:24',0,2,2,'2002-09-09 08:46:46','none','default',0,'','application/octet-stream','all'),
 (6,3,'Guide utilisateur','Accès au guide utilisateur','2006-09-19 16:20:13',0,1,1,'2006-02-15 17:39:59','none','default',1,NULL,'','all'),
 (5,1,'L\'outil','Description du CMS Lutèce','2006-10-12 17:03:49',0,1,1,'2006-02-15 17:37:26','none','default',1,NULL,'','all'),
 (7,3,'Guide technique','Accès à documentation technique','2006-09-19 16:19:45',0,2,1,'2006-02-15 17:40:30','none','default',1,'GIF89a0\00\0÷\0\0|‚”DBDTR|ÄÂ¼¤¢¤lj„\\^\\$\"$<BläâÜtn””’”424´²´TRdÔÒÌTRT\\^||z|ôòì¤¢´”’¬42DDBT,*,|z”„‚¤dfŒtr„LNd¼º¼TRtÜÚÜ\r\n\n\r\nÄÆÌtvtäæìŒ’¤\Zdf|¬ª¬ljŒ\\^ttvŒüúô¬ª´<:DLJLTZ|$&4tv”<:<\\Zdœž¬LJT,*4|~¤ŒŽ¤DJtŒŠœ¤¢¬$\",trœ´¶ÄÜÚÔ\\Z\\\\^„|zœ¼ºÄÜÞä\r\nÌÎÌìîìœš¤\Z$ljl¬ª¼\r\n„†œDBLÄÂÄln„DFläâälr”œšœ42<´²¼TRlÔÒÔTR\\|~|ôòô”’´46D\\ZtÜÞÜ”–¤df„lnŒdbttzŒüúü<:L\\Z„LJ\\,.4ŒŠ¬LNt„‚”DFDTV|¤¦¤dfd$&$DBl”–”464´¶´\\VdTVTdb|tv„¤¦´LJltrŒ\\Zlœž´ÌÎÔìîôÌÊÌìêìdb„”–¬DFT,.,|~”„†¤¼¾¼TVt\r\n\r\n¬®¬¬®´LNL<><¤¦¬$&,|~œ¼¾Ä$¬®¼ÄÆÄäæäÔÖÔôöôüþü<>LLN\\ŒŽ¬lfŒ”’¤lf|d^t|vŒ\\Z|LJttn„tr”<6DäÞÜtnŒ4.4||ÿ\0pH\0À\0\0\0\0\0\0Â \0*î\0‚\0|\0 \0\0`\0\0\0\0\0\0\0\0èxRÁ\0\0\0\0\0¾\0>µ\0‚K\0|\0W}XöÁ€\0|\0\0\\tóÀÁ²N\0\0\0 ÷Œ`>Á‚\0|\0i¾\\ð>MN‚e\0|s\r\nP ðÄdNo\0\0c\0¶uÿm\0ÿe\0nptÀÄs\\\0\0M\0èeRs\0 \0\0i¾m?>a‚‚g||e\0ès\0R\\\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0¯\0\0I\0G\0\0\0 ž\0Ä\0\0\0\0\0\0Ä\0\0\0\0\0!ù\0\0\0\0,\0\0\0\00\00\0ÿ\0H° Áƒ*\\È°¡Ã‡#JœH±¢Å‚X0]¬ˆdO}(0hRb£Ãˆô¢Tš4¡:ñ1™°c‡2Y”x23”¡15hl°bBœOª|	Å´Ê#Œ„a£è\r\n%ˆ<õLó	„žG\r¤\nl#h ¤U1\r¥¤ÓSP¥R]\0éÖ# ˜¦%EÓ#=€5mÓ‚H™B Ò®ý²EŠpè¼qÄ\Z!v(©0fi¨4ž’tê$å©É”-bjÑA•JDè%âÐ!Òþê™äÀÎF†·(Ð©Ô¢-I’Ü.íÕÁ¤I6âHd@Ä¬U”TÙ¡ÐrÈ 8ÿP1É÷Å®uúä	‘÷[/7íœ¼¤Ê-î\0úTÅSZòæWs“¨ BtÂŸŒHGÈÜiÏÑ¡Â}EÑÚ!\"øç	,Ê	H_bx‚P‘…&ŒáÉ!NXŸ\n¢`PqUT1†m¸H#’ÀŒ`!@\r\n\r\n1ÄCH`ÉL\n\r2Ç¡@µ	Øs4Òñ‡íUàÒZhª°P\r\"…cñÉh2Vx	[t˜\"|\"¢„_|‘W\'ä¡fÜá,GŒ!i~æœ…$Á_{íùYA gR\0^,äG Œaê\'ŽHž–ÂbB¦~ÿ*(L‡Ðáœ°ÀB£Ì1Á¦6F€nãé!Å![áÉ²~nÆ)­t<ç\0%°‘PeP,Ö=¢	HÑ	°Ìòù\'§yÕÊ›x;h¡P$Üqƒ£[ävÚWâ&Ñ!¦Í>J­ÎIfJp,4-ŒñI!¦à˜ˆÐ_¦J²© ÿ ­d¸²ëB¢¬!†\n$W¸°z)¬Î~	ÑNB‰Š¡Cd›	%‡|•›&‡|¢¬	æ\Zâo\'\ZË¬G:Àà,„È;p+‚cô¸ìÖ\\{¢„§ÏM¢Ç%ŒQ+Èˆ&€ògVËöè£©W\"‚äIF6\r\n‰ÿâŠ\r\n!‚²™úgª£W‹`·äÉ\r\n…‘L”CEèà)ŠÂ‰$rõácñÉ\'I¬\r\nELX´	¢°!†f\0‘	;dîDš€ì8ÐƒmH\r\n‘G$\\`‚EˆQ†EÐ~!NìÀAmÒJ\"”ÐÀ\r\nÁ\n˜„Á¼‚< Ñ\"4ÌÑˆ\'¡ø¸…WÔ!‡(€¿PÁ¡ÐŠsxÛDÐ“PpFIxÄ$®à…:H ú³H+Ê0C(à\r\nþqÉq€DšÇ”æ)æ±Hˆ0…1\\ÇGÐ	$ô–I•g#mH	>Á§á˜…^Ñ%\nd¢ŠD‚TxÒâ í$§ƒ¦ÓL¢ L4È=&P@!bDŸñ”¨\"\0;','image/gif','all'),
 (9,1,'Collaboratif','Espace de travail collaboratif','2007-11-24 18:24:19',0,3,1,'2006-10-12 13:17:31','none','default',1,NULL,NULL,'all'),
 (10,1,'Développeurs','Ressources pour les contributeurs/développeurs Lutèce','2007-11-24 18:26:31',0,4,4,'2006-10-12 17:03:20','none','default',1,NULL,NULL,'all');
/*!40000 ALTER TABLE `core_page` ENABLE KEYS */;


--
-- Dumping data for table `core_page_template`
--

/*!40000 ALTER TABLE `core_page_template` DISABLE KEYS */;
INSERT INTO `core_page_template` (`id_template`,`description`,`file_name`,`picture`) VALUES 
 (1,'Une colonne','skin/site/page_template1.html','page_template1.gif'),
 (2,'Deux colonnes','skin/site/page_template2.html','page_template2.gif'),
 (3,'Trois colonnes','skin/site/page_template3.html','page_template3.gif'),
 (4,'1 + 2 colonnes','skin/site/page_template4.html','page_template4.gif'),
 (5,'Deux colonnes égales','skin/site/page_template5.html','page_template5.gif'),
 (6,'Trois colonnes inégales','skin/site/page_template6.html','page_template6.gif');
/*!40000 ALTER TABLE `core_page_template` ENABLE KEYS */;


--
-- Dumping data for table `core_parameters`
--

/*!40000 ALTER TABLE `core_parameters` DISABLE KEYS */;
INSERT INTO `core_parameters` (`password_duration`) VALUES 
 (120);
/*!40000 ALTER TABLE `core_parameters` ENABLE KEYS */;


--
-- Dumping data for table `core_portal_component`
--

/*!40000 ALTER TABLE `core_portal_component` DISABLE KEYS */;
INSERT INTO `core_portal_component` (`id_portal_component`,`name`) VALUES 
 (0,'Rubrique'),
 (1,'Article'),
 (2,'Rubrique Liste Article'),
 (3,'Menu Init'),
 (4,'Menu Principal'),
 (5,'Chemin Page'),
 (6,'Plan du site'),
 (7,'Arborescence'),
 (8,'Plan du site admin');
/*!40000 ALTER TABLE `core_portal_component` ENABLE KEYS */;


--
-- Dumping data for table `core_portlet`
--

/*!40000 ALTER TABLE `core_portlet` DISABLE KEYS */;
/*!40000 ALTER TABLE `core_portlet` ENABLE KEYS */;


--
-- Dumping data for table `core_portlet_alias`
--

/*!40000 ALTER TABLE `core_portlet_alias` DISABLE KEYS */;
/*!40000 ALTER TABLE `core_portlet_alias` ENABLE KEYS */;


--
-- Dumping data for table `core_portlet_type`
--

/*!40000 ALTER TABLE `core_portlet_type` DISABLE KEYS */;
INSERT INTO `core_portlet_type` (`id_portlet_type`,`name`,`url_creation`,`url_update`,`home_class`,`plugin_name`,`url_docreate`,`create_script`,`create_specific`,`create_specific_form`,`url_domodify`,`modify_script`,`modify_specific`,`modify_specific_form`) VALUES 
 ('ALIAS_PORTLET','portal.site.portletAlias.name','plugins/alias/CreatePortletAlias.jsp','plugins/alias/ModifyPortletAlias.jsp','fr.paris.lutece.portal.business.portlet.AliasPortletHome','alias','plugins/alias/DoCreatePortletAlias.jsp','/admin/portlet/script_create_portlet.html','/admin/portlet/alias/create_portlet_alias.html','','plugins/alias/DoModifyPortletAlias.jsp','/admin/portlet/script_modify_portlet.html','/admin/portlet/alias/modify_portlet_alias.html','');
/*!40000 ALTER TABLE `core_portlet_type` ENABLE KEYS */;


--
-- Dumping data for table `core_role`
--

/*!40000 ALTER TABLE `core_role` DISABLE KEYS */;
/*!40000 ALTER TABLE `core_role` ENABLE KEYS */;


--
-- Dumping data for table `core_style`
--

/*!40000 ALTER TABLE `core_style` DISABLE KEYS */;
INSERT INTO `core_style` (`id_style`,`description_style`,`id_portlet_type`,`id_portal_component`) VALUES 
 (3,'Menu Init','',3),
 (4,'Main Menu','',4),
 (5,'Chemin Page','',5),
 (6,'Plan du site','',6),
 (7,'Arborescence','',7),
 (8,'Plan du Site Admin',NULL,8);
/*!40000 ALTER TABLE `core_style` ENABLE KEYS */;


--
-- Dumping data for table `core_style_mode_stylesheet`
--

/*!40000 ALTER TABLE `core_style_mode_stylesheet` DISABLE KEYS */;
INSERT INTO `core_style_mode_stylesheet` (`id_style`,`id_mode`,`id_stylesheet`) VALUES 
 (3,0,211),
 (4,0,213),
 (5,0,215),
 (6,0,217),
 (7,0,253),
 (8,1,279);
/*!40000 ALTER TABLE `core_style_mode_stylesheet` ENABLE KEYS */;


--
-- Dumping data for table `core_stylesheet`
--

/*!40000 ALTER TABLE `core_stylesheet` DISABLE KEYS */;
INSERT INTO `core_stylesheet` (`id_stylesheet`,`description`,`file_name`,`source`) VALUES 
 (253,'Pages filles - Arborescence','menu_tree.xsl',0x3C3F786D6C2076657273696F6E3D22312E30223F3E0D0A3C78736C3A7374796C6573686565742076657273696F6E3D22312E302220786D6C6E733A78736C3D22687474703A2F2F7777772E77332E6F72672F313939392F58534C2F5472616E73666F726D223E0D0A0D0A3C78736C3A706172616D206E616D653D22736974652D70617468222073656C6563743D22736974652D7061746822202F3E0D0A0D0A3C78736C3A74656D706C617465206D617463683D226D656E752D6C697374223E0D0A3C6272202F3E3C6272202F3E0D0A093C6469762069643D226D656E752D696E6974223E0D0A09093C6469762069643D226D656E752D696E69742D636F6E74656E74223E0D0A2020202020202020202020203C756C2069643D226D656E752D7665727469223E0D0A202020202020202020202020202020203C78736C3A6170706C792D74656D706C617465732073656C6563743D226D656E7522202F3E0D0A2020202020202020202020203C2F756C3E0D0A20202020202020203C2F6469763E0D0A20202020203C2F6469763E0D0A3C2F78736C3A74656D706C6174653E0D0A0D0A0D0A3C78736C3A74656D706C617465206D617463683D226D656E75223E0D0A202020203C78736C3A7661726961626C65206E616D653D22696E646578223E0D0A20202020093C78736C3A6E756D626572206C6576656C3D2273696E676C6522202F3E0D0A202020203C2F78736C3A7661726961626C653E0D0A0D0A202020203C78736C3A696620746573743D2224696E646578202667743B2037223E0D0A20202020202020203C6C6920636C6173733D2266697273742D7665727469223E0D0A202020202020202020203C6120687265663D227B24736974652D706174687D3F706167655F69643D7B706167652D69647D22207461726765743D225F746F70223E0D0A2020202020202020202020202020203C78736C3A76616C75652D6F662073656C6563743D22706167652D6E616D6522202F3E0D0A20202020202020202020203C2F613E0D0A20202020202020202020203C78736C3A6170706C792D74656D706C617465732073656C6563743D227375626C6576656C2D6D656E752D6C69737422202F3E0D0A20202020202020203C2F6C693E0D0A2020203C2F78736C3A69663E0D0A0D0A3C2F78736C3A74656D706C6174653E0D0A0D0A3C78736C3A74656D706C617465206D617463683D227375626C6576656C2D6D656E752D6C69737422203E0D0A093C756C3E0D0A20202020093C6C6920636C6173733D226C6173742D7665727469223E0D0A090920093C78736C3A6170706C792D74656D706C617465732073656C6563743D227375626C6576656C2D6D656E7522202F3E0D0A2009202020203C2F6C693E0D0A202020203C2F756C3E0D0A3C2F78736C3A74656D706C6174653E0D0A0D0A0D0A3C78736C3A74656D706C617465206D617463683D227375626C6576656C2D6D656E75223E0D0A2020203C78736C3A7661726961626C65206E616D653D22696E6465785F736F75735F6D656E75223E0D0A2020202020202020203C78736C3A6E756D626572206C6576656C3D2273696E676C6522202F3E0D0A2020203C2F78736C3A7661726961626C653E0D0A0D0A2020203C6120687265663D227B24736974652D706174687D3F706167655F69643D7B706167652D69647D22207461726765743D225F746F70223E0D0A09093C7370616E3E3C78736C3A76616C75652D6F662073656C6563743D22706167652D6E616D6522202F3E3C2F7370616E3E0D0A2020203C2F613E0D0A3C2F78736C3A74656D706C6174653E0D0A0D0A3C2F78736C3A7374796C6573686565743E0D0A),
 (215,'Chemin page','page_path.xsl',0x3C3F786D6C2076657273696F6E3D22312E30223F3E0D0A3C78736C3A7374796C6573686565742076657273696F6E3D22312E302220786D6C6E733A78736C3D22687474703A2F2F7777772E77332E6F72672F313939392F58534C2F5472616E73666F726D223E0D0A0D0A3C78736C3A706172616D206E616D653D22736974652D70617468222073656C6563743D22736974652D7061746822202F3E0D0A0D0A0D0A3C78736C3A74656D706C617465206D617463683D2270616765223E0D0A09093C78736C3A696620746573743D22706F736974696F6E2829213D6C61737428292D31223E0D0A0909093C6120687265663D227B24736974652D706174687D3F706167655F69643D7B706167652D69647D22207461726765743D225F746F70223E3C78736C3A76616C75652D6F662073656C6563743D22706167652D6E616D6522202F3E3C2F613E203E0D0A09093C2F78736C3A69663E0D0A09093C78736C3A696620746573743D22706F736974696F6E28293D6C61737428292D31223E0D0A0909093C78736C3A76616C75652D6F662073656C6563743D22706167652D6E616D6522202F3E0D0A09093C2F78736C3A69663E0D0A3C2F78736C3A74656D706C6174653E0D0A0D0A0D0A3C78736C3A74656D706C617465206D617463683D22706167655F6C696E6B223E0D0A09093C78736C3A696620746573743D22706F736974696F6E2829213D6C61737428292D31223E0D0A0909093C6120687265663D227B24736974652D706174687D3F7B706167652D75726C7D22207461726765743D225F746F70223E3C78736C3A76616C75652D6F662073656C6563743D22706167652D6E616D6522202F3E3C2F613E203E0D0A09093C2F78736C3A69663E0D0A09093C78736C3A696620746573743D22706F736974696F6E28293D6C61737428292D31223E0D0A0909093C78736C3A76616C75652D6F662073656C6563743D22706167652D6E616D6522202F3E0D0A09093C2F78736C3A69663E0D0A3C2F78736C3A74656D706C6174653E0D0A0D0A0D0A3C2F78736C3A7374796C6573686565743E);
INSERT INTO `core_stylesheet` (`id_stylesheet`,`description`,`file_name`,`source`) VALUES 
 (213,'Menu principal','menu_main.xsl',0x3C3F786D6C2076657273696F6E3D22312E30223F3E0D0A3C78736C3A7374796C6573686565742076657273696F6E3D22312E302220786D6C6E733A78736C3D22687474703A2F2F7777772E77332E6F72672F313939392F58534C2F5472616E73666F726D223E0D0A0D0A3C78736C3A706172616D206E616D653D22736974652D70617468222073656C6563743D22736974652D7061746822202F3E0D0A0D0A3C78736C3A74656D706C617465206D617463683D226D656E752D6C697374223E0D0A093C6469762069643D226D656E752D6D61696E223E0D0A2020202020202020203C756C2069643D226D656E75223E0D0A202020202020202020202020203C78736C3A6170706C792D74656D706C617465732073656C6563743D226D656E7522202F3E0D0A2020202020202020203C2F756C3E0D0A202020203C2F6469763E0D0A3C2F78736C3A74656D706C6174653E0D0A0D0A0D0A3C78736C3A74656D706C617465206D617463683D226D656E75223E0D0A202020203C78736C3A7661726961626C65206E616D653D22696E646578223E0D0A20202020093C78736C3A6E756D626572206C6576656C3D2273696E676C6522202F3E0D0A202020203C2F78736C3A7661726961626C653E0D0A0D0A202020203C78736C3A696620746573743D2224696E64657820266C743B2035223E0D0A0D0A20202020203C78736C3A63686F6F73653E0D0A202020202020203C78736C3A7768656E20746573743D22706F736974696F6E28293D3122203E0D0A2020202020202020202020203C6C6920636C6173733D226669727374223E0D0A202020202020202020202020202020203C6120687265663D227B24736974652D706174687D3F706167655F69643D7B706167652D69647D22207461726765743D225F746F7022203E0D0A202020202020202020202020202020202020203C78736C3A76616C75652D6F662073656C6563743D22706167652D6E616D6522202F3E0D0A202020202020202020202020202020203C2F613E0D0A092020202020202020202020203C212D2D203C78736C3A6170706C792D74656D706C617465732073656C6563743D227375626C6576656C2D6D656E752D6C69737422202F3E202D2D3E0D0A202020202020202020202020203C2F6C693E0D0A202020202020203C2F78736C3A7768656E3E0D0A202020202020203C78736C3A7768656E20746573743D22706F736974696F6E28293D6C6173742829206F722024696E6465783D3422203E0D0A202020202020202020202020202020202020203C6C6920636C6173733D226C617374223E0D0A202020202020202020202020202020202020202020203C6120687265663D227B24736974652D706174687D3F706167655F69643D7B706167652D69647D22207461726765743D225F746F7022203E0D0A2020202020202020202020202020202020202020202020202020202020203C78736C3A76616C75652D6F662073656C6563743D22706167652D6E616D6522202F3E0D0A20202020202020202020202020202020202020202020203C2F613E0D0A202020202020202020202020202020202020202020203C212D2D203C78736C3A6170706C792D74656D706C617465732073656C6563743D227375626C6576656C2D6D656E752D6C69737422202F3E202D2D3E0D0A20202020202020202020202020202020202020203C2F6C693E0D0A202020202020203C2F78736C3A7768656E3E0D0A202020202020203C78736C3A6F74686572776973653E0D0A202020202020202020202020202020202020203C6C693E0D0A202020202020202020202020202020202020202020203C6120687265663D227B24736974652D706174687D3F706167655F69643D7B706167652D69647D22207461726765743D225F746F7022203E0D0A2020202020202020202020202020202020202020202020202020202020203C78736C3A76616C75652D6F662073656C6563743D22706167652D6E616D6522202F3E0D0A20202020202020202020202020202020202020202020203C2F613E0D0A20202020202020202020202020202020202020202020203C212D2D203C78736C3A6170706C792D74656D706C617465732073656C6563743D227375626C6576656C2D6D656E752D6C69737422202F3E202D2D3E0D0A20202020202020202020202020202020202020203C2F6C693E0D0A202020202020203C2F78736C3A6F74686572776973653E0D0A20202020202020203C2F78736C3A63686F6F73653E0D0A2020203C2F78736C3A69663E0D0A0D0A3C2F78736C3A74656D706C6174653E0D0A0D0A3C78736C3A74656D706C617465206D617463683D227375626C6576656C2D6D656E752D6C69737422203E0D0A093C78736C3A696620746573743D227375626C6576656C2D6D656E75213D2727223E0D0A20202020093C756C3E0D0A20092020202020202020202020203C78736C3A6170706C792D74656D706C617465732073656C6563743D227375626C6576656C2D6D656E7522202F3E0D0A20202020202020203C2F756C3E0D0A093C2F78736C3A69663E0D0A3C2F78736C3A74656D706C6174653E0D0A0D0A3C78736C3A74656D706C617465206D617463683D227375626C6576656C2D6D656E75223E0D0A2020203C78736C3A7661726961626C65206E616D653D22696E6465785F736F75735F6D656E75223E0D0A2020202020202020203C78736C3A6E756D626572206C6576656C3D2273696E676C6522202F3E0D0A2020203C2F78736C3A7661726961626C653E0D0A0D0A20202020202020203C78736C3A63686F6F73653E0D0A20202020202020202020203C78736C3A7768656E20746573743D22706F736974696F6E28293D3122203E0D0A202020202020202020202020203C6C6920636C6173733D226669727374223E0D0A202020202020202020202020202020203C6120687265663D227B24736974652D706174687D3F706167655F69643D7B706167652D69647D22207461726765743D225F746F7022203E0D0A0920202020202020202020202020202020202020203C7370616E3E3C78736C3A76616C75652D6F662073656C6563743D22706167652D6E616D6522202F3E3C2F7370616E3E0D0A202020202020202020202020202020203C2F613E0D0A20202020202020202020202020203C2F6C693E0D0A20202020202020202020203C2F78736C3A7768656E3E0D0A20202020202020202020203C78736C3A7768656E20746573743D22706F736974696F6E28293D6C617374282922203E0D0A202020202020202020202020203C6C6920636C6173733D226C617374223E0D0A202020202020202020202020202020203C6120687265663D227B24736974652D706174687D3F706167655F69643D7B706167652D69647D22207461726765743D225F746F7022203E0D0A0920202020202020202020202020202020202020203C7370616E3E3C78736C3A76616C75652D6F662073656C6563743D22706167652D6E616D6522202F3E3C2F7370616E3E0D0A202020202020202020202020202020203C2F613E0D0A20202020202020202020202020203C2F6C693E0D0A20202020202020202020203C2F78736C3A7768656E3E0D0A20202020202020202020203C78736C3A6F74686572776973653E0D0A2020202020202020202020202020203C6C693E0D0A202020202020202020202020202020203C6120687265663D227B24736974652D706174687D3F706167655F69643D7B706167652D69647D22207461726765743D225F746F7022203E0D0A0920202020202020202020202020202020202020203C7370616E3E3C78736C3A76616C75652D6F662073656C6563743D22706167652D6E616D6522202F3E3C2F7370616E3E0D0A202020202020202020202020202020203C2F613E0D0A20202020202020202020202020203C2F6C693E0D0A20202020202020202020203C2F78736C3A6F74686572776973653E0D0A20202020202020203C2F78736C3A63686F6F73653E0D0A0D0A3C2F78736C3A74656D706C6174653E0D0A0D0A3C2F78736C3A7374796C6573686565743E0D0A),
 (211,'Menu Init','menu_init.xsl',0x3C3F786D6C2076657273696F6E3D22312E30223F3E0D0A3C78736C3A7374796C6573686565742076657273696F6E3D22312E302220786D6C6E733A78736C3D22687474703A2F2F7777772E77332E6F72672F313939392F58534C2F5472616E73666F726D223E0D0A0D0A3C78736C3A706172616D206E616D653D22736974652D70617468222073656C6563743D22736974652D7061746822202F3E0D0A0D0A3C78736C3A74656D706C617465206D617463683D226D656E752D6C697374223E0D0A3C6272202F3E3C6272202F3E0D0A093C6469762069643D226D656E752D696E6974223E0D0A09093C6469762069643D226D656E752D696E69742D636F6E74656E74223E0D0A2020202020202020202020203C756C2069643D226D656E752D7665727469223E0D0A202020202020202020202020202020203C78736C3A6170706C792D74656D706C617465732073656C6563743D226D656E7522202F3E0D0A2020202020202020202020203C2F756C3E0D0A20202020202020203C2F6469763E0D0A20202020203C2F6469763E0D0A3C2F78736C3A74656D706C6174653E0D0A0D0A0D0A3C78736C3A74656D706C617465206D617463683D226D656E75223E0D0A202020203C78736C3A7661726961626C65206E616D653D22696E646578223E0D0A20202020093C78736C3A6E756D626572206C6576656C3D2273696E676C6522202F3E0D0A202020203C2F78736C3A7661726961626C653E0D0A0D0A202020203C78736C3A696620746573743D2224696E646578202667743B2037223E0D0A20202020202020203C6C6920636C6173733D2266697273742D7665727469223E0D0A2020202020202020093C6120687265663D227B24736974652D706174687D3F706167655F69643D7B706167652D69647D22207461726765743D225F746F70223E0D0A2020202020202020202009093C78736C3A76616C75652D6F662073656C6563743D22706167652D6E616D6522202F3E0D0A0920202020202020203C2F613E0D0A2020202009202020203C78736C3A6170706C792D74656D706C617465732073656C6563743D227375626C6576656C2D6D656E752D6C69737422202F3E0D0A20202020202020203C2F6C693E0D0A2020203C2F78736C3A69663E0D0A3C2F78736C3A74656D706C6174653E0D0A0D0A3C78736C3A74656D706C617465206D617463683D227375626C6576656C2D6D656E752D6C69737422203E0D0A093C756C3E0D0A20202020093C6C6920636C6173733D226C6173742D7665727469223E0D0A090920093C78736C3A6170706C792D74656D706C617465732073656C6563743D227375626C6576656C2D6D656E7522202F3E0D0A2009202020203C2F6C693E0D0A202020203C2F756C3E0D0A3C2F78736C3A74656D706C6174653E0D0A0D0A3C78736C3A74656D706C617465206D617463683D227375626C6576656C2D6D656E75223E0D0A2020203C78736C3A7661726961626C65206E616D653D22696E6465785F736F75735F6D656E75223E0D0A2020202020202020203C78736C3A6E756D626572206C6576656C3D2273696E676C6522202F3E0D0A2020203C2F78736C3A7661726961626C653E0D0A0D0A2020203C6120687265663D227B24736974652D706174687D3F706167655F69643D7B706167652D69647D22207461726765743D225F746F70223E0D0A09093C7370616E3E3C78736C3A76616C75652D6F662073656C6563743D22706167652D6E616D6522202F3E3C2F7370616E3E0D0A2020203C2F613E0D0A3C2F78736C3A74656D706C6174653E0D0A0D0A3C2F78736C3A7374796C6573686565743E0D0A);
INSERT INTO `core_stylesheet` (`id_stylesheet`,`description`,`file_name`,`source`) VALUES 
 (217,'Plan du site','site_map.xsl','<?xml version=\"1.0\" encoding=\"ISO-8859-1\"?>\r\n<xsl:stylesheet version=\"1.0\" xmlns:xsl=\"http://www.w3.org/1999/XSL/Transform\">\r\n\r\n<xsl:param name=\"site-path\" select=\"site-path\" />\r\n\r\n\r\n<xsl:template match=\"page[page-level=0]\">\r\n<div id=\"content\">\r\n<div id=\"one-zone-first\">\r\n		<div id=\"one-zone-first-content\">			\r\n			<xsl:apply-templates select=\"child-pages-list\" />\r\n		</div>\r\n	</div>\r\n</div>\r\n<div id=\"sidebar\">\r\n	<img src=\"document?id=7&amp;id_attribute=52\" alt=\"banner\" title=\"banner\"/>\r\n	&#160;\r\n</div>\r\n	\r\n</xsl:template>\r\n\r\n\r\n<xsl:template match=\"page[page-level=1]\" >\r\n<ul class=\"site-map-level-one\">\r\n	<li>\r\n		<a href=\"{$site-path}?page_id={page-id}\" target=\"_top\">\r\n			<xsl:value-of select=\"page-name\" />\r\n		</a>\r\n		<xsl:apply-templates select=\"page-description\" />\r\n		<xsl:apply-templates select=\"page-image\" />\r\n		<xsl:apply-templates select=\"child-pages-list\" />\r\n	    <xsl:text disable-output-escaping=\"yes\">\r\n		    <![CDATA[<div class=\"clear\">&#160;</div>]]>\r\n	    </xsl:text>\r\n	</li>\r\n</ul>\r\n</xsl:template>\r\n\r\n\r\n<xsl:template match=\"page[page-level=2]\" >\r\n<ul class=\"site-map-level-two\">\r\n	<li>\r\n		<a href=\"{$site-path}?page_id={page-id}\" target=\"_top\">\r\n			<xsl:value-of select=\"page-name\" />\r\n		</a>\r\n		<xsl:apply-templates select=\"page-description\" />\r\n		<xsl:apply-templates select=\"child-pages-list\" />\r\n	</li>\r\n</ul>\r\n</xsl:template>\r\n\r\n\r\n<xsl:template match=\"page[page-level>2]\" >\r\n<ul class=\"site-map-level-highest\">\r\n	<li>\r\n		<a href=\"{$site-path}?page_id={page-id}\" target=\"_top\">\r\n			<xsl:value-of select=\"page-name\" />\r\n		</a>\r\n		<xsl:apply-templates select=\"page-description\" />\r\n		<xsl:apply-templates select=\"child-pages-list\" />\r\n	</li>\r\n</ul>\r\n</xsl:template>\r\n\r\n\r\n<xsl:template match=\"page-description\">\r\n	<br /><xsl:value-of select=\".\" />\r\n</xsl:template>\r\n\r\n\r\n<xsl:template match=\"child-pages-list[page-level=0]\">\r\n	<xsl:if test=\"count(page)>0\" >\r\n		<xsl:apply-templates select=\"page\" />\r\n    </xsl:if>\r\n</xsl:template>\r\n\r\n\r\n<xsl:template match=\"child-pages-list[page-level=1]\">\r\n	<xsl:if test=\"count(page)>0\" >\r\n		<xsl:apply-templates select=\"page\" />\r\n    </xsl:if>\r\n</xsl:template>\r\n\r\n\r\n<xsl:template match=\"child-pages-list[page-level=2]\">\r\n	<xsl:if test=\"count(page)>0\" >\r\n		<xsl:apply-templates select=\"page\" />\r\n    </xsl:if>\r\n</xsl:template>\r\n\r\n<xsl:template match=\"child-pages-list[page-level>2]\">\r\n	<xsl:if test=\"count(page)>0\" >\r\n		<xsl:apply-templates select=\"page\" />\r\n    </xsl:if>\r\n</xsl:template>\r\n\r\n\r\n<xsl:template match=\"page-image\">\r\n	<div class=\"level-one-image\">\r\n    	<div class=\"polaroid\">\r\n		<img  border=\"0\" width=\"80\" height=\"80\" src=\"images/local/data/pages/{.}\" alt=\"\" />\r\n         </div>\r\n	</div >\r\n</xsl:template>\r\n\r\n\r\n</xsl:stylesheet>\r\n'),
 (279,'Plan du Site module d\'Administration','admin_site_map_admin.xsl','<?xml version=\"1.0\"?>\r\n<xsl:stylesheet version=\"1.0\" xmlns:xsl=\"http://www.w3.org/1999/XSL/Transform\">\r\n    \r\n    <xsl:param name=\"site-path\" select=\"site-path\" />\r\n    <xsl:variable name=\"current-page-id\">\r\n        <xsl:value-of select=\"current-page-id\" />\r\n    </xsl:variable>\r\n    \r\n    \r\n    <xsl:template match=\"page[page-level=0]\">\r\n   \r\n      <div id=\"admin-site-menu\">\r\n                        <ul>\r\n                            <li>\r\n                                <a href=\"{$site-path}?page_id={current-page-id}\">#i18n{portal.site.admin_page.tabPageManagement} <xsl:value-of select=\"current-page-id\" /></a>\r\n                            </li>\r\n                            <li id=\"current\">\r\n                                <a href=\"{$site-path}?page_id={current-page-id}\">#i18n{portal.site.admin_page.tabAdminMapSite}</a>\r\n                            </li>\r\n                        </ul>\r\n       </div>\r\n        \r\n        <h3><img src=\"js/jquery/plugins/treeview/images/folder.gif\" style=\"margin-bottom:-10px;\"/></h3>\r\n        <ul id=\"tree\" class=\"treeview\">\r\n            <li>\r\n                <span>\r\n                  <a href=\"{$site-path}?page_id={page-id}\">\r\n                    <xsl:value-of select=\"page-name\" />\r\n                  </a>\r\n                </span>\r\n                <xsl:if test=\"not(string(page-role)=\'none\')\"><br />\r\n                    <strong>\r\n                        <xsl:text disable-output-escaping=\"yes\">\r\n                            #i18n{portal.site.admin_page.tabAdminMapRoleReserved}\r\n                        </xsl:text>\r\n                    </strong>\r\n                    <xsl:value-of select=\"page-role\" />\r\n                </xsl:if>\r\n                \r\n                <xsl:if test=\"not(string(page-description)=\'\')\">\r\n                    <br />\r\n                    <strong>\r\n                        <xsl:text disable-output-escaping=\"yes\">\r\n                            #i18n{portal.site.admin_page.tabAdminMapDescription}\r\n                        </xsl:text>\r\n                    </strong>\r\n                    <xsl:apply-templates select=\"page-description\" />\r\n                </xsl:if>\r\n                <ul>\r\n                   <xsl:apply-templates select=\"child-pages-list\" />\r\n                </ul>\r\n            </li>\r\n        </ul>\r\n    </xsl:template>\r\n    \r\n    <xsl:template match=\"page[page-level>0]\" >\r\n        <li>\r\n        <span>\r\n            <a href=\"{$site-path}?page_id={page-id}\">\r\n                <xsl:value-of select=\"page-name\" />\r\n            </a>\r\n          </span>  \r\n            <xsl:if test=\"not(string(page-role)=\'none\')\"><br />\r\n                <strong>\r\n                    <xsl:text disable-output-escaping=\"yes\">\r\n                        #i18n{portal.site.admin_page.tabAdminMapRoleReserved}\r\n                    </xsl:text>\r\n                </strong> \r\n                <xsl:value-of select=\"page-role\" />\r\n            </xsl:if>\r\n            \r\n            <xsl:if test=\"not(string(page-description)=\'\')\">\r\n                <br />\r\n                <strong>\r\n                    <xsl:text disable-output-escaping=\"yes\">\r\n                        #i18n{portal.site.admin_page.tabAdminMapDescription}\r\n                    </xsl:text>\r\n                </strong>\r\n                <xsl:apply-templates select=\"page-description\" />\r\n            </xsl:if>\r\n            \r\n            <xsl:choose>\r\n                <xsl:when test=\"count(child-pages-list/*)>0\">\r\n                    <ul>\r\n                        <xsl:apply-templates select=\"child-pages-list\" />\r\n                    </ul>\r\n                </xsl:when>\r\n                <xsl:otherwise>\r\n                    <xsl:apply-templates select=\"child-pages-list\" />\r\n                </xsl:otherwise>\r\n            </xsl:choose>\r\n        </li>\r\n    </xsl:template>\r\n    \r\n    <xsl:template match=\"page-description\">\r\n        <xsl:value-of select=\".\" />\r\n    </xsl:template>\r\n    \r\n    <xsl:template match=\"child-pages-list\">\r\n        <xsl:apply-templates select=\"page\" />\r\n    </xsl:template>\r\n    \r\n</xsl:stylesheet>\r\n');
/*!40000 ALTER TABLE `core_stylesheet` ENABLE KEYS */;


--
-- Dumping data for table `core_user_right`
--

/*!40000 ALTER TABLE `core_user_right` DISABLE KEYS */;
INSERT INTO `core_user_right` (`id_right`,`id_user`) VALUES 
 ('CORE_ADMIN_SITE',1),
 ('CORE_ADMIN_SITE',2),
 ('CORE_CACHE_MANAGEMENT',1),
 ('CORE_DAEMONS_MANAGEMENT',1),
 ('CORE_FEATURES_MANAGEMENT',1),
 ('CORE_LOGS_VISUALISATION',1),
 ('CORE_MODES_MANAGEMENT',1),
 ('CORE_PAGE_TEMPLATE_MANAGEMENT',1),
 ('CORE_PAGE_TEMPLATE_MANAGEMENT',2),
 ('CORE_PLUGINS_MANAGEMENT',1),
 ('CORE_PROPERTIES_MANAGEMENT',1),
 ('CORE_PROPERTIES_MANAGEMENT',2),
 ('CORE_RBAC_MANAGEMENT',1),
 ('CORE_SEARCH_INDEXATION',1),
 ('CORE_SEARCH_INDEXATION',2),
 ('CORE_STYLES_MANAGEMENT',1),
 ('CORE_STYLESHEET_MANAGEMENT',1),
 ('CORE_USERS_MANAGEMENT',1),
 ('CORE_USERS_MANAGEMENT',2),
 ('CORE_WORKGROUPS_MANAGEMENT',1),
 ('CORE_WORKGROUPS_MANAGEMENT',2),
 ('CORE_ROLES_MANAGEMENT',1),
 ('CORE_ROLES_MANAGEMENT',2),
 ('CORE_GROUPS_MANAGEMENT',1),
 ('CORE_GROUPS_MANAGEMENT',2),
 ('CORE_MAILINGLISTS_MANAGEMENT',1),
 ('CORE_LEVEL_RIGHT_MANAGEMENT',1);
/*!40000 ALTER TABLE `core_user_right` ENABLE KEYS */;


--
-- Dumping data for table `core_user_role`
--

/*!40000 ALTER TABLE `core_user_role` DISABLE KEYS */;
INSERT INTO `core_user_role` (`role_key`,`id_user`) VALUES 
 ('all_site_manager',1),
 ('all_site_manager',2),
 ('super_admin',1);
/*!40000 ALTER TABLE `core_user_role` ENABLE KEYS */;




/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
