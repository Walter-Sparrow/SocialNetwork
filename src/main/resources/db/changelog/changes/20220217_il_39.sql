--liquibase formatted sql
--changeset ilya_groshev:16 runOnChange:true

create table link
(
    id          bigserial,
    domain      text,
    url         text not null unique,
    title       varchar(255),
    description text,
    image       text,
    imageAlt    text,
    primary key (id)
);

alter table group_post
    add column link_attachment_id bigint,
    add constraint fk_group_post_link_attachment_id_link_id
        foreign key (link_attachment_id)
            references link (id)
            on delete set null;