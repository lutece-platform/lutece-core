ALTER TABLE core_portlet CHANGE COLUMN name name VARCHAR(70) NULL DEFAULT NULL COLLATE 'utf8_unicode_ci' AFTER id_page;
ALTER TABLE core_xsl_export ADD COLUMN plugin VARCHAR(255) DEFAULT '';

INSERT INTO core_xsl_export VALUES (125,'Coeur - Export CSV des administrateurs','Export des utilisateurs back office dans un fichier CSV','csv',125,'core');
INSERT INTO core_file VALUES (125,'export_users_csv.xml',125,2523,'application/xml');
INSERT INTO core_physical_file VALUES (125,'<?xml version=\"1.0\"?>\r\n<xsl:stylesheet version=\"1.0\" xmlns:xsl=\"http://www.w3.org/1999/XSL/Transform\">\r\n	<xsl:output method=\"text\"/>\r\n	\r\n	<xsl:template match=\"users\">\r\n		<xsl:apply-templates select=\"user\" />\r\n	</xsl:template>\r\n	\r\n	<xsl:template match=\"user\">\r\n		<xsl:text>\"</xsl:text>\r\n		<xsl:value-of select=\"access_code\" />\r\n		<xsl:text>\";\"</xsl:text>\r\n		<xsl:value-of select=\"last_name\" />\r\n		<xsl:text>\";\"</xsl:text>\r\n		<xsl:value-of select=\"first_name\" />\r\n		<xsl:text>\";\"</xsl:text>\r\n		<xsl:value-of select=\"email\" />\r\n		<xsl:text>\";\"</xsl:text>\r\n		<xsl:value-of select=\"status\" />\r\n		<xsl:text>\";\"</xsl:text>\r\n		<xsl:value-of select=\"locale\" />\r\n		<xsl:text>\";\"</xsl:text>\r\n		<xsl:value-of select=\"level\" />\r\n		<xsl:text>\";\"</xsl:text>\r\n		<xsl:value-of select=\"must_change_password\" />\r\n		<xsl:text>\";\"</xsl:text>\r\n		<xsl:value-of select=\"accessibility_mode\" />\r\n		<xsl:text>\";\"</xsl:text>\r\n		<xsl:value-of select=\"password_max_valid_date\" />\r\n		<xsl:text>\";\"</xsl:text>\r\n		<xsl:value-of select=\"account_max_valid_date\" />\r\n		<xsl:text>\";\"</xsl:text>\r\n		<xsl:value-of select=\"date_last_login\" />\r\n		<xsl:text>\"</xsl:text>\r\n		<xsl:apply-templates select=\"roles\" />\r\n		<xsl:apply-templates select=\"rights\" />\r\n		<xsl:apply-templates select=\"workgroups\" />\r\n		<xsl:apply-templates select=\"attributes\" />\r\n		<xsl:text>&#10;</xsl:text>\r\n	</xsl:template>\r\n	\r\n	<xsl:template match=\"roles\">\r\n		<xsl:apply-templates select=\"role\" />\r\n	</xsl:template>\r\n	\r\n	<xsl:template match=\"role\">\r\n		<xsl:text>;\"role:</xsl:text>\r\n		<xsl:value-of select=\"current()\" />\r\n		<xsl:text>\"</xsl:text>\r\n	</xsl:template>\r\n	\r\n	<xsl:template match=\"rights\">\r\n		<xsl:apply-templates select=\"right\" />\r\n	</xsl:template>\r\n	\r\n	<xsl:template match=\"right\">\r\n		<xsl:text>;\"right:</xsl:text>\r\n		<xsl:value-of select=\"current()\" />\r\n		<xsl:text>\"</xsl:text>\r\n	</xsl:template>\r\n	\r\n	<xsl:template match=\"workgroups\">\r\n		<xsl:apply-templates select=\"workgroup\" />\r\n	</xsl:template>\r\n	\r\n	<xsl:template match=\"workgroup\">\r\n		<xsl:text>;\"workgroup:</xsl:text>\r\n		<xsl:value-of select=\"current()\" />\r\n		<xsl:text>\"</xsl:text>\r\n	</xsl:template>\r\n	\r\n	<xsl:template match=\"attributes\">\r\n		<xsl:apply-templates select=\"attribute\" />\r\n	</xsl:template>\r\n	\r\n	<xsl:template match=\"attribute\">\r\n		<xsl:text>;\"</xsl:text>\r\n		<xsl:value-of select=\"attribute-id\" />\r\n		<xsl:text>:</xsl:text>\r\n		<xsl:value-of select=\"attribute-field-id\" />\r\n		<xsl:text>:</xsl:text>\r\n		<xsl:value-of select=\"attribute-value\" />\r\n		<xsl:text>\"</xsl:text>\r\n	</xsl:template>\r\n	\r\n</xsl:stylesheet>');

INSERT INTO core_xsl_export VALUES (126,'Coeur - Export XML des administrateurs','Export des utilisateurs back office dans un fichier XML','xml',126,'core');
INSERT INTO core_file VALUES (126,'export_users_xml.xml',126,259,'application/xml');
INSERT INTO core_physical_file VALUES (126,'<?xml version=\"1.0\" ?>\r\n<xsl:stylesheet version=\"1.0\" xmlns:xsl=\"http://www.w3.org/1999/XSL/Transform\">\r\n	<xsl:template match=\"/ | @* | node()\">\r\n		<xsl:copy>\r\n			<xsl:apply-templates select=\"@* | node()\" />\r\n		</xsl:copy>\r\n	</xsl:template>\r\n</xsl:stylesheet>');

INSERT INTO core_datastore VALUES ('portal.site.site_property.name', 'LUTECE');
INSERT INTO core_datastore VALUES ('portal.site.site_property.meta.author', '<author>');
INSERT INTO core_datastore VALUES ('portal.site.site_property.meta.copyright', '<copyright>');
INSERT INTO core_datastore VALUES ('portal.site.site_property.meta.description', '<description>');
INSERT INTO core_datastore VALUES ('portal.site.site_property.meta.keywords', '<keywords>');
INSERT INTO core_datastore VALUES ('portal.site.site_property.email', '<webmaster email>');
INSERT INTO core_datastore VALUES ('portal.site.site_property.noreply_email', 'no-reply@mydomain.com');
INSERT INTO core_datastore VALUES ('portal.site.site_property.home_url', 'jsp/site/Portal.jsp');
INSERT INTO core_datastore VALUES ('portal.site.site_property.admin_home_url', 'jsp/admin/AdminMenu.jsp');
INSERT INTO core_datastore VALUES ('portal.site.site_property.popup_credits.textblock', '&lt;credits text&gt;');
INSERT INTO core_datastore VALUES ('portal.site.site_property.popup_legal_info.copyright.textblock', '&lt;copyright text&gt;');
INSERT INTO core_datastore VALUES ('portal.site.site_property.popup_legal_info.privacy.textblock', '&lt;privacy text&gt;');
INSERT INTO core_datastore VALUES ('core.daemon.threadLauncherDaemon.interval', '10');
INSERT INTO core_datastore VALUES ('core.daemon.threadLauncherDaemon.onStartup', '10');


UPDATE core_admin_right SET admin_url='jsp/admin/features/DispatchFeatures.jsp' WHERE id_right='CORE_FEATURES_MANAGEMENT';
