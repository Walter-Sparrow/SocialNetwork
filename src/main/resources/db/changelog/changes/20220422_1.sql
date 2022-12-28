--liquibase formatted sql
--changeset ilya_groshev:26 runOnChange:true

insert into audit_config(name, enabled)
values ('update-post-audit', true);