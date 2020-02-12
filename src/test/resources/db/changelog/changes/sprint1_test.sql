--liquibase formatted sql

--changeset frfontoura:1
--comment: Add User (password: imgroot)
INSERT INTO stm_users (name,username,email,password,role) VALUES ('Peter Quill','starlord','starlord@avengers.com','$2a$10$RF.Ri/OKidQ.bHekFIf4H.uLXwnbHkVQa5ukF/iQ90v6UmQfYjcDq','REGULAR_USER');