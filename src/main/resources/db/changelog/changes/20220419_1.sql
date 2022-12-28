--liquibase formatted sql
--changeset ilya_groshev:22 runOnChange:true

create or replace function check_auth_provider_not_local_when_password_is_null() returns trigger as
'
    begin
        if (new.password is null and new.auth_provider_id = (select id
                                                             from auth_provider
                                                             where name = ''local'')) then
            raise exception ''password cannot be null with local auth provider'';
        end if;

        return new;
    end;
' language plpgsql;

create trigger auth_provider_not_local_when_password_is_null
    before insert
    on "user"
    for each row
execute procedure check_auth_provider_not_local_when_password_is_null();