--liquibase formatted sql
--changeset ilya_groshev:4 runOnChange: true

insert into "group_post"(user_id, group_id, text, created_at)
values
    (1, 2, 'Hey guys, check out my streams!', clock_timestamp()),
    (3, 1, 'Today is the anniversary of the first video on our platform!', clock_timestamp()),
    (1, 2, 'Sorry for the duplicates, but you really shouldn''t miss my stream!!!', clock_timestamp());;
