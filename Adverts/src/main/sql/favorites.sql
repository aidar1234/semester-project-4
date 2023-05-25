CREATE TABLE favorites
(
    account_id uuid NOT NULL REFERENCES account(id),
    advert_id uuid NOT NULL,
    PRIMARY KEY (account_id, advert_id)
)