--liquibase formatted sql
--changeset ilya_groshev:4 runOnChange: true

insert into "group_members"(user_id, group_id)
values
    (1, 2),
    (2, 2),
    (3, 1);
