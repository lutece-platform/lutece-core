ALTER TABLE core_portlet ADD role varchar(50) default NULL;
-- search admin dashboard --
INSERT INTO core_admin_dashboard(dashboard_name, dashboard_column, dashboard_order) VALUES('searchAdminDashboardComponent', 1, 2);
INSERT INTO core_admin_right VALUES ('CORE_SEARCH_MANAGEMENT','portal.search.adminFeature.search_management.name',0,'jsp/admin/search/ManageSearch.jsp','portal.search.adminFeature.search_management.description',0,NULL,'SYSTEM',NULL,NULL,NULL);
INSERT INTO core_admin_role_resource VALUES (137,'all_site_manager', 'SEARCH_SERVICE', '*', '*');
INSERT INTO core_user_right VALUES ('CORE_SEARCH_MANAGEMENT',1);
INSERT INTO core_user_right VALUES ('CORE_SEARCH_MANAGEMENT',2);

-- Table structure for table core_search_parameter --
DROP TABLE IF EXISTS core_search_parameter;
CREATE TABLE core_search_parameter 
(    
	parameter_key VARCHAR(100) NOT NULL,    
	parameter_value LONG VARCHAR,  
	PRIMARY KEY (parameter_key) 
);

INSERT INTO core_search_parameter (parameter_key, parameter_value) VALUES ('type_filter', 'none');
INSERT INTO core_search_parameter (parameter_key, parameter_value) VALUES ('default_operator', 'OR');
INSERT INTO core_search_parameter (parameter_key, parameter_value) VALUES ('help_message', '');
INSERT INTO core_search_parameter (parameter_key, parameter_value) VALUES ('date_filter', '0');
INSERT INTO core_search_parameter (parameter_key, parameter_value) VALUES ('tag_filter', '0');
INSERT INTO core_search_parameter (parameter_key, parameter_value) VALUES ('taglist', '');

ALTER TABLE core_admin_user ADD COLUMN accessibility_mode smallint default 0 NOT NULL;
