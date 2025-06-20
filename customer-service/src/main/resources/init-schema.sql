DROP SCHEMA IF EXISTS client CASCADE;

CREATE SCHEMA client;

CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

CREATE TABLE client.customers
(
    id uuid NOT NULL,
    username character varying COLLATE pg_catalog."default" NOT NULL,
    first_name character varying COLLATE pg_catalog."default" NOT NULL,
    last_name character varying COLLATE pg_catalog."default" NOT NULL,
    CONSTRAINT customers_pkey PRIMARY KEY (id)
);

DROP MATERIALIZED VIEW IF EXISTS client.order_customer_m_view;

CREATE MATERIALIZED VIEW client.order_customer_m_view
TABLESPACE pg_default
AS
 SELECT id,
    username,
    first_name,
    last_name
   FROM client.customers
WITH DATA;

refresh materialized VIEW client.order_customer_m_view;

DROP function IF EXISTS client.refresh_order_customer_m_view;

CREATE OR replace function client.refresh_order_customer_m_view()
returns trigger
AS '
BEGIN
    refresh materialized VIEW client.order_customer_m_view;
    return null;
END;
'  LANGUAGE plpgsql;

DROP trigger IF EXISTS refresh_order_customer_m_view ON client.customers;

CREATE trigger refresh_order_customer_m_view
after INSERT OR UPDATE OR DELETE OR truncate
ON client.customers FOR each statement
EXECUTE PROCEDURE client.refresh_order_customer_m_view();