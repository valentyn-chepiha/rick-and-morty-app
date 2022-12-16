--liquibase formatted sql

--changeset <vchepiha>:<create-locations_external_links-table>
CREATE TABLE IF NOT EXISTS public.locations_external_links
(
    location_id     bigint       NOT NULL,
    external_link_id     bigint       NOT NULL
);

--rollback DROP TABLE public.locations_external_links;