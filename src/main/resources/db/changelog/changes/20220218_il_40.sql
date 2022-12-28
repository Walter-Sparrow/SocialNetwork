--liquibase formatted sql
--changeset ilya_groshev:17 runOnChange:true

CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

alter table confirmation_token
    alter column token set default uuid_generate_v4();

alter table confirmation_token
    drop constraint confirmation_token_pkey;

alter table confirmation_token
    drop column id;

alter table confirmation_token
    rename column token to id;

alter table confirmation_token
    add primary key (id);