--liquibase formatted sql
--changeset ilya_groshev:1 runOnChange:true context:dev

insert into "user"(id, login, password, created_at, email, enabled)
values (100, 'test', '$2a$10$X757hQ9LkIT415jH/4MFseuEooguW2X/YR6ctbo3yABnX9ZUfOkdi', current_timestamp,
        'test@vktest.com',
        true),
       (101, 'ttest', '$2a$10$X757hQ9LkIT415jH/4MFseuEooguW2X/YR6ctbo3yABnX9ZUfOkdi', current_timestamp,
        'ttest@vktest.com',
        true),
       (102, 'tttest', '$2a$10$X757hQ9LkIT415jH/4MFseuEooguW2X/YR6ctbo3yABnX9ZUfOkdi', current_timestamp,
        'tttest@vktest.com', true);

insert into "group"(id, name, created_at, owner_id)
values (100, 'test1', current_timestamp, (select u.id from "user" as u where u.login = 'test')),
       (101, 'test2', current_timestamp, (select u.id from "user" as u where u.login = 'test')),
       (102, 'test3', current_timestamp, (select u.id from "user" as u where u.login = 'test')),
       (103, 'test4', current_timestamp, (select u.id from "user" as u where u.login = 'test'));

insert into group_members(user_id, group_id)
values ((select u.id from "user" as u where u.login = 'test'),
        (select g.id from "group" as g where g.name = 'test1')),
       ((select u.id from "user" as u where u.login = 'test'),
        (select g.id from "group" as g where g.name = 'test2')),
       ((select u.id from "user" as u where u.login = 'test'),
        (select g.id from "group" as g where g.name = 'test3')),
       ((select u.id from "user" as u where u.login = 'test'),
        (select g.id from "group" as g where g.name = 'test4')),
       ((select u.id from "user" as u where u.login = 'ttest'),
        (select g.id from "group" as g where g.name = 'test1')),
       ((select u.id from "user" as u where u.login = 'ttest'),
        (select g.id from "group" as g where g.name = 'test4')),
       ((select u.id from "user" as u where u.login = 'tttest'),
        (select g.id from "group" as g where g.name = 'test2')),
       ((select u.id from "user" as u where u.login = 'tttest'),
        (select g.id from "group" as g where g.name = 'test4'));

insert into group_post(id, user_id, group_id, text, created_at)
values (100, (select u.id from "user" as u where u.login = 'test'),
        (select g.id from "group" as g where g.name = 'test1'),
        'test',
        current_timestamp),
       (101, (select u.id from "user" as u where u.login = 'test'),
        (select g.id from "group" as g where g.name = 'test2'),
        'test',
        current_timestamp),
       (102, (select u.id from "user" as u where u.login = 'test'),
        (select g.id from "group" as g where g.name = 'test3'),
        'test',
        current_timestamp),
       (103, (select u.id from "user" as u where u.login = 'test'),
        (select g.id from "group" as g where g.name = 'test4'),
        'test',
        current_timestamp)