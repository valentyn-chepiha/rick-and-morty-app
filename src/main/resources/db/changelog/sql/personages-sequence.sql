--liquibase formatted sql

--changeset <vchepiha>:<create-personages-sequence-id>
CREATE SEQUENCE IF NOT EXISTS public.personages_id_seq INCREMENT 1 START 1 MINVALUE 1;

--rollback DROP SEQUENCE public.personages_id_seq;
