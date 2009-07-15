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
