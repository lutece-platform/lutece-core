

INSERT INTO core_admin_auth_db_module VALUES ('admin','admin','2006-07-21','x','admin','admin','admin@lutece.fr');
INSERT INTO core_admin_auth_db_module VALUES ('lutece','lutece','2006-07-21','x','lutece','lutece','lutece@lutece.fr');
INSERT INTO core_admin_auth_db_module VALUES ('redac','redac','2006-07-21','x','redac','redac','redac@lutece.fr');
INSERT INTO core_admin_auth_db_module VALUES ('valid','valid','2006-07-21','x','valid','valid','valid@lutece.fr');



INSERT INTO core_admin_right VALUES ('CORE_ADMIN_SITE','portal.site.adminFeature.admin_site.name',2,'jsp/admin/site/AdminSite.jsp','portal.site.adminFeature.admin_site.description',1,NULL,'SITE','images/admin/skin/features/admin_site.png','jsp/admin/documentation/AdminDocumentation.jsp?doc=admin-site',1);
INSERT INTO core_admin_right VALUES ('CORE_CACHE_MANAGEMENT','portal.system.adminFeature.cache_management.name',0,'jsp/admin/system/ManageCaches.jsp','portal.system.adminFeature.cache_management.description',1,NULL,'SYSTEM','images/admin/skin/features/manage_caches.png',NULL,1);
INSERT INTO core_admin_right VALUES ('CORE_SEARCH_INDEXATION','portal.search.adminFeature.indexer.name',0,'jsp/admin/search/ManageSearchIndexation.jsp','portal.search.adminFeature.indexer.description',0,NULL,'SYSTEM',NULL,NULL,2);
INSERT INTO core_admin_right VALUES ('CORE_LOGS_VISUALISATION','portal.system.adminFeature.logs_visualisation.name',0,'jsp/admin/system/ManageFilesSystem.jsp','portal.system.adminFeature.logs_visualisation.description',1,NULL,'SYSTEM','images/admin/skin/features/view_logs.png',NULL,3);
INSERT INTO core_admin_right VALUES ('CORE_MODES_MANAGEMENT','portal.style.adminFeature.modes_management.name',0,'jsp/admin/style/ManageModes.jsp','portal.style.adminFeature.modes_management.description',1,NULL,'STYLE','images/admin/skin/features/manage_modes.png',NULL,1);
INSERT INTO core_admin_right VALUES ('CORE_PAGE_TEMPLATE_MANAGEMENT','portal.style.adminFeature.page_template_management.name',0,'jsp/admin/style/ManagePageTemplates.jsp','portal.style.adminFeature.page_template_management.description',0,NULL,'STYLE','images/admin/skin/features/manage_page_templates.png',NULL,2);
INSERT INTO core_admin_right VALUES ('CORE_PLUGINS_MANAGEMENT','portal.system.adminFeature.plugins_management.name',0,'jsp/admin/system/ManagePlugins.jsp','portal.system.adminFeature.plugins_management.description',1,NULL,'SYSTEM','images/admin/skin/features/manage_plugins.png',NULL,4);
INSERT INTO core_admin_right VALUES ('CORE_PROPERTIES_MANAGEMENT','portal.site.adminFeature.properties_management.name',2,'jsp/admin/ManageProperties.jsp','portal.site.adminFeature.properties_management.description',0,NULL,'SITE',NULL,'jsp/admin/documentation/AdminDocumentation.jsp?doc=admin-properties',2);
INSERT INTO core_admin_right VALUES ('CORE_STYLESHEET_MANAGEMENT','portal.style.adminFeature.stylesheet_management.name',0,'jsp/admin/style/ManageStyleSheets.jsp','portal.style.adminFeature.stylesheet_management.description',1,NULL,'STYLE','images/admin/skin/features/manage_stylesheets.png',NULL,3);
INSERT INTO core_admin_right VALUES ('CORE_STYLES_MANAGEMENT','portal.style.adminFeature.styles_management.name',0,'jsp/admin/style/ManageStyles.jsp','portal.style.adminFeature.styles_management.description',1,NULL,'STYLE','images/admin/skin/features/manage_styles.png',NULL,4);
INSERT INTO core_admin_right VALUES ('CORE_USERS_MANAGEMENT','portal.users.adminFeature.users_management.name',2,'jsp/admin/user/ManageUsers.jsp','portal.users.adminFeature.users_management.description',1,'','MANAGERS','images/admin/skin/features/manage_users.png',NULL,1);
INSERT INTO core_admin_right VALUES ('CORE_FEATURES_MANAGEMENT','portal.admin.adminFeature.features_management.name',0,'jsp/admin/features/ManageFeatures.jsp','portal.admin.adminFeature.features_management.description',0,NULL,'SYSTEM','images/admin/skin/features/manage_features.png',NULL,5);
INSERT INTO core_admin_right VALUES ('CORE_RBAC_MANAGEMENT','portal.rbac.adminFeature.rbac_management.name',0,'jsp/admin/rbac/ManageRoles.jsp','portal.rbac.adminFeature.rbac_management.description',0,'','MANAGERS','images/admin/skin/features/manage_rbac.png',NULL,2);
INSERT INTO core_admin_right VALUES ('CORE_DAEMONS_MANAGEMENT','portal.system.adminFeature.daemons_management.name',0,'jsp/admin/system/ManageDaemons.jsp','portal.system.adminFeature.daemons_management.description',0,NULL,'SYSTEM',NULL,NULL,6);
INSERT INTO core_admin_right VALUES ('CORE_WORKGROUPS_MANAGEMENT','portal.workgroup.adminFeature.workgroups_management.name',2,'jsp/admin/workgroup/ManageWorkgroups.jsp','portal.workgroup.adminFeature.workgroups_management.description',0,NULL,'MANAGERS','images/admin/skin/features/manage_workgroups.png',NULL,3);
INSERT INTO core_admin_right VALUES ('CORE_ROLES_MANAGEMENT','portal.role.adminFeature.roles_management.name',2,'jsp/admin/role/ManagePageRole.jsp','portal.role.adminFeature.roles_management.description',0,NULL,'USERS','images/admin/skin/features/manage_roles.png',NULL,1);
INSERT INTO core_admin_right VALUES ('CORE_GROUPS_MANAGEMENT','portal.group.adminFeature.groups_management.name',2,'jsp/admin/group/ManageGroups.jsp','portal.group.adminFeature.groups_management.description',0,NULL,'USERS','images/admin/skin/features/manage_groups.png',NULL,2);
INSERT INTO core_admin_right VALUES ('CORE_MAILINGLISTS_MANAGEMENT','portal.mailinglist.adminFeature.mailinglists_management.name',2,'jsp/admin/mailinglist/ManageMailingLists.jsp','portal.mailinglist.adminFeature.mailinglists_management.description',0,NULL,'MANAGERS','images/admin/skin/features/manage_mailinglists.png',NULL,4);
INSERT INTO core_admin_right VALUES ('CORE_LEVEL_RIGHT_MANAGEMENT','portal.users.adminFeature.level_right_management.name',2,'jsp/admin/features/ManageLevels.jsp','portal.users.adminFeature.level_right_management.description',0,NULL,'MANAGERS','images/admin/skin/features/manage_rights_levels.png',NULL,5);
INSERT INTO core_admin_right VALUES ('CORE_THEMES_MANAGEMENT','portal.style.adminFeature.themes_management.name',0,'jsp/admin/style/ManageThemes.jsp','portal.style.adminFeature.themes_management.description',1,NULL,'STYLE','images/admin/skin/features/manage_styles.png',NULL,4);

