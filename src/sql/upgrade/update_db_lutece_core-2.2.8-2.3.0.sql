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

DROP TABLE IF EXISTS core_theme;
CREATE TABLE  core_theme (
  code_theme varchar(16) default "default" NOT NULL,
  theme_description varchar(255),
  path_images varchar(255) NOT NULL,
  path_css varchar(255) NOT NULL,
  theme_author varchar(255),
  theme_author_url varchar(255),
  theme_version varchar(255) NOT NULL,
  theme_licence varchar(255) NOT NULL,
  PRIMARY KEY (code_theme)
);
INSERT INTO core_theme (code_theme, theme_description, path_images, path_css, theme_author, theme_author_url, theme_version, theme_licence) VALUES ('default','Thème par défaut','images/local/skin/','css/page_template_styles.css','Mairie de Paris','http://www.paris.fr','1.0','BSD');
INSERT INTO core_theme (code_theme, theme_description, path_images, path_css, theme_author, theme_author_url, theme_version, theme_licence) VALUES ('black','Thème Noir','images/local/skin/','themes/black/css/page_template_styles.css','Mairie de Paris','http://www.paris.fr','1.0','BSD');
