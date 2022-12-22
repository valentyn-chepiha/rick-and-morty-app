--liquibase formatted sql

--changeset <vchepiha>:<create-personage-origins-table>
CREATE TABLE IF NOT EXISTS public.personage_origins
(
    personage_id     bigint       NOT NULL,
    name   varchar(256),
    external_id     bigint
);

--rollback DROP TABLE public.personage_origins;