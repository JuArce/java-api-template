CREATE TABLE IF NOT EXISTS users
(
    id         SERIAL PRIMARY KEY,
    email      VARCHAR(255) NOT NULL,
    username   VARCHAR(255) NOT NULL,
    password   VARCHAR(255) NOT NULL,
    enabled    BOOLEAN      NOT NULL,
    created_at TIMESTAMP    NOT NULL DEFAULT current_timestamp,
    unique (email),
    unique (username)
);
