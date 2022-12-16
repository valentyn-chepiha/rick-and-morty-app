--liquibase formatted sql

--changeset <vchepiha>:<create-locations-sequence-id>
CREATE SEQUENCE IF NOT EXISTS public.locations_id_seq INCREMENT 1 START 1 MINVALUE 1;

--rollback DROP SEQUENCE public.locations_id_seq;
