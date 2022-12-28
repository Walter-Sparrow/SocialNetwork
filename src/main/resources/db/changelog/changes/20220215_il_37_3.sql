--liquibase formatted sql
--changeset ilya_groshev:15 runOnChange:true

alter table "user"
rename last_recommendation_email_sent_on to recommended_email_send_at