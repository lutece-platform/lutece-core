INSERT INTO core_user_parameter VALUES ('force_change_password_reinit', 'false');
INSERT INTO core_user_parameter VALUES ('password_minimum_length', '8');
INSERT INTO core_user_parameter VALUES ('password_format', 'false');
INSERT INTO core_user_parameter VALUES ('password_history_size', '');
INSERT INTO core_user_parameter VALUES ('maximum_number_password_change', '');
INSERT INTO core_user_parameter VALUES ('tsw_size_password_change', '');
INSERT INTO core_user_parameter VALUES ('use_advanced_security_parameters', '');
INSERT INTO core_user_parameter VALUES ('account_life_time', '12');
INSERT INTO core_user_parameter VALUES ('time_before_alert_account', '30');
INSERT INTO core_user_parameter VALUES ('nb_alert_account', '2');
INSERT INTO core_user_parameter VALUES ('time_between_alerts_account', '10');
INSERT INTO core_user_parameter VALUES ('access_failures_max', '3');
INSERT INTO core_user_parameter VALUES ('access_failures_interval', '10');
INSERT INTO core_user_parameter VALUES ('expired_alert_mail_sender', 'lutece@nowhere.com');
INSERT INTO core_user_parameter VALUES ('expired_alert_mail_subject', 'Votre compte a expiré');
INSERT INTO core_user_parameter VALUES ('first_alert_mail_sender', 'lutece@nowhere.com');
INSERT INTO core_user_parameter VALUES ('first_alert_mail_subject', 'Votre compte va bientot expirer');
INSERT INTO core_user_parameter VALUES ('other_alert_mail_sender', 'lutece@nowhere.com');
INSERT INTO core_user_parameter VALUES ('other_alert_mail_subject', 'Votre compte va bientot expirer');
INSERT INTO core_user_parameter VALUES ('account_reactivated_mail_sender', 'lutece@nowhere.com');
INSERT INTO core_user_parameter VALUES ('account_reactivated_mail_subject', 'Votre compte a bien été réactivé');

DROP TABLE IF EXISTS core_template;
CREATE TABLE core_template (
  template_name VARCHAR(100) NOT NULL,
  template_value LONG VARCHAR NULL,
  PRIMARY KEY (template_name)
  );
  
ALTER TABLE core_admin_user ADD COLUMN password_max_valid_date TIMESTAMP NULL ;
ALTER TABLE core_admin_user ADD COLUMN account_max_valid_date BIGINT NULL ;
ALTER TABLE core_admin_user ADD COLUMN nb_alerts_sent INTEGER DEFAULT 0 NOT NULL;
ALTER TABLE core_attribute ADD COLUMN anonymize SMALLINT DEFAULT NULL ;
ALTER TABLE core_portlet ADD COLUMN device_display_flags int default 15 NOT NULL;

DROP TABLE IF EXISTS core_user_password_history;
CREATE  TABLE core_user_password_history (
  id_user INT NOT NULL ,
  password VARCHAR(100) NOT NULL ,
  date_password_change TIMESTAMP NOT NULL DEFAULT NOW() ,
  PRIMARY KEY (id_user, date_password_change)
  );
  
DROP TABLE IF EXISTS core_admin_user_anonymize_field;
CREATE  TABLE core_admin_user_anonymize_field (
  field_name VARCHAR(100) NOT NULL ,
  anonymize SMALLINT NOT NULL ,
  PRIMARY KEY (field_name)
  );

INSERT INTO core_admin_user_anonymize_field (field_name, anonymize) VALUES ('access_code', 1);
INSERT INTO core_admin_user_anonymize_field (field_name, anonymize) VALUES ('last_name', 1);
INSERT INTO core_admin_user_anonymize_field (field_name, anonymize) VALUES ('first_name', 1);
INSERT INTO core_admin_user_anonymize_field (field_name, anonymize) VALUES ('email', 1);


INSERT INTO core_template VALUES ('core_first_alert_mail', 'Bonjour ${first_name} ! Votre compte utilisateur arrive à expiration. Pour prolonger sa validité, veuillez <a href="${url}">cliquer ici</a>.</br>Si vous ne le faites pas avant le ${date_valid}, il sera désactivé.');
INSERT INTO core_template VALUES ('core_expiration_mail', 'Bonjour ${first_name} ! Votre compte a expiré. Vous ne pourrez plus vous connecter avec, et les données vous concernant ont été anonymisées');
INSERT INTO core_template VALUES ('core_other_alert_mail', 'Bonjour ${first_name} ! Votre compte utilisateur arrive à expiration. Pour prolonger sa validité, veuillez <a href="${url}">cliquer ici</a>.</br>Si vous ne le faites pas avant le ${date_valid}, il sera désactivé.');
INSERT INTO core_template VALUES ('core_account_reactivated_mail', 'Bonjour ${first_name} ! Votre compte utilisateur a bien été réactivé. Il est désormais valable jusqu''au ${date_valid}.');
--
-- UPDATE of email size to 256 for respecting WSSO constraints 
--
ALTER TABLE core_admin_user MODIFY email  VARCHAR(256) default '' NOT NULL; 
