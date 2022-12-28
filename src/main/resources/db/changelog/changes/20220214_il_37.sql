--liquibase formatted sql
--changeset ilya_groshev:13 runOnChange:true

alter table "user"
add column last_logon timestamp
