--liquibase formatted sql
--changeset ilya_groshev:8 runOnChange:true

update "user"
set password = '$2a$10$U2G/c5936pgupX1VZxtiq.M.Cai/2s87dv73k9YOGWziPBthGllW6'
where id = id