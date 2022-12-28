--liquibase formatted sql
--changeset ilya_groshev:25 runOnChange:true

insert into audit_config(name, enabled)
values ('login-audit', true);