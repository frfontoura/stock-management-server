--liquibase formatted sql

--changeset frfontoura:2
--comment: stm_portfolio table creation
CREATE TABLE stm_portfolios (
	id serial NOT NULL,
	user_id int4 NOT NULL,
	name varchar(50) NOT NULL,
	description varchar(280) NOT NULL,
	CONSTRAINT stm_portfolio_pk PRIMARY KEY (id),
	CONSTRAINT stm_portfolio_user_fk FOREIGN KEY (user_id) REFERENCES stm_users(id) ON DELETE RESTRICT
);
--rollback drop table stm_portfolio;
