--liquibase formatted sql
--changeset ilya_groshev:6 runOnChange:true

alter table "user" alter column password type varchar(120);