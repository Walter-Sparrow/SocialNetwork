--liquibase formatted sql
--changeset ilya_groshev:21 runOnChange:true

create table auth_provider
(
    id   bigserial,
    name text not null unique,
    primary key (id)
);

insert into auth_provider(name)
values ('local'),
       ('google');

create or replace function get_local_auth_provider_id() returns bigint
    language sql as
'
    select id
    from auth_provider
    where name = ''local'';
';

alter table "user"
    add column auth_provider_id bigint not null default get_local_auth_provider_id();

alter table "user"
    add constraint fk_user_auth_provider_id_auth_provider_id
        foreign key (auth_provider_id)
            references auth_provider (id);