INSERT INTO core_admin_role VALUES ('all_site_manager','Site Manager');
INSERT INTO core_admin_role VALUES ('super_admin','Super Administrateur');
INSERT INTO core_admin_role VALUES ('assign_roles','Assigner des roles aux utilisateurs');
INSERT INTO core_admin_role VALUES ('assign_groups','Assigner des groupes aux utilisateurs');

INSERT INTO core_admin_role_resource VALUES (57,'all_site_manager','PAGE','*','VIEW');
INSERT INTO core_admin_role_resource VALUES (58,'all_site_manager','PAGE','*','MANAGE');
INSERT INTO core_admin_role_resource VALUES (77,'super_admin','INSERT_SERVICE','*','*');
INSERT INTO core_admin_role_resource VALUES (101,'all_site_manager','PORTLET_TYPE','*','*');
INSERT INTO core_admin_role_resource VALUES (205,'assign_roles','ROLE_TYPE','*','ASSIGN_ROLE');
INSERT INTO core_admin_role_resource VALUES (206,'assign_groups','GROUP_TYPE','*','ASSIGN_GROUP');

INSERT INTO core_admin_user VALUES (1,'admin','Admin','admin','',0,'admin','fr',0);
INSERT INTO core_admin_user VALUES (2,'lutece','Lutèce','lutece','',0,'lutece','fr',1);
INSERT INTO core_admin_user VALUES (3,'redac','redac','redac','redac@mdp',0,'redac','fr',2);
INSERT INTO core_admin_user VALUES (4,'valid','valid','valid','valid@mdp',0,'valid','fr',3);




