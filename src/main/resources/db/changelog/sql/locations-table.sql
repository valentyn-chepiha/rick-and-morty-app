--liquibase formatted sql

--changeset <vchepiha>:<create-locations-table>
CREATE TABLE IF NOT EXISTS public.locations
(
    id     bigint       NOT NULL,
    external_id     bigint       NOT NULL,
    name   varchar(256) NOT NULL,
    type varchar(256),
    dimension varchar(256),
    CONSTRAINT locations_pk PRIMARY KEY (id)
);

--rollback DROP TABLE public.locations;