-- Table: toggler.toggles

-- DROP TABLE toggler.toggles;

CREATE TABLE toggler.toggles
(
    toggle_identifier text COLLATE pg_catalog."default" NOT NULL,
    value boolean NOT NULL,
    active boolean NOT NULL,
    CONSTRAINT toggler_pkey PRIMARY KEY (toggle_identifier)
)
WITH (
    OIDS = FALSE
)
TABLESPACE pg_default;

ALTER TABLE toggler.toggles
    OWNER to postgres;

-- Table: toggler.toggles_exclusions

-- DROP TABLE toggler.toggles_exclusions;

CREATE TABLE toggler.toggles_exclusions
(
    toggle_identifier text COLLATE pg_catalog."default" NOT NULL,
    service_identifier text COLLATE pg_catalog."default" NOT NULL,
    service_version text COLLATE pg_catalog."default" NOT NULL,
    active boolean NOT NULL,
    id integer NOT NULL,
    CONSTRAINT toggles_exclusions_pkey PRIMARY KEY (id),
    CONSTRAINT toggles_exclusions_unique UNIQUE (toggle_identifier, service_identifier, service_version)
,
    CONSTRAINT toggler_exclusion_identifier_fkey FOREIGN KEY (toggle_identifier)
        REFERENCES toggler.toggles (toggle_identifier) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION
)
WITH (
    OIDS = FALSE
)
TABLESPACE pg_default;

ALTER TABLE toggler.toggles_exclusions
    OWNER to postgres;


-- Table: toggler.toggles_overrides

-- DROP TABLE toggler.toggles_overrides;

CREATE TABLE toggler.toggles_overrides
(
    toggle_identifier text COLLATE pg_catalog."default" NOT NULL,
    service_identifier text COLLATE pg_catalog."default" NOT NULL,
    service_version text COLLATE pg_catalog."default" NOT NULL,
    value boolean NOT NULL,
    active boolean NOT NULL,
    id integer NOT NULL,
    CONSTRAINT toggles_overrides_pkey PRIMARY KEY (id),
    CONSTRAINT toggles_overrides_unique UNIQUE (toggle_identifier, service_identifier, service_version)

)
WITH (
    OIDS = FALSE
)
TABLESPACE pg_default;

ALTER TABLE toggler.toggles_overrides
    OWNER to postgres;