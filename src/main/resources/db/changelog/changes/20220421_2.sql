--liquibase formatted sql
--changeset ilya_groshev:24 runOnChange:true

insert into audit_config(name, enabled)
values ('follow-group-audit', true),
       ('unfollow-group-audit', true);
