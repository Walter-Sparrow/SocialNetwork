--liquibase formatted sql
--changeset ilya_groshev:12 runOnChange:true

create table attachment
(
    id       bigserial,
    post_id  bigint not null,
    image_id bigint not null,
    primary key (id),
    constraint fk_attachment_post_id_group_post_id
        foreign key (post_id)
            references group_post (id)
            on delete cascade,
    constraint fk_attachment_image_id_image_id
        foreign key (image_id)
            references image (id)
);