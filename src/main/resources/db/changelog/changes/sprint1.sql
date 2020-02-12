--liquibase formatted sql

--changeset frfontoura:1
--comment: stm_users table creation
CREATE TABLE stm_users (
	id serial NOT NULL,
	name varchar(100) NOT NULL,
	username varchar(50) NOT NULL,
	email varchar(100) NOT NULL,
	password varchar(60) NOT NULL,
	role varchar(15) NOT NULL,
	CONSTRAINT stm_users_pk PRIMARY KEY (id),
	CONSTRAINT stm_users_email_un UNIQUE (email),
	CONSTRAINT stm_users_username_un UNIQUE (username)
);
--rollback drop table stm_users;
