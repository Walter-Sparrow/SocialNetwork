--liquibase formatted sql
--changeset ilya_groshev:23 runOnChange:true

insert into auth_provider(name)
values ('github');