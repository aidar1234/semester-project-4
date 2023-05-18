CREATE TABLE refresh_token
(
    id         bigserial PRIMARY KEY,
    token      uuid                        NOT NULL,
    expire     timestamp without time zone NOT NULL,
    account_id uuid UNIQUE                 NOT NULL REFERENCES account (id)
)