INSERT INTO core_feature_group VALUES ('CONTENT','portal.features.group.content.description','portal.features.group.content.label',1);
INSERT INTO core_feature_group VALUES ('APPLICATIONS','portal.features.group.applications.description','portal.features.group.applications.label',3);
INSERT INTO core_feature_group VALUES ('SYSTEM','portal.features.group.system.description','portal.features.group.system.label',7);
INSERT INTO core_feature_group VALUES ('SITE','portal.features.group.site.description','portal.features.group.site.label',2);
INSERT INTO core_feature_group VALUES ('STYLE','portal.features.group.charter.description','portal.features.group.charter.label',6);
INSERT INTO core_feature_group VALUES ('USERS','portal.features.group.users.description','portal.features.group.users.label',4);
INSERT INTO core_feature_group VALUES ('MANAGERS','portal.features.group.managers.description','portal.features.group.managers.label',5);



INSERT INTO core_level_right VALUES (0,'Niveau 0 - Droits de l\'administrateur technique');
INSERT INTO core_level_right VALUES (1,'Niveau 1 - Droits de l\'administrateur fonctionnel');
INSERT INTO core_level_right VALUES (2,'Niveau 2 - Droits du webmestre');
INSERT INTO core_level_right VALUES (3,'Niveau 3 - Droits de l\'assistant webmestre');


INSERT INTO core_mode VALUES (0,'Normal','normal/','xml','1.0','text/xml','UTF-8','yes','yes','yes');
INSERT INTO core_mode VALUES (1,'Administration','admin/','xml','1.0','text/xml','UTF-8','yes','yes','yes');
INSERT INTO core_mode VALUES (2,'Wap','wml/','xml','1.0','text/xml','UTF-8','yes','yes','yes');

