--liquibase formatted sql
--changeset ilya_groshev:5 runOnChange:true

alter table "user" alter column password type varchar(32);