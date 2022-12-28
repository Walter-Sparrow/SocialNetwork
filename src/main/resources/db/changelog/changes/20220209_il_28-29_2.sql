--liquibase formatted sql
--changeset ilya_groshev:11 runOnChange:true

alter table "user"
    add column profile_picture_id bigint,
    add constraint fk_user_profile_picture_id_image_id
        foreign key (profile_picture_id)
            references image (id)
            on delete set null;