--liquibase formatted sql
--changeset ilya_groshev:20 runOnChange:true

create table chat_room
(
    id               bigserial,
    participant_id_1 bigint not null,
    participant_id_2 bigint not null,
    unique (participant_id_1, participant_id_2),
    primary key (id),
    constraint fk_chat_room_participant_id_1_user_id
        foreign key (participant_id_1)
            references "user" (id),
    constraint fk_chat_room_participant_id_2_user_id
        foreign key (participant_id_2)
            references "user" (id)
);

create table direct_message
(
    id           bigserial,
    chat_room_id bigint not null,
    peer_id      bigint not null,
    from_id      bigint not null,
    text         text,
    date         timestamptz,
    primary key (id),
    constraint fk_direct_message_chat_room_id_chat_room_id
        foreign key (chat_room_id)
            references chat_room (id),
    constraint fk_direct_message_peer_id_user_id
        foreign key (peer_id)
            references "user" (id),
    constraint fk_direct_message_from_id_user_id
        foreign key (from_id)
            references "user" (id)
);