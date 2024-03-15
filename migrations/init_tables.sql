--liquibase formatted sql

--changeset vladislav77777:1
CREATE TABLE IF NOT EXISTS telegram_chat
(
    id
    BIGINT,
    registered_at
    TIMESTAMP
    WITH
    TIME
    ZONE
    NOT
    NULL,
    PRIMARY
    KEY
(
    id
)
    );

--changeset vladislav77777:2
CREATE TABLE IF NOT EXISTS link
(
    id
    BIGINT
    GENERATED
    ALWAYS AS
    IDENTITY,
    url
    VARCHAR
(
    256
) UNIQUE NOT NULL,
    last_updated_at TIMESTAMP WITH TIME ZONE,
                                  PRIMARY KEY (id)
    );
--changeset vladislav77777:3
CREATE TABLE IF NOT EXISTS assignment
(
    chat_id
    BIGINT
    REFERENCES
    telegram_chat
(
    id
) ON DELETE CASCADE,
    link_id BIGINT REFERENCES link
(
    id
)
  ON DELETE CASCADE,
    PRIMARY KEY
(
    chat_id,
    link_id
)
    )
