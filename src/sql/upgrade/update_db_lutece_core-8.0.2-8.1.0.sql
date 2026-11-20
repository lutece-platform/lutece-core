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
