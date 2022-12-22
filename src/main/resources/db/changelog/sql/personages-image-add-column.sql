--liquibase formatted sql

--changeset <vchepiha>:<add-column-image-to-personages-table>
ALTER TABLE public.personages ADD image varchar(256);

--rollback ALTER TABLE DROP COLUNM image;