CREATE TABLE refresh_token
(
    id         uuid PRIMARY KEY DEFAULT uuid_generate_v4(),
    token      uuid                        NOT NULL,
    expire     timestamp without time zone NOT NULL,
    account_id uuid                        NOT NULL REFERENCES account (id)
)