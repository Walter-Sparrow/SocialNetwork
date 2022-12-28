--liquibase formatted sql
--changeset ilya_groshev:28 runOnChange:true

alter table post_comment
    drop constraint fk_post_comment_reply_id_post_comment_id,
    add constraint fk_post_comment_reply_id_post_comment_id
        foreign key (reply_id)
            references post_comment (id)
            on delete set null;

alter table post_comment
    drop constraint fk_post_comment_parent_id_post_comment_id,
    add constraint fk_post_comment_parent_id_post_comment_id
        foreign key (parent_id)
            references post_comment (id)
            on delete cascade;