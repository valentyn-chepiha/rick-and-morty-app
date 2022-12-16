--liquibase formatted sql

--changeset <vchepiha>:<create-episodes_external_links-table>
CREATE TABLE IF NOT EXISTS public.episodes_external_links
(
    episode_id     bigint       NOT NULL,
    external_link_id     bigint       NOT NULL
);

--rollback DROP TABLE public.episodes_external_links;