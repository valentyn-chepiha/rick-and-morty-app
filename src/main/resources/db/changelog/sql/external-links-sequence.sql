--liquibase formatted sql

--changeset <vchepiha>:<create-external_links-sequence-id>
CREATE SEQUENCE IF NOT EXISTS public.external_links_id_seq INCREMENT 1 START 1 MINVALUE 1;

--rollback DROP SEQUENCE public.external_links_id_seq;
