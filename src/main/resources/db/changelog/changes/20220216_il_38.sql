--liquibase formatted sql
--changeset ilya_groshev:16 runOnChange:true

alter table "group"
    add column group_picture_id bigint,
    add constraint fk_group_group_picture_id_image_id
        foreign key (group_picture_id)
            references image (id)
            on delete set null;