--liquibase formatted sql
--changeset ilya_groshev:14 runOnChange:true

alter table "user"
add column last_recommendation_email_sent_on timestamp