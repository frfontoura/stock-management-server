--liquibase formatted sql

--changeset frfontoura:3
--comment: stm_assets table creation
CREATE TABLE stm_assets (
	id serial NOT NULL,
	portfolio_id int4 NOT NULL,
	symbol varchar(10) NOT NULL,
    avg_price numeric(19,2) NOT NULL,
    last_price numeric(19,2) NOT NULL,
    amount int NOT NULL,
    last_date date NOT NULL,
    asset_type varchar(10),
	CONSTRAINT stm_assets_pk PRIMARY KEY (id),
	CONSTRAINT stm_assets_portfolio_fk FOREIGN KEY (portfolio_id) REFERENCES stm_portfolios(id) ON DELETE RESTRICT
);
--rollback drop table stm_assets;