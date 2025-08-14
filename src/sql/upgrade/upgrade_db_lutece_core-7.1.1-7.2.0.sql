CREATE TABLE  core_theme (
  code_theme varchar(25) default '' NOT NULL,
  theme_description varchar(255),
  path_images varchar(255) NOT NULL,
  path_css varchar(255) NOT NULL,
  theme_author varchar(255),
  theme_author_url varchar(255),
  theme_version varchar(255) NOT NULL,
  theme_licence varchar(255) NOT NULL,
  path_js varchar(255) NOT NULL,
  PRIMARY KEY (code_theme)
);


INSERT INTO core_theme (code_theme, theme_description, path_images, path_css, theme_author, theme_author_url, theme_version, theme_licence, path_js) VALUES
	('amelia', 'Amelia', 'themes/site/amelia/img/', 'themes/site/amelia/css', 'Bootswatch', 'http://bootswatch.com/', '1.0', 'Apache 2.0', 'js/'),
	('blue', 'Thème Défaut', 'images/', 'css', 'Mairie de Paris', 'http://fr.lutece.paris.fr', '1.0', 'BSD', 'js/'),
	('slate', 'Slate', 'themes/site/slate/img/', 'themes/site/slate/css', 'Bootswatch', 'http://bootswatch.com/', '1.0', 'Apache 2.0', 'js/'),
	('superhero', 'Superhero', 'themes/site/superhero/img/', 'themes/site/superhero/css', 'Bootswatch', 'http://bootswatch.com/', '1.0', 'Apache 2.0', 'js/'),
	('flatly', 'Flatly', 'themes/site/flatly/img/', 'themes/site/flatly/css', 'Bootswatch', 'http://bootswatch.com/', '1.0', 'Apache 2.0', 'js/'),
	('darkly', 'Darkly', 'themes/site/darkly/img/', 'themes/site/darkly/css', 'Bootswatch', 'http://bootswatch.com/', '1.0', 'Apache 2.0', 'js/'),
	('cyborg', 'Cyborg', 'themes/site/cyborg/img/', 'themes/site/cyborg/css', 'Bootswatch', 'http://bootswatch.com/', '1.0', 'Apache 2.0', 'js/'),
	('readable', 'Readable', 'themes/site/readable/img/', 'themes/site/readable/css', 'Bootswatch', 'http://bootswatch.com/', '1.0', 'Apache 2.0', 'js/'),
	('cosmo', 'Cosmo', 'themes/site/cosmo/img/', 'themes/site/cosmo/css', 'Bootswatch', 'http://bootswatch.com/', '1.0', 'Apache 2.0', 'js/'),
	('united', 'United', 'themes/site/united/img/', 'themes/site/united/css', 'Bootswatch', 'http://bootswatch.com/', '1.0', 'Apache 2.0', 'js/');
	
CREATE TABLE core_theme_global (
  global_theme_code varchar(50) default '' NOT NULL,
  PRIMARY KEY  (global_theme_code)
);

INSERT INTO core_theme_global VALUES ('blue');

INSERT INTO core_admin_right (id_right,name,level_right,admin_url,description,is_updatable,plugin_name,id_feature_group,icon_url, documentation_url) VALUES 
('CORE_THEME_MANAGEMENT','portal.theme.adminFeature.theme_management.name',0,'jsp/admin/theme/ManageThemes.jsp','portal.theme.adminFeature.theme_management.description',1,'','STYLE','images/admin/skin/features/manage_styles.png', NULL);
