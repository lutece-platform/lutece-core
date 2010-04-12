ALTER TABLE core_theme ADD COLUMN path_js varchar(255) default NULL;

UPDATE TABLE core_theme SET path_css='themes/black/css', path_js='theme/black/js' WHERE code_theme='black';
UPDATE TABLE core_theme SET path_css='css', path_js='js' WHERE code_theme='blue';