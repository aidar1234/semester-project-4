CREATE TABLE advert_file
(
    advert_id uuid NOT NULL,
    file_id bigint NOT NULL,
    PRIMARY KEY (advert_id, file_id)
)