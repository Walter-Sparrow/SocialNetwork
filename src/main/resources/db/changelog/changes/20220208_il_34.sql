--liquibase formatted sql
--changeset ilya_groshev:9 runOnChange:true

alter table group_post
    drop constraint fk_group_post_group_id_group_id,
    add constraint fk_group_post_group_id_group_id
        foreign key (group_id)
            references "group" (id)
            on delete cascade;

alter table group_members
    drop constraint fk_group_members_group_id_group_id,
    add constraint fk_group_members_group_id_group_id
        foreign key (group_id)
            references "group" (id)
            on delete cascade;

alter table likes
    drop constraint fk_likes_post_id_group_post_id,
    add constraint fk_likes_post_id_group_post_id
        foreign key (post_id)
            references group_post (id)
            on delete cascade;
