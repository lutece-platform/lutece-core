ALTER TABLE core_style MODIFY COLUMN description_style VARCHAR(100) NOT NULL;

INSERT INTO core_admin_right VALUES ('CORE_LINK_SERVICE_MANAGEMENT','portal.insert.adminFeature.linkService_management.name',2,NULL,'portal.insert.adminFeature.linkService_management.description',0,NULL,NULL,NULL,NULL,1);

ALTER TABLE core_page ADD COLUMN meta_keywords varchar(255) default NULL;
ALTER TABLE core_page ADD COLUMN meta_description varchar(255) default NULL;

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
-- Table structure for table core_theme
--
DROP TABLE IF EXISTS core_theme;
CREATE TABLE  core_theme (
  code_theme varchar(25) default '' NOT NULL,
  theme_description varchar(255),
  path_images varchar(255) NOT NULL,
  path_css varchar(255) NOT NULL,
  theme_author varchar(255),
  theme_author_url varchar(255),
  theme_version varchar(255) NOT NULL,
  theme_licence varchar(255) NOT NULL,
  PRIMARY KEY (code_theme)
);

INSERT INTO core_theme VALUES ('black','Thème Noir','themes/black/images/','themes/black/css/page_template_styles.css','Mairie de Paris','http://fr.lutece.paris.fr','1.0','Creative-Commons');
INSERT INTO core_theme VALUES ('blue','Thème Bleu','images/','css/page_template_styles.css','Mairie de Paris','http://fr.lutece.paris.fr','1.0','Creative-Commons');

--
-- Table structure for table core_theme_global
--
DROP TABLE IF EXISTS core_theme_global;
CREATE TABLE core_theme_global (
  global_theme_code varchar(50) default '' NOT NULL,
  PRIMARY KEY  (global_theme_code)
);

INSERT INTO core_theme_global VALUES ('blue');