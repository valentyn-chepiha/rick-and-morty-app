--liquibase formatted sql

--changeset <vchepiha>:<create-episodes-table>
CREATE TABLE IF NOT EXISTS public.episodes
(
    id     bigint       NOT NULL,
    external_id  bigint   NOT NULL,
    name   varchar(256) NOT NULL,
    air_date varchar(256),
    episode varchar(256),
    url varchar(256),
    created varchar(256),
    CONSTRAINT episodes_pk PRIMARY KEY (id)
);

--rollback DROP TABLE public.episodes;