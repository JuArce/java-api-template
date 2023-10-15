INSERT INTO users(id, email, username, password, enabled, created_at)
VALUES (1, 'user@email.com', 'user', 'password', true, current_timestamp);
INSERT INTO users(id, email, username, password, enabled, created_at)
VALUES (2, 'admin@email', 'admin', 'password', true, current_timestamp);

ALTER SEQUENCE users_id_seq RESTART WITH 100;