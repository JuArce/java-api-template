CREATE TABLE IF NOT EXISTS users
(
    id         SERIAL PRIMARY KEY,
    email      VARCHAR(255) NOT NULL,
    username   VARCHAR(255) NOT NULL,
    password   VARCHAR(255) NOT NULL,
    enabled    BOOLEAN      NOT NULL,
    created_at TIMESTAMP    NOT NULL DEFAULT NOW(),
    unique (email),
    unique (username)
);

-- INSERT INTO users (email, username, password, enabled)
--     VALUES('jason@bravo.com', 'jason', 'password', true);
--
-- SELECT nextval('users_id_seq');