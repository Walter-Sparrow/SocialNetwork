--liquibase formatted sql
--changeset ilya_groshev:20 runOnChange:true

update "user"
set password = '$2a$10$NUZX3iZCbooV5p13KYUQausxPyt7/kZF75cq5l32Mz7oRihRDhW1O'