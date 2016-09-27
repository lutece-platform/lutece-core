ALTER TABLE core_file ADD COLUMN date_creation timestamp default NULL NULL;

-- updating core_admin_right and core_user_right for external features management 
-- on core_admin_right 
ALTER TABLE core_admin_right ADD COLUMN is_external_feature BOOLEAN DEFAULT NULL;
UPDATE core_admin_right SET is_external_feature = false ;
INSERT INTO core_admin_right VALUES ('CORE_EXTERNAL_FEATURES_MANAGEMENT', 'portal.system.adminFeature.external_features_management.name', 1, 'jsp/admin/features/ManageExternalFeatures.jsp', 'portal.system.adminFeature.external_features_management.description', 1, NULL, 'SYSTEM', NULL, NULL, 11, false);

-- on core_user_right
INSERT INTO core_user_right VALUES ('CORE_EXTERNAL_FEATURES_MANAGEMENT',1);
INSERT INTO core_user_right VALUES ('CORE_EXTERNAL_FEATURES_MANAGEMENT',2);
