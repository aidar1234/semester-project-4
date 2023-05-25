CREATE TABLE transport_advert
(
    id            uuid PRIMARY KEY                     DEFAULT uuid_generate_v4(),
    name          varchar(64)                 NOT NULL,
    price         double precision            NOT NULL,
    phone         varchar(12)                 NOT NULL,
    locality      varchar(32)                 NOT NULL,
    brand         varchar(32)                 NOT NULL,
    is_new        boolean                     NOT NULL,
    is_registered boolean                     NOT NULL, -- is registered with the traffic police
    kind          varchar(10)                 NOT NULL,
    mileage       integer                     NOT NULL,
    description   varchar(1024)               NOT NULL,
    created_date  timestamp without time zone NOT NULL default now(),
    updated_date  timestamp without time zone NOT NULL default now(),
    account_id    uuid                        NOT NULL REFERENCES account (id)
)
