--liquibase formatted sql
--changeset ilya_groshev:1 runOnChange:true

create table "user" (
    id bigserial,
    login varchar(25) unique not null,
    password varchar(30) not null,
    first_name varchar(50) null,
    middle_name varchar(50) null,
    last_name varchar(50) null,
    birthday date null,
    created_at timestamptz not null,
    primary key(id)
);

create table "group" (
    id bigserial,
    name varchar(100) unique not null,
    description varchar(500) null,
    status varchar(100) null,
    created_at timestamptz not null,
    owner_id bigint not null,
    primary key(id),
    constraint fk_group_owner_id_user_id
        foreign key(owner_id)
        references "user"(id)
);

create table group_members (
    id bigserial,
    user_id bigint not null,
    group_id bigint not null,
    unique(user_id, group_id),
    primary key(id),
    constraint fk_group_members_user_id_user_id
        foreign key(user_id)
        references "user"(id),
    constraint fk_group_members_group_id_group_id
        foreign key(group_id)
        references "group"(id)
);

create table group_post (
    id bigserial,
    user_id bigint not null,
    group_id bigint not null,
    text varchar(1000) not null,
    created_at timestamptz not null,
    primary key(id),
    constraint fk_group_post_user_id_user_id
        foreign key(user_id)
        references "user"(id),
    constraint fk_group_post_group_id_group_id
        foreign key(group_id)
        references "group"(id)
);
