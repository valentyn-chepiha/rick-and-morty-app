--liquibase formatted sql

--changeset <vchepiha>:<create-personages_external_links-table>
CREATE TABLE IF NOT EXISTS public.personages_external_links
(
    personage_id     bigint       NOT NULL,
    external_link_id     bigint       NOT NULL
);

--rollback DROP TABLE public.personages_external_links;