INSERT INTO core_page VALUES (1,0,'accueil','Page d\'accueil','2009-05-02 00:19:33',1,1,2,'2003-09-09 06:38:01','none','default',0,'','application/octet-stream','all');
INSERT INTO core_page VALUES (3,1,'Documentation','Tout ce dont vous avez besoin pour utiliser Lutece','2006-10-18 15:39:24',0,2,2,'2002-09-09 06:46:46','none','default',0,'','application/octet-stream','all');
INSERT INTO core_page VALUES (6,3,'Guide utilisateur','Accès au guide utilisateur','2006-09-19 14:20:13',0,1,1,'2006-02-15 16:39:59','none','default',1,NULL,'','all');
INSERT INTO core_page VALUES (5,1,'L\'outil','Description du CMS Lutèce','2006-10-12 15:03:49',0,1,1,'2006-02-15 16:37:26','none','default',1,NULL,'','all');
INSERT INTO core_page VALUES (7,3,'Guide technique','Accès à documentation technique','2006-09-19 14:19:45',0,2,1,'2006-02-15 16:40:30','none','default',1,0x47494638396130003000C3B700000402047CE2809AEFBFBD3F44424454527CC384C382C2BCC2A4C2A2C2A46C6AE2809E5C5E5C2422243C426CC3A4C3A2C39C746EEFBFBD3FEFBFBD3FE28099EFBFBD3F343234C2B4C2B2C2B4545264C394C392C38C1412145452545C5E7C7C7A7CC3B4C3B2C3ACC2A4C2A2C2B4EFBFBD3FE28099C2AC3432444442542C2A2C7C7AEFBFBD3FE2809EE2809AC2A46466C5927472E2809E4C4E64C2BCC2BAC2BC545274C39CC39AC39C0D0A0A0D0AC384C386C38C747674C3A4C3A6C3ACC592E28099C2A41C1A1C64667CC2ACC2AAC2AC6C6AC5925C5E747476C592C3BCC3BAC3B4C2ACC2AAC2B43C3A444C4A4C545A7C2426347476EFBFBD3F3C3A3C5C5A64C593C5BEC2AC4C4A542C2A347C7EC2A4C592C5BDC2A4444A74C592C5A0C593C2A4C2A2C2AC24222C7472C593C2B4C2B6C384C39CC39AC3945C5A5C5C5EE2809E7C7AC593C2BCC2BAC384C39CC39EC3A40D0A0E14C38CC38EC38CC3ACC3AEC3ACC593C5A1C2A41C1A246C6A6CC2ACC2AAC2BC04060D0AE2809EE280A0C59344424CC384C382C3846C6EE2809E44466CC3A4C3A2C3A46C72EFBFBD3FC593C5A1C59334323CC2B4C2B2C2BC54526CC394C392C39414161C54525C7C7E7CC3B4C3B2C3B4EFBFBD3FE28099C2B43436445C5A74C39CC39EC39CEFBFBD3FE28093C2A46466E2809E6C6EC592646274747AC592C3BCC3BAC3BC3C3A4C5C5AE2809E4C4A5C2C2E34C592C5A0C2AC4C4E74040604E2809EE2809AEFBFBD3F44464454567CC2A4C2A6C2A464666424262444426CEFBFBD3FE28093EFBFBD3F343634C2B4C2B6C2B45C566414161454565464627C7476E2809EC2A4C2A6C2B44C4A6C7472C5925C5A6CC593C5BEC2B4C38CC38EC394C3ACC3AEC3B4C38CC38AC38CC3ACC3AAC3AC6462E2809EEFBFBD3FE28093C2AC4446542C2E2C7C7EEFBFBD3FE2809EE280A0C2A4C2BCC2BEC2BC5456740D0A0E0D0A1C1E1CC2ACC2AEC2ACC2ACC2AEC2B44C4E4C3C3E3CC2A4C2A6C2AC24262C7C7EC593C2BCC2BEC3841C1E24C2ACC2AEC2BCC384C386C384C3A4C3A6C3A4C394C396C394C3B4C3B6C3B4C3BCC3BEC3BC3C3E4C4C4E5CC592C5BDC2AC6C66C592EFBFBD3FE28099C2A46C667C645E747C76C5925C5A7C4C4A74140E14746EE2809E7472EFBFBD3F3C3644C3A4C39EC39C746EC592342E347C7CC3BF00704800C3800015120000000000C382C2A0002AC3AE00E2809A12007C0020000060000015000000000000C3A8780152EFBFBD3F00161200000000C2BE08003EC2B500E2809A4B007C00577D58C3B604EFBFBD3FE282AC00127C00005C74C3B3C380EFBFBD3FC2B212124E00000020C3B7C592603EEFBFBD3F15E2809A12007C006904C2BE5CC3B03E4D4EE2809A65007C730D0A5020C3B0C384644E126F00006300C2B67501C3BF6D00C3BF65007F6E700874C380C3847312125C00004D00C3A86501527300162000006916C2BE6D3F3E61E2809AE2809A677C7C6500C3A87300525C00160000000000000000000100000000000100C2AF000049020047000000C2A0C5BE080001C384001E120001001F00080000C38400001200000021C3B90401000000002C00000000300030000708C3BF0001081C48C2B0C2A0EFBFBD3FC69208132A5CC388C2B0C2A1C383E280A110234AC59348C2B1C2A2C385E2809A58305DC2ACCB86644F070F7D2830685262C2A3C3830606CB86C3B410C2A20454C5A134C2A13AC3B131E284A2C2B063E280A13259EFBFBD3F78320133EFBFBD3FC2A13135681E6CC2B06242C5934F15C2AA7C09C385C2B4C38A2310C592E2809E121C61C2A3C3A80D0A25CB863CC3B54CC3B309E2809EC5BE470DC2A40A6C2368EFBFBD3F20C2A45510310DC2A5C2A4C393531050C2A5525D00C3A906C39623C2A0CB9CC2A6EFBFBD3F2545C393233DE282AC35086DC393E2809A48E284A242C2A0C392C2AEC3BDC2B245C5A01402701DC3A811C2BC71C3841E1A217628C2A9306669C2A834C5BEE2809974C3AA24C3A5C2A91EC389EFBFBD3F2D626AC39141E280A211174A44C3A80525C3A2EFBFBD3F21C392EFBFBD3FC3BEC3AAE284A2C3A4C380C38E460FE280A0C2B728EFBFBD3FC2A9C39405C2A22D49E28099C39C2EC3ADC395EFBFBD3FC2A4490836C3A2486440C38416C2AC55EFBFBD3F54C39902C2A1EFBFBD3F72C388201C38C3BF5031C389C3B7C3851CC2AE0275C3BAC3A409E2809802C3B75BEFBFBD3F2F37C3ADC593C2BCC2A4C38A2DC3AE00C3BA54C385535A05C3B2EFBFBD3FC3A65773E2809CC2A8C2A042741BEFBFBD3F11C38206C5B8C592EFBFBD3F487F4704C388C39C69EFBFBD3FC391C2A1C3827D1B45C391C39A2122C3B8C3A7092CC38A09485FEFBFBD3F1662781102E2809A50E28098E280A61026C592C3A1C38918214E58C5B80A16C2A260121C065071027F555431E280A06DC2B8114823EFBFBD3FE28099C380EFBFBD3F0202C5926021400D0A120D0A31C38401434860C3894C0A0D32C38714101CC2A14015C2B50509C3987334C392C3B115E280A1C3AD55C3A0C3925A68C2AAC2B0500D1D0422E280A602631CC3B1C3896832567809085B74CB9C15227C22C2A2E2809E125F7CE2809857270EC3A4C2A1101F66C39CC3A1032C47C5922102697EEFBFBD3FC3A6C593E280A6EFBFBD3F24EFBFBD3F5F7BC3ADC3B95941C2A0671E5200085E2CC3A44720EFBFBD3FC59261C3AA27C5BDEFBFBD3F48C5BE14E28093C3826242C2A67E06C3BF2A284CE280A1EFBFBD3FC3A1C5931E7FC2B0C3800542C2A3C38C31EFBFBD3F16C2A63646E282AC6EC3A3C3A921C385215B1CC3A1C389C2B27E6EC38629C2AD743CC3A7002517C2B0E28098501B655001022CC3963DC2A209EFBFBD3FEFBFBD3F48C39109C2B0C38CC3B2C3B927C2A779C395C38AE280BA783B68C2A1502406C39C71C692C2A35BC3A476C39A57C3A226C39121C2A6EFBFBD3F3E1B4AC2ADC38E49664A19702C34080F2DC592C3B14921C2A6EFBFBD3FC3A0CB9C1410CB86EFBFBD3F5FC2A64A18C2B2C2A9C2A0C3BF1620C2AD1E64C2B8C2B2C3AB42C2A2C2AC21E280A0080A240157C2B804C2B07A29C2ACC38E7E011309C3914E42E280B01EC5A014C2A111430864E280BA0925E280A17CE280A2E280BA26E280A17CC2A2C2AC09C3A61AC3A26F271AC38BC2AC47013AC380C3A0101C2CE2809E10C3881B3B702BE2809A0863C3B4C2B8C3ACC3965C7BC2A2E2809EC2A7EFBFBD3F4DC2A2C387251CC59202510E2BC388EFBFBD3FCB861526E282ACC3B26756C38BC3B6C3A8C2A3C2A9577F22E2809A1EC3A44946360D0A12E280B0C3BFC3A2C5A0150D0A0421E2809AC2B2E284A2C3BA67C2AAC2A357E280B960C2B714C3A4C3890D0AE280A619E280984CEFBFBD3F431945C3A8C3A00829C5A010C382E280B011241472C3B5C3A16308C3B1C3892749C2AC0D0A45114C58C2B40916C2A2C2B021E280A01F6600E2809809073B64C3AE4416C5A1EFBFBD3FE282ACC3AC160438EFBFBD3FC6920E6D4804070D0AE280981B0447245C60E2809A45061FCB8651E280A01945EFBFBD3F7E01214EC3ACC3804106126DC3924A2213EFBFBD3FEFBFBD3FC380080D0AEFBFBD3FEFBFBD3F040ACB9CE2809EEFBFBD3FC2BC18E2809A3C20C3912234C38CC391CB8627C2A1C3B8C2B8E280A61E57C39421E280A10628E282ACC2BF50EFBFBD3F10C2A1EFBFBD3FC5A0197378EFBFBD3FC39B44EFBFBD3FE2809C507046044978C38424C2AEC3A0E280A63A48200FC3BAC2B3482BC38A30EFBFBD3F4328C3A00D0AC3BE71C38971E282AC44C5A1C387EFBFBD3FC3A629C3A6C2B14817CB8630E280A6315CC3871347EFBFBD3F090424C3B4E2809349E280A267236D480F093EEFBFBD3FC2A7C3A1CB9CEFBFBD3FE280A65EC39103250A64C2A2C5A044E2809A065478EFBFBD3F101FC392C3A2C2A0C3AD24C2A7C692C2A601C393EFBFBD3F4CC2A2C2A04C34C3883D265040216244C5B8C3B1EFBFBD3FC2A8220101003B,'image/gif','all');
INSERT INTO core_page VALUES (10,1,'Développeurs','Ressources pour les contributeurs/développeurs Lutèce','2009-05-02 00:25:13',0,4,2,'2006-10-12 15:03:20','none','default',1,NULL,NULL,'all');

