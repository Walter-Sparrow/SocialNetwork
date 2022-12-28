--liquibase formatted sql
--changeset ilya_groshev:7 runOnChange:true

create table likes
(
    id      bigserial,
    user_id bigint not null,
    post_id bigint not null,
    isLike  bool   not null,
    unique (user_id, post_id),
    primary key (id),
    constraint fk_likes_user_id_user_id
        foreign key (user_id)
            references "user" (id),
    constraint fk_likes_post_id_group_post_id
        foreign key (post_id)
            references group_post (id)
);