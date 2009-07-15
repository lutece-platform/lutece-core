ALTER TABLE core_style MODIFY COLUMN description_style VARCHAR(100) CHARACTER SET utf8 COLLATE utf8_unicode_ci NOT NULL;

INSERT INTO core_admin_right VALUES ('CORE_LINK_SERVICE_MANAGEMENT','portal.insert.adminFeature.linkService_management.name',2,NULL,'portal.insert.adminFeature.linkService_management.description',0,NULL,NULL,NULL,NULL,1);