INSERT INTO core_page_template VALUES (1,'Une colonne','skin/site/page_template1.html','page_template1.gif');
INSERT INTO core_page_template VALUES (2,'Deux colonnes','skin/site/page_template2.html','page_template2.gif');
INSERT INTO core_page_template VALUES (3,'Trois colonnes','skin/site/page_template3.html','page_template3.gif');
INSERT INTO core_page_template VALUES (4,'1 + 2 colonnes','skin/site/page_template4.html','page_template4.gif');
INSERT INTO core_page_template VALUES (5,'Deux colonnes égales','skin/site/page_template5.html','page_template5.gif');
INSERT INTO core_page_template VALUES (6,'Trois colonnes inégales','skin/site/page_template6.html','page_template6.gif');

INSERT INTO core_parameters VALUES (120);

INSERT INTO core_portal_component VALUES (0,'Rubrique');
INSERT INTO core_portal_component VALUES (1,'Article');
INSERT INTO core_portal_component VALUES (2,'Rubrique Liste Article');
INSERT INTO core_portal_component VALUES (3,'Menu Init');
INSERT INTO core_portal_component VALUES (4,'Menu Principal');
INSERT INTO core_portal_component VALUES (5,'Chemin Page');
INSERT INTO core_portal_component VALUES (6,'Plan du site');
INSERT INTO core_portal_component VALUES (7,'Arborescence');
INSERT INTO core_portal_component VALUES (8,'Plan du site admin');

