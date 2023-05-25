CREATE TABLE file
(
    id bigserial PRIMARY KEY,
    hash varchar(64) UNIQUE NOT NULL, --md5 hash
    size bigint NOT NULL,
    name varchar(64) UNIQUE NOT NULL,
    created_date  timestamp without time zone NOT NULL DEFAULT now(),
    updated_date  timestamp without time zone NOT NULL DEFAULT now()
)