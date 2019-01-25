--
-- Table structure for table core_daemon_trigger
--
DROP TABLE IF EXISTS core_daemon_trigger;
CREATE TABLE core_daemon_trigger (
    id_daemon_trigger int AUTO_INCREMENT,
    key varchar(50) default '' NOT NULL,
    group varchar(50) default '' NOT NULL,
    cron_expression varchar(50) default '' NOT NULL,
    daemon_key varchar(50) default '' NOT NULL,
    PRIMARY KEY (id_daemon_trigger)
);