-- liquibase formatted sql
-- The changesets of 8.1.0. They were first appended to update_db_lutece_core-8.0.1-8.0.2.sql, a file a database created
-- with an 8.0.2 beta never runs. A database that already ran them from that file (same changeset id, other file) skips them:
-- each one is guarded by a precondition on its former id.

-- changeset core:update_db_lutece_core-8.0.2-8.1.0-rev1.sql
-- preconditions onFail:MARK_RAN onError:WARN
-- comment LUT-33153 : the XSS warning is plain text (written with textContent and escaped by the templates), no more HTML entities nor JavaScript escapes
-- precondition-sql-check expectedResult:0 SELECT COUNT(*) FROM DATABASECHANGELOG WHERE ID = 'update_db_lutece_core-8.0.1-8.0.2-rev36.sql'
UPDATE core_datastore SET entity_value = 'Les caractères < > # " et & sont interdits dans le contenu de votre message.'
WHERE entity_key = 'portal.theme.site_property.xss.xssMsg' AND entity_value LIKE 'Les caract%res &#60;%';

-- changeset core:update_db_lutece_core-8.0.2-8.1.0-rev2.sql
-- preconditions onFail:MARK_RAN onError:WARN
-- comment LUT-34109 : the parameters are no longer rejected nor rewritten on input; the browser check of the front office forms, which refused < > # " &, is emptied where it still holds its default value
-- precondition-sql-check expectedResult:0 SELECT COUNT(*) FROM DATABASECHANGELOG WHERE ID = 'update_db_lutece_core-8.0.1-8.0.2-rev37.sql'
UPDATE core_datastore SET entity_value = '' WHERE entity_key = 'portal.theme.site_property.xss.xssChars' AND entity_value = '<>#"&';

