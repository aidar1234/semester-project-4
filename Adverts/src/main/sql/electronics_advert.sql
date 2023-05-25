CREATE TABLE electronics_advert
(
    id           uuid PRIMARY KEY,
    name         varchar(64)                 NOT NULL,
    price        double precision            NOT NULL,
    phone        varchar(12)                 NOT NULL,
    locality     varchar(32)                 NOT NULL,
    description  varchar(1024)               NOT NULL,
    created_date timestamp without time zone NOT NULL DEFAULT now(),
    updated_date timestamp without time zone NOT NULL DEFAULT now(),
    account_id    uuid                        NOT NULL REFERENCES account (id)
)