INSERT INTO core_portlet VALUES (85,'CHILDPAGES_PORTLET',5,'Pages filles','2007-11-24 17:15:10',0,1,5,303,0,'2007-11-24 17:14:58',1);
INSERT INTO core_portlet VALUES (87,'CHILDPAGES_PORTLET',3,'Pages filles','2007-11-24 17:21:01',0,1,5,302,0,'2007-11-24 17:19:50',1);
INSERT INTO core_portlet VALUES (88,'CHILDPAGES_PORTLET',10,'Pages filles','2007-11-24 17:20:37',0,1,5,305,0,'2007-11-24 17:20:37',1);
INSERT INTO core_portlet VALUES (83,'CHILDPAGES_PORTLET',1,'Pages filles','2007-11-24 16:11:33',0,1,5,305,0,'2007-11-24 16:11:33',1);
INSERT INTO core_portlet VALUES (56,'HTML_PORTLET',1,'Comptes d\'administration du portail','2009-05-02 00:18:43',0,1,2,104,0,'2007-03-14 16:15:26',0);
INSERT INTO core_portlet VALUES (57,'HTML_PORTLET',5,'Le projet','2009-05-02 00:28:13',0,2,1,104,0,'2007-03-14 16:17:30',0);
INSERT INTO core_portlet VALUES (55,'HTML_PORTLET',1,'Lutèce v2 - Sinnamary','2009-05-02 00:19:09',0,1,1,104,0,'2007-03-14 16:13:39',0);
INSERT INTO core_portlet VALUES (86,'HTML_PORTLET',3,'Bannière','2007-11-24 17:19:18',0,1,1,100,0,'2007-11-24 17:19:18',1);
INSERT INTO core_portlet VALUES (64,'HTML_PORTLET',10,'Infos développeurs','2009-05-02 00:23:47',0,1,1,104,0,'2007-03-14 16:45:14',0);
INSERT INTO core_portlet VALUES (65,'HTML_PORTLET',10,'Liens développeurs','2009-05-02 00:24:58',0,1,2,104,0,'2007-03-14 16:49:18',0);
INSERT INTO core_portlet VALUES (58,'HTML_PORTLET',3,'Accès à la documentation technique','2007-11-24 17:19:26',0,2,1,100,0,'2007-03-14 16:20:08',1);


