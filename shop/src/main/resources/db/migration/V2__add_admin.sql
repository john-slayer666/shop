INSERT INTO users(id,archive, email, name, password, role)
VALUES(1, false, 'john_slayer@gmail.com','admin', '$2a$10$ANDVW38bA3RjVKnWLczk5OsN7LZoi287pW9Kts5DbG8GKBAREZdc2', 'ADMIN');

ALTER SEQUENCE user_seq RESTART WITH 2;