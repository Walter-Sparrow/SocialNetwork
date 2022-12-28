--liquibase formatted sql
--changeset ilya_groshev:24 runOnChange:true

create table audit_config
(
    name    text,
    enabled bool not null,
    primary key (name)
);

insert into audit_config(name, enabled)
values ('reaction-audit', true);
