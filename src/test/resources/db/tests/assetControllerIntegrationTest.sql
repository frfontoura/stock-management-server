--liquibase formatted sql

--changeset frfontoura:2
--comment: Add User (imgroot)
INSERT INTO stm_portfolios (user_id,name,description) VALUES (1,'Stock Portfolio','Stock portfolio');