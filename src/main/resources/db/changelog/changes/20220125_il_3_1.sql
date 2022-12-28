--liquibase formatted sql
--changeset ilya_groshev:2 runOnChange: true

insert into "user"(login, password, first_name, middle_name, last_name, birthday, created_at)
values
    ('admin', '098f6bcd4621d373cade4e832627b4f6', 'Potap', 'Timurovich', 'Krutikov', '1943-03-04', clock_timestamp()),
    ('cdawgva', '1a1dc91c907325c69271ddf0c944bc72', 'Matvei', 'Larionovich', 'Tokmakov', '1965-02-14', clock_timestamp()),
    ('pewdiepie', '81b616fbff55b8698d44852f45c08630', 'Klavdiy', 'Kirillovich', 'Protasov', '2000-11-11', clock_timestamp());
