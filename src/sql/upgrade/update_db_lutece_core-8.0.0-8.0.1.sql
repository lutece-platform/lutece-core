-- liquibase formatted sql
-- changeset core:update_db_lutece_core-8.0.0-8.0.1.sql
-- preconditions onFail:MARK_RAN onError:WARN

DELETE FROM core_datastore WHERE entity_key = 'portal.theme.site_property.menu.logo.alt';
INSERT INTO core_datastore VALUES ('portal.theme.site_property.menu.logo.alt', '');

-- changeset core:update_db_lutece_core-8.0.0-8.0.1-rev1.sql
DELETE FROM core_datastore WHERE entity_key = 'portal.theme.site_property.xss.xssMsg';
INSERT INTO core_datastore VALUES ('portal.theme.site_property.xss.xssMsg', 'Les caract\\u00e8res &#60; &#62; &#35; et &#34;  &amp; sont interdits dans le contenu de votre message.');

-- changeset core:update_db_lutece_core-8.0.0-8.0.1-rev2.sql
DELETE FROM core_datastore WHERE entity_key = 'portal.site.site_property.layout.fluid.checkbox';
INSERT INTO core_datastore VALUES ('portal.site.site_property.layout.fluid.checkbox', '1');

-- changeset core:update_db_lutece_core-8.0.0-8.0.1-rev3.sql
-- preconditions onFail:MARK_RAN onError:WARN
-- precondition-sql-check expectedResult:0 SELECT COUNT(1) from INFORMATION_SCHEMA.TABLES WHERE TABLE_NAME='core_admin_security_header_config_item';
CREATE TABLE core_admin_security_header_config_item (
  id_config_item SMALLINT AUTO_INCREMENT NOT NULL,
  id_security_header SMALLINT NOT NULL,
  header_custom_value VARCHAR(1024) NULL,
  url_pattern VARCHAR(1024) NOT NULL,
  PRIMARY KEY  (id_config_item)
);

