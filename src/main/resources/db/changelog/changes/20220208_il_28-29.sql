--liquibase formatted sql
--changeset ilya_groshev:10 runOnChange:true

create table image
(
    id     bigserial,
    data   bytea       not null,
    size   bigint      not null,
    width  int         not null,
    height int         not null,
    type   varchar(10) not null,
    primary key (id)
);