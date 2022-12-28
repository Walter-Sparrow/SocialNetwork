--liquibase formatted sql
--changeset ilya_groshev:13 runOnChange:true

alter table "user"
    add column email varchar(255) unique;

update "user"
set email = 'test1@gmail.com'
where id = 1;

update "user"
set email = 'test2@gmail.com'
where id = 2;

update "user"
set email = 'test3@gmail.com'
where id = 3;

alter table "user"
    alter column email set not null;

alter table "user"
    add column enabled boolean not null default false;

update "user"
set enabled = true;

create table confirmation_token
(
    id         bigserial,
    token      uuid      not null unique,
    user_id    bigint    not null unique,
    created_at timestamp not null,
    primary key (id),
    constraint fk_confirmation_token_user_id_user_id
        foreign key (user_id)
            references "user" (id)
            on delete cascade
);