INSERT INTO core_portlet_type VALUES ('ALIAS_PORTLET','portal.site.portletAlias.name','plugins/alias/CreatePortletAlias.jsp','plugins/alias/ModifyPortletAlias.jsp','fr.paris.lutece.portal.business.portlet.AliasPortletHome','alias','plugins/alias/DoCreatePortletAlias.jsp','/admin/portlet/script_create_portlet.html','/admin/portlet/alias/create_portlet_alias.html','','plugins/alias/DoModifyPortletAlias.jsp','/admin/portlet/script_modify_portlet.html','/admin/portlet/alias/modify_portlet_alias.html','');


INSERT INTO core_style VALUES (3,'Menu Init','',3);
INSERT INTO core_style VALUES (4,'Main Menu','',4);
INSERT INTO core_style VALUES (5,'Chemin Page','',5);
INSERT INTO core_style VALUES (6,'Plan du site','',6);
INSERT INTO core_style VALUES (7,'Arborescence','',7);
INSERT INTO core_style VALUES (8,'Plan du Site Admin',NULL,8);

INSERT INTO core_style_mode_stylesheet VALUES (3,0,211);
INSERT INTO core_style_mode_stylesheet VALUES (4,0,213);
INSERT INTO core_style_mode_stylesheet VALUES (5,0,215);
INSERT INTO core_style_mode_stylesheet VALUES (6,0,217);
INSERT INTO core_style_mode_stylesheet VALUES (7,0,253);
INSERT INTO core_style_mode_stylesheet VALUES (8,1,279);


