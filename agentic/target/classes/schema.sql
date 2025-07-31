CREATE TABLE IF NOT EXISTS FILE_META
(
    FILE_ID      uuid        not null,
    TS           BIGINT      not null,
    USER_ID      varchar(64) not null,
    FILE_NAME    varchar(128),
    VECTORIZABLE boolean     not null,
    VECTORIZED   boolean     not null,
    MIMETYPE     varchar(128),
    URI          varchar(256),
    PRIMARY KEY (FILE_ID, TS)
    );


CREATE INDEX IF NOT EXISTS IDX_FILE_USER_ID ON FILE_META (USER_ID);
