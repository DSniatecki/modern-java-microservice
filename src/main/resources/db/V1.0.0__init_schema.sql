CREATE TABLE car
(
    id          CHAR(36) PRIMARY KEY NOT NULL,
    brand       VARCHAR(64)          NOT NULL,
    model       VARCHAR(128)         NOT NULL,
    produced_at DATE                 NOT NULL,
    created_at  TIMESTAMP            NOT NULL,
    updated_at  TIMESTAMP,
    is_deleted  BOOLEAN              NOT NULL
);