-- changeset core:update_db_lutece_core-8.0.2-8.1.0-rev3.sql
-- preconditions onFail:MARK_RAN onError:WARN
-- comment LUT-34109 : decodes, once, the HTML entities the removed input filter (sanitizeFilterMode) wrote into the plain text columns. Columns that hold HTML (templates, htmlblock site properties) are left as they are. Before the upgrade, back up the database; the values concerned are those of these columns matching LIKE '%&%;%' (queries in the ticket). A text in which a < was followed by a letter was cut by the filter at that point: that loss cannot be undone.
-- precondition-sql-check expectedResult:0 SELECT COUNT(*) FROM DATABASECHANGELOG WHERE ID = 'update_db_lutece_core-8.0.1-8.0.2-rev38.sql'
UPDATE core_admin_user SET last_name = REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(last_name, '&#39;', ''''), '&#x27;', ''''), '&#34;', '"'), '&quot;', '"'), '&#96;', '`'), '&#61;', '='), '&#64;', '@'), '&#43;', '+'), '&lt;', '<'), '&gt;', '>'), '&amp;', '&') WHERE last_name LIKE '%&%;%';
UPDATE core_admin_user SET first_name = REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(first_name, '&#39;', ''''), '&#x27;', ''''), '&#34;', '"'), '&quot;', '"'), '&#96;', '`'), '&#61;', '='), '&#64;', '@'), '&#43;', '+'), '&lt;', '<'), '&gt;', '>'), '&amp;', '&') WHERE first_name LIKE '%&%;%';
UPDATE core_admin_user SET email = REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(email, '&#39;', ''''), '&#x27;', ''''), '&#34;', '"'), '&quot;', '"'), '&#96;', '`'), '&#61;', '='), '&#64;', '@'), '&#43;', '+'), '&lt;', '<'), '&gt;', '>'), '&amp;', '&') WHERE email LIKE '%&%;%';
UPDATE core_admin_role SET role_description = REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(role_description, '&#39;', ''''), '&#x27;', ''''), '&#34;', '"'), '&quot;', '"'), '&#96;', '`'), '&#61;', '='), '&#64;', '@'), '&#43;', '+'), '&lt;', '<'), '&gt;', '>'), '&amp;', '&') WHERE role_description LIKE '%&%;%';
UPDATE core_admin_workgroup SET workgroup_description = REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(workgroup_description, '&#39;', ''''), '&#x27;', ''''), '&#34;', '"'), '&quot;', '"'), '&#96;', '`'), '&#61;', '='), '&#64;', '@'), '&#43;', '+'), '&lt;', '<'), '&gt;', '>'), '&amp;', '&') WHERE workgroup_description LIKE '%&%;%';
UPDATE core_role SET role_description = REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(role_description, '&#39;', ''''), '&#x27;', ''''), '&#34;', '"'), '&quot;', '"'), '&#96;', '`'), '&#61;', '='), '&#64;', '@'), '&#43;', '+'), '&lt;', '<'), '&gt;', '>'), '&amp;', '&') WHERE role_description LIKE '%&%;%';
UPDATE core_page SET name = REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(name, '&#39;', ''''), '&#x27;', ''''), '&#34;', '"'), '&quot;', '"'), '&#96;', '`'), '&#61;', '='), '&#64;', '@'), '&#43;', '+'), '&lt;', '<'), '&gt;', '>'), '&amp;', '&') WHERE name LIKE '%&%;%';
UPDATE core_page SET description = REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(description, '&#39;', ''''), '&#x27;', ''''), '&#34;', '"'), '&quot;', '"'), '&#96;', '`'), '&#61;', '='), '&#64;', '@'), '&#43;', '+'), '&lt;', '<'), '&gt;', '>'), '&amp;', '&') WHERE description LIKE '%&%;%';
UPDATE core_page SET meta_keywords = REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(meta_keywords, '&#39;', ''''), '&#x27;', ''''), '&#34;', '"'), '&quot;', '"'), '&#96;', '`'), '&#61;', '='), '&#64;', '@'), '&#43;', '+'), '&lt;', '<'), '&gt;', '>'), '&amp;', '&') WHERE meta_keywords LIKE '%&%;%';
UPDATE core_page SET meta_description = REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(meta_description, '&#39;', ''''), '&#x27;', ''''), '&#34;', '"'), '&quot;', '"'), '&#96;', '`'), '&#61;', '='), '&#64;', '@'), '&#43;', '+'), '&lt;', '<'), '&gt;', '>'), '&amp;', '&') WHERE meta_description LIKE '%&%;%';
UPDATE core_portlet SET name = REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(name, '&#39;', ''''), '&#x27;', ''''), '&#34;', '"'), '&quot;', '"'), '&#96;', '`'), '&#61;', '='), '&#64;', '@'), '&#43;', '+'), '&lt;', '<'), '&gt;', '>'), '&amp;', '&') WHERE name LIKE '%&%;%';
UPDATE core_admin_mailinglist SET name = REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(name, '&#39;', ''''), '&#x27;', ''''), '&#34;', '"'), '&quot;', '"'), '&#96;', '`'), '&#61;', '='), '&#64;', '@'), '&#43;', '+'), '&lt;', '<'), '&gt;', '>'), '&amp;', '&') WHERE name LIKE '%&%;%';
UPDATE core_admin_mailinglist SET description = REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(description, '&#39;', ''''), '&#x27;', ''''), '&#34;', '"'), '&quot;', '"'), '&#96;', '`'), '&#61;', '='), '&#64;', '@'), '&#43;', '+'), '&lt;', '<'), '&gt;', '>'), '&amp;', '&') WHERE description LIKE '%&%;%';
UPDATE core_attribute SET title = REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(title, '&#39;', ''''), '&#x27;', ''''), '&#34;', '"'), '&quot;', '"'), '&#96;', '`'), '&#61;', '='), '&#64;', '@'), '&#43;', '+'), '&lt;', '<'), '&gt;', '>'), '&amp;', '&') WHERE title LIKE '%&%;%';
UPDATE core_attribute SET help_message = REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(help_message, '&#39;', ''''), '&#x27;', ''''), '&#34;', '"'), '&quot;', '"'), '&#96;', '`'), '&#61;', '='), '&#64;', '@'), '&#43;', '+'), '&lt;', '<'), '&gt;', '>'), '&amp;', '&') WHERE help_message LIKE '%&%;%';
UPDATE core_attribute_field SET title = REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(title, '&#39;', ''''), '&#x27;', ''''), '&#34;', '"'), '&quot;', '"'), '&#96;', '`'), '&#61;', '='), '&#64;', '@'), '&#43;', '+'), '&lt;', '<'), '&gt;', '>'), '&amp;', '&') WHERE title LIKE '%&%;%';
UPDATE core_attribute_field SET DEFAULT_value = REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(DEFAULT_value, '&#39;', ''''), '&#x27;', ''''), '&#34;', '"'), '&quot;', '"'), '&#96;', '`'), '&#61;', '='), '&#64;', '@'), '&#43;', '+'), '&lt;', '<'), '&gt;', '>'), '&amp;', '&') WHERE DEFAULT_value LIKE '%&%;%';
UPDATE core_admin_user_field SET user_field_value = REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(user_field_value, '&#39;', ''''), '&#x27;', ''''), '&#34;', '"'), '&quot;', '"'), '&#96;', '`'), '&#61;', '='), '&#64;', '@'), '&#43;', '+'), '&lt;', '<'), '&gt;', '>'), '&amp;', '&') WHERE user_field_value LIKE '%&%;%';
UPDATE core_page_template SET description = REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(description, '&#39;', ''''), '&#x27;', ''''), '&#34;', '"'), '&quot;', '"'), '&#96;', '`'), '&#61;', '='), '&#64;', '@'), '&#43;', '+'), '&lt;', '<'), '&gt;', '>'), '&amp;', '&') WHERE description LIKE '%&%;%';
UPDATE core_datastore SET entity_value = REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(entity_value, '&#39;', ''''), '&#x27;', ''''), '&#34;', '"'), '&quot;', '"'), '&#96;', '`'), '&#61;', '='), '&#64;', '@'), '&#43;', '+'), '&lt;', '<'), '&gt;', '>'), '&amp;', '&') WHERE (entity_key LIKE 'portal.site.site_property.%' OR entity_key LIKE 'portal.theme.site_property.%') AND entity_key NOT LIKE '%.htmlblock' AND entity_key <> 'portal.theme.site_property.xss.xssMsg' AND entity_value LIKE '%&%;%';

-- changeset core:update_db_lutece_core-8.0.2-8.1.0-rev4.sql
-- preconditions onFail:MARK_RAN onError:WARN
-- comment markdown-text-editor, the front office editor: the textarea posts Markdown, turned into HTML by the BBCode parser service (BBCode is still understood).
-- precondition-sql-check expectedResult:0 SELECT COUNT(*) FROM DATABASECHANGELOG WHERE ID = 'update_db_lutece_core-8.0.1-8.0.2-rev39.sql'
-- precondition-sql-check expectedResult:0 SELECT COUNT(*) FROM core_text_editor WHERE editor_name = 'markdown' AND backOffice = 0
INSERT INTO core_text_editor VALUES ('markdown', 'portal.admindashboard.editors.labelFrontMarkdown', 0);

-- changeset core:update_db_lutece_core-8.0.2-8.1.0-rev5.sql
-- preconditions onFail:MARK_RAN onError:WARN
-- comment SCEditor is removed from the core: the front office moves to markdown-text-editor. The contents written in BBCode still render.
-- precondition-sql-check expectedResult:0 SELECT COUNT(*) FROM DATABASECHANGELOG WHERE ID = 'update_db_lutece_core-8.0.1-8.0.2-rev40.sql'
UPDATE core_datastore SET entity_value = 'markdown' WHERE entity_key = 'core.frontOffice.defaultEditor' AND entity_value = 'sceeditor';
DELETE FROM core_text_editor WHERE editor_name = 'sceeditor';

-- changeset core:update_db_lutece_core-8.0.2-8.1.0-rev6.sql
-- preconditions onFail:MARK_RAN onError:WARN
-- comment The front office validation messages written with JavaScript escapes by update_db_lutece_core-8.0.0-8.0.1-rev4.sql (stored without the backslash, caractu00e8res) or by the init script of 8.0.1 (an escape with its backslash): written again in UTF-8. A value changed by the administrator (without escape) is left as it is.
UPDATE core_datastore SET entity_value = 'La valeur doit être supérieure ou égale à {min}.' WHERE entity_key = 'portal.theme.site_property.formvalidation.msg.min' AND entity_value LIKE '%u00%';
UPDATE core_datastore SET entity_value = 'La valeur doit être inférieure ou égale à {max}.' WHERE entity_key = 'portal.theme.site_property.formvalidation.msg.max' AND entity_value LIKE '%u00%';
UPDATE core_datastore SET entity_value = 'Ce champ doit contenir au moins {minlength} caractères.' WHERE entity_key = 'portal.theme.site_property.formvalidation.msg.minlength' AND entity_value LIKE '%u00%';
UPDATE core_datastore SET entity_value = 'Ce champ ne doit pas dépasser {maxlength} caractères.' WHERE entity_key = 'portal.theme.site_property.formvalidation.msg.maxlength' AND entity_value LIKE '%u00%';
UPDATE core_datastore SET entity_value = 'La valeur doit être un multiple de {step}.' WHERE entity_key = 'portal.theme.site_property.formvalidation.msg.step' AND entity_value LIKE '%u00%';
UPDATE core_datastore SET entity_value = 'Veuillez saisir un numéro de téléphone valide.' WHERE entity_key = 'portal.theme.site_property.formvalidation.msg.tel' AND entity_value LIKE '%u00%';
UPDATE core_datastore SET entity_value = 'Veuillez sélectionner un fichier valide.' WHERE entity_key = 'portal.theme.site_property.formvalidation.msg.file' AND entity_value LIKE '%u00%';
UPDATE core_datastore SET entity_value = 'Type de fichier non autorisé. Types acceptés : {accept}.' WHERE entity_key = 'portal.theme.site_property.formvalidation.msg.filetype' AND entity_value LIKE '%u00%';

-- changeset core:update_db_lutece_core-8.0.2-8.1.0-rev7.sql
-- preconditions onFail:MARK_RAN onError:WARN
-- comment The right of the management of the levels, created by the install script only since 8.0.2-beta-03. The upgrade creates the right without granting it: the administrators give it to the users who need it.
-- precondition-sql-check expectedResult:0 SELECT COUNT(*) FROM core_admin_right WHERE id_right = 'CORE_LEVEL_MANAGEMENT'
INSERT INTO core_admin_right VALUES ('CORE_LEVEL_MANAGEMENT', 'portal.users.adminFeature.level_management.name', 0, NULL, 'portal.users.adminFeature.level_management.description', 1, '', 'SYSTEM', 'ti ti-gavel', NULL, 11, 0);

-- changeset core:update_db_lutece_core-8.0.2-8.1.0-rev8.sql
-- preconditions onFail:MARK_RAN onError:WARN
-- comment The languages of the translation menu, created by the install script only
-- precondition-sql-check expectedResult:0 SELECT COUNT(*) FROM core_datastore WHERE entity_key = 'portal.theme.site_property.menu.translate.langs.textblock'
INSERT INTO core_datastore VALUES ('portal.theme.site_property.menu.translate.langs.textblock', 'en,es,it,da,nl,de,tr,ko,zh-CN');

-- changeset core:update_db_lutece_core-8.0.2-8.1.0-rev9.sql
-- preconditions onFail:MARK_RAN onError:WARN
-- comment The search of the translation menu, created by the install script only
-- precondition-sql-check expectedResult:0 SELECT COUNT(*) FROM core_datastore WHERE entity_key = 'portal.theme.site_property.menu.translate.search.checkbox'
INSERT INTO core_datastore VALUES ('portal.theme.site_property.menu.translate.search.checkbox', '1');

-- changeset core:update_db_lutece_core-8.0.2-8.1.0-rev10.sql
-- preconditions onFail:MARK_RAN onError:WARN
-- comment The Content Security Policy of the pages allows the hosts of the translation script (Google Translate), as the install script does. Only the policy still holding the default value of 8.0.0 and 8.0.1 is changed: a policy changed by the administrator is left as it is.
UPDATE core_admin_security_header SET value = 'default-src ''self'' https://translate.googleapis.com ; script-src ''self'' ''unsafe-inline'' https://translate-pa.googleapis.com https://translate.googleapis.com https://translate.google.com/; style-src ''self'' ''unsafe-inline'' https://www.gstatic.com; img-src ''self'' data: blob: https://translate.google.com/ https://translate.googleapis.com https://translate-pa.googleapis.com https://www.gstatic.com https://fonts.gstatic.com https://www.google.com ; connect-src ''self'' blob: https://translate-pa.googleapis.com https://translate.googleapis.com https://translate.google.com/; object-src ''none''; font-src ''self'' data:;form-action ''self''; frame-ancestors ''self''; upgrade-insecure-requests' WHERE name = 'Content-Security-Policy' AND type = 'page' AND value = 'default-src ''self''; script-src ''self'' ''unsafe-inline''; style-src ''self'' ''unsafe-inline''; img-src ''self'' data: blob:; connect-src ''self'' blob:; object-src ''none''; font-src ''self'' data:;form-action ''self''; frame-ancestors ''self''; upgrade-insecure-requests';

-- changeset core:update_db_lutece_core-8.0.2-8.1.0-rev11.sql
-- preconditions onFail:MARK_RAN onError:WARN
-- comment The back office site property choosing the display of the vertical menu, created by the install script only
-- precondition-sql-check expectedResult:0 SELECT COUNT(*) FROM core_datastore WHERE entity_key = 'portal.site.site_property.layout.menu.sidebar.select'
INSERT INTO core_datastore VALUES ('portal.site.site_property.layout.menu.sidebar.select', 'default');

-- changeset core:update_db_lutece_core-8.0.2-8.1.0-rev12.sql
-- preconditions onFail:MARK_RAN onError:WARN
-- comment The options of the display of the vertical menu, created by the install script only
-- precondition-sql-check expectedResult:0 SELECT COUNT(*) FROM core_datastore WHERE entity_key = 'portal.site.site_property.layout.menu.sidebar.select.options'
INSERT INTO core_datastore VALUES ('portal.site.site_property.layout.menu.sidebar.select.options', 'default|folded|folded-hover');

-- changeset core:update_db_lutece_core-8.0.2-8.1.0-rev13.sql
-- preconditions onFail:MARK_RAN onError:WARN
-- comment The site property that shows or hides the notifications button of the BO header (shown by default)
-- precondition-sql-check expectedResult:0 SELECT COUNT(*) FROM core_datastore WHERE entity_key = 'portal.site.site_property.layout.user.notification.show.checkbox'
INSERT INTO core_datastore VALUES ('portal.site.site_property.layout.user.notification.show.checkbox', '1');

-- changeset core:update_db_lutece_core-8.0.2-8.1.0-rev14.sql
-- preconditions onFail:MARK_RAN onError:WARN
-- comment The site property that shows or hides the apps button of the BO header, the shortcuts of the user (shown by default)
-- precondition-sql-check expectedResult:0 SELECT COUNT(*) FROM core_datastore WHERE entity_key = 'portal.site.site_property.layout.user.apps.show.checkbox'
INSERT INTO core_datastore VALUES ('portal.site.site_property.layout.user.apps.show.checkbox', '1');
