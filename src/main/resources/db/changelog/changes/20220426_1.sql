--liquibase formatted sql
--changeset ilya_groshev:27 runOnChange:true

alter table post_comment
    add column createdAt timestamptz not null default current_timestamp;

alter table post_comment
    add column reply_id bigint;

alter table post_comment
    add constraint fk_post_comment_reply_id_post_comment_id
        foreign key (reply_id)
            references post_comment (id);

create or replace function set_parent_if_same_thread() returns trigger as
'
    declare
        reply_thread_id bigint;
    begin
        if new.reply_id is null then
            return new;
        end if;

        select parent_id
        into reply_thread_id
        from post_comment
        where id = new.reply_id;

        if reply_thread_id is null then
            new.parent_id = new.reply_id;
        else
            new.parent_id = reply_thread_id;
        end if;

        return new;
    end;
' language plpgsql;

create trigger change_parent_if_same_thread_trigger
    before insert
    on post_comment
    for each row
execute procedure set_parent_if_same_thread();