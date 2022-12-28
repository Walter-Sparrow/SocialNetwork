--liquibase formatted sql
--changeset ilya_groshev:3 runOnChange: true

insert into "group"(name, description, status, created_at, owner_id)
values
    ('YouTube Life', 'YouTube is an American online video sharing and social media platform owned by Google. It was launched on February 14, 2005, by Steve Chen, Chad Hurley, and Jawed Karim. It is the second most visited website, right after Google itself.', 'like and subscribe.', clock_timestamp(), 1),
    ('Twitch', 'We are Twitch: a global community of millions who come together each day to create their own entertainment.', null, clock_timestamp(), 1),
    ('Programming Wisdom', 'Programming wisdom and quotes throughout the years.', 'The Knuth, the whole Knuth, and nothing but the Knuth, so help me Codd.', clock_timestamp(), 3);
