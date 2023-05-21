CREATE TABLE account
(
    id            uuid PRIMARY KEY                     DEFAULT uuid_generate_v4(),
    email         varchar(255) UNIQUE         NOT NULL,
    hash_password varchar(72)                 NOT NULL,
    state         varchar(16)                 NOT NULL default 'NOT_CONFIRMED',
    role          varchar(16)                 NOT NULL default 'USER',
    first_name    varchar(32)                 NOT NULL,
    last_name     varchar(32)                 NOT NULL,
    phone         varchar(12)                 NOT NULL,
    locality      varchar(32)                 NOT NULL,
    created_date  timestamp without time zone NOT NULL DEFAULT now(),
    updated_date  timestamp without time zone NOT NULL DEFAULT now()
)