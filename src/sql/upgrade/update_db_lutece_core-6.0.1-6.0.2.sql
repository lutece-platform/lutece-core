ALTER TABLE core_page add COLUMN display_date_update smallint default 0 NOT NULL;
ALTER TABLE core_page add COLUMN is_manual_date_update smallint default 0 NOT NULL;
