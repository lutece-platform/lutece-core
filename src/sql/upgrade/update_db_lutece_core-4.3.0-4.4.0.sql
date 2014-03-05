DELETE FROM core_user_parameter WHERE  parameter_key='password_format';
INSERT INTO core_user_parameter VALUES ('password_format_upper_lower_case', 'false');
INSERT INTO core_user_parameter VALUES ('password_format_numero', 'false');
INSERT INTO core_user_parameter VALUES ('password_format_special_characters', 'false');