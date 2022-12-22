--liquibase formatted sql

--changeset <vchepiha>:<create-episodes-sequence-id>
CREATE SEQUENCE IF NOT EXISTS public.episodes_id_seq INCREMENT 1 START 1 MINVALUE 1;

--rollback DROP SEQUENCE public.episodes_id_seq;
