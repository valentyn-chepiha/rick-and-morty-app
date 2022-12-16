--liquibase formatted sql

--changeset <vchepiha>:<create-personages-table>
CREATE TABLE IF NOT EXISTS public.personages
(
    id     bigint       NOT NULL,
    name   varchar(256) NOT NULL,
    status varchar(256),
    specie varchar(256),
    type   varchar(256),
    gender varchar(256),
    CONSTRAINT personages_pk PRIMARY KEY (id)
);

--rollback DROP TABLE public.personages;