INSERT INTO core_user_right VALUES ('CORE_ADMIN_SITE',1);
INSERT INTO core_user_right VALUES ('CORE_ADMIN_SITE',2);
INSERT INTO core_user_right VALUES ('CORE_CACHE_MANAGEMENT',1);
INSERT INTO core_user_right VALUES ('CORE_DAEMONS_MANAGEMENT',1);
INSERT INTO core_user_right VALUES ('CORE_FEATURES_MANAGEMENT',1);
INSERT INTO core_user_right VALUES ('CORE_GROUPS_MANAGEMENT',1);
INSERT INTO core_user_right VALUES ('CORE_GROUPS_MANAGEMENT',2);
INSERT INTO core_user_right VALUES ('CORE_LEVEL_RIGHT_MANAGEMENT',1);
INSERT INTO core_user_right VALUES ('CORE_LOGS_VISUALISATION',1);
INSERT INTO core_user_right VALUES ('CORE_MAILINGLISTS_MANAGEMENT',1);
INSERT INTO core_user_right VALUES ('CORE_MODES_MANAGEMENT',1);
INSERT INTO core_user_right VALUES ('CORE_PAGE_TEMPLATE_MANAGEMENT',1);
INSERT INTO core_user_right VALUES ('CORE_PAGE_TEMPLATE_MANAGEMENT',2);
INSERT INTO core_user_right VALUES ('CORE_PLUGINS_MANAGEMENT',1);
INSERT INTO core_user_right VALUES ('CORE_PROPERTIES_MANAGEMENT',1);
INSERT INTO core_user_right VALUES ('CORE_PROPERTIES_MANAGEMENT',2);
INSERT INTO core_user_right VALUES ('CORE_RBAC_MANAGEMENT',1);
INSERT INTO core_user_right VALUES ('CORE_ROLES_MANAGEMENT',1);
INSERT INTO core_user_right VALUES ('CORE_ROLES_MANAGEMENT',2);
INSERT INTO core_user_right VALUES ('CORE_SEARCH_INDEXATION',1);
INSERT INTO core_user_right VALUES ('CORE_SEARCH_INDEXATION',2);
INSERT INTO core_user_right VALUES ('CORE_STYLES_MANAGEMENT',1);
INSERT INTO core_user_right VALUES ('CORE_STYLESHEET_MANAGEMENT',1);
INSERT INTO core_user_right VALUES ('CORE_THEMES_MANAGEMENT',1);
INSERT INTO core_user_right VALUES ('CORE_USERS_MANAGEMENT',1);
INSERT INTO core_user_right VALUES ('CORE_USERS_MANAGEMENT',2);
INSERT INTO core_user_right VALUES ('CORE_WORKGROUPS_MANAGEMENT',1);
INSERT INTO core_user_right VALUES ('CORE_WORKGROUPS_MANAGEMENT',2);

INSERT INTO core_user_role VALUES ('all_site_manager',1);
INSERT INTO core_user_role VALUES ('all_site_manager',2);
INSERT INTO core_user_role VALUES ('assign_groups',1);
INSERT INTO core_user_role VALUES ('assign_groups',2);
INSERT INTO core_user_role VALUES ('assign_groups',3);
INSERT INTO core_user_role VALUES ('assign_roles',1);
INSERT INTO core_user_role VALUES ('assign_roles',2);
INSERT INTO core_user_role VALUES ('assign_roles',3);


