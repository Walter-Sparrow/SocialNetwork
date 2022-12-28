--liquibase formatted sql
--changeset ilya_groshev:19 runOnChange:true

alter table confirmation_token
alter column created_at type timestamptz