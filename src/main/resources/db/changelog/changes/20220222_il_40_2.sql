--liquibase formatted sql
--changeset ilya_groshev:18 runOnChange:true

create table post_comment
(
    id        bigserial,
    user_id   bigint not null,
    post_id   bigint not null,
    parent_id bigint,
    text      text,
    primary key (id),
    constraint fk_post_comment_user_id_user_id
        foreign key (user_id)
            references "user" (id)
            on delete cascade,
    constraint fk_post_comment_post_id_group_post_id
        foreign key (post_id)
            references "group_post" (id)
            on delete cascade,
    constraint fk_post_comment_parent_id_post_comment_id
        foreign key (parent_id)
            references "post_comment" (id)
            on delete cascade
);