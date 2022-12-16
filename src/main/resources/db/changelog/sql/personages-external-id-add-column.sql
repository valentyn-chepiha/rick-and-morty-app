--liquibase formatted sql

--changeset <vchepiha>:<add-column-external-id-to-personages-table>
ALTER TABLE public.personages ADD external_id bigint;

--rollback ALTER TABLE DROP COLUNM external_id;