-- changeset core:update_db_lutece_core-8.0.0-8.0.1-rev4.sql
DELETE FROM core_datastore WHERE entity_key = 'portal.theme.site_property.formvalidation.msg.required';
INSERT INTO core_datastore VALUES ('portal.theme.site_property.formvalidation.msg.required', 'Ce champ est obligatoire.');
DELETE FROM core_datastore WHERE entity_key = 'portal.theme.site_property.formvalidation.msg.email';
INSERT INTO core_datastore VALUES ('portal.theme.site_property.formvalidation.msg.email', 'Veuillez saisir une adresse email valide.');
DELETE FROM core_datastore WHERE entity_key = 'portal.theme.site_property.formvalidation.msg.url';
INSERT INTO core_datastore VALUES ('portal.theme.site_property.formvalidation.msg.url', 'Veuillez saisir une URL valide.');
DELETE FROM core_datastore WHERE entity_key = 'portal.theme.site_property.formvalidation.msg.number';
INSERT INTO core_datastore VALUES ('portal.theme.site_property.formvalidation.msg.number', 'Veuillez saisir un nombre valide.');
DELETE FROM core_datastore WHERE entity_key = 'portal.theme.site_property.formvalidation.msg.min';
INSERT INTO core_datastore VALUES ('portal.theme.site_property.formvalidation.msg.min', 'La valeur doit \u00eatre sup\u00e9rieure ou \u00e9gale \u00e0 {min}.');
DELETE FROM core_datastore WHERE entity_key = 'portal.theme.site_property.formvalidation.msg.max';
INSERT INTO core_datastore VALUES ('portal.theme.site_property.formvalidation.msg.max', 'La valeur doit \u00eatre inf\u00e9rieure ou \u00e9gale \u00e0 {max}.');
DELETE FROM core_datastore WHERE entity_key = 'portal.theme.site_property.formvalidation.msg.minlength';
INSERT INTO core_datastore VALUES ('portal.theme.site_property.formvalidation.msg.minlength', 'Ce champ doit contenir au moins {minlength} caract\u00e8res.');
DELETE FROM core_datastore WHERE entity_key = 'portal.theme.site_property.formvalidation.msg.maxlength';
INSERT INTO core_datastore VALUES ('portal.theme.site_property.formvalidation.msg.maxlength', 'Ce champ ne doit pas d\u00e9passer {maxlength} caract\u00e8res.');
DELETE FROM core_datastore WHERE entity_key = 'portal.theme.site_property.formvalidation.msg.pattern';
INSERT INTO core_datastore VALUES ('portal.theme.site_property.formvalidation.msg.pattern', 'Le format saisi est invalide.');
DELETE FROM core_datastore WHERE entity_key = 'portal.theme.site_property.formvalidation.msg.step';
INSERT INTO core_datastore VALUES ('portal.theme.site_property.formvalidation.msg.step', 'La valeur doit \u00eatre un multiple de {step}.');
DELETE FROM core_datastore WHERE entity_key = 'portal.theme.site_property.formvalidation.msg.tel';
INSERT INTO core_datastore VALUES ('portal.theme.site_property.formvalidation.msg.tel', 'Veuillez saisir un num\u00e9ro de t\u00e9l\u00e9phone valide.');
DELETE FROM core_datastore WHERE entity_key = 'portal.theme.site_property.formvalidation.msg.date';
INSERT INTO core_datastore VALUES ('portal.theme.site_property.formvalidation.msg.date', 'Veuillez saisir une date valide.');
DELETE FROM core_datastore WHERE entity_key = 'portal.theme.site_property.formvalidation.msg.time';
INSERT INTO core_datastore VALUES ('portal.theme.site_property.formvalidation.msg.time', 'Veuillez saisir une heure valide.');
DELETE FROM core_datastore WHERE entity_key = 'portal.theme.site_property.formvalidation.msg.file';
INSERT INTO core_datastore VALUES ('portal.theme.site_property.formvalidation.msg.file', 'Veuillez s\u00e9lectionner un fichier valide.');
DELETE FROM core_datastore WHERE entity_key = 'portal.theme.site_property.formvalidation.msg.filetype';
INSERT INTO core_datastore VALUES ('portal.theme.site_property.formvalidation.msg.filetype', 'Type de fichier non autoris\u00e9. Types accept\u00e9s : {accept}.');
DELETE FROM core_datastore WHERE entity_key = 'portal.theme.site_property.formvalidation.msg.filesize';
INSERT INTO core_datastore VALUES ('portal.theme.site_property.formvalidation.msg.filesize', 'Le fichier est trop volumineux. Taille maximale : {maxsize}.');
DELETE FROM core_datastore WHERE entity_key = 'portal.theme.site_property.formvalidation.msg.mismatch';
INSERT INTO core_datastore VALUES ('portal.theme.site_property.formvalidation.msg.mismatch', 'Les valeurs ne correspondent pas.');
DELETE FROM core_datastore WHERE entity_key = 'portal.theme.site_property.formvalidation.msg.custom';
INSERT INTO core_datastore VALUES ('portal.theme.site_property.formvalidation.msg.custom', 'Ce champ contient une erreur.');
DELETE FROM core_datastore WHERE entity_key = 'portal.theme.site_property.formvalidation.errorClass';
INSERT INTO core_datastore VALUES ('portal.theme.site_property.formvalidation.errorClass', 'is-invalid');
DELETE FROM core_datastore WHERE entity_key = 'portal.theme.site_property.formvalidation.validClass';
INSERT INTO core_datastore VALUES ('portal.theme.site_property.formvalidation.validClass', 'is-valid');
DELETE FROM core_datastore WHERE entity_key = 'portal.theme.site_property.formvalidation.errorFeedbackClass';
INSERT INTO core_datastore VALUES ('portal.theme.site_property.formvalidation.errorFeedbackClass', 'invalid-feedback');
DELETE FROM core_datastore WHERE entity_key = 'portal.theme.site_property.formvalidation.helpClass';
INSERT INTO core_datastore VALUES ('portal.theme.site_property.formvalidation.helpClass', 'form-text');
DELETE FROM core_datastore WHERE entity_key = 'portal.theme.site_property.formvalidation.errorIconSvg';
INSERT INTO core_datastore VALUES ('portal.theme.site_property.formvalidation.errorIconSvg', '<svg class="paris-icon paris-icon-alert-error main-danger-color me-xxs" aria-hidden="true" focusable="false" role="img"><use href="#paris-icon-alert-error"></use></svg>');
