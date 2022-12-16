--liquibase formatted sql

--changeset <vchepiha>:<create-external-links-table>
CREATE TABLE IF NOT EXISTS public.external_links
(
    id     bigint       NOT NULL,
    external_id     bigint       NOT NULL,
    CONSTRAINT external_links_pk PRIMARY KEY (id)
);

--rollback DROP TABLE public.external_links;