INSERT INTO users(id,archive, email, name, password, role, bucket_id)
VALUES(1, false, 'john_slayer@gmail.com','admin', '5696', 'ADMIN', null);

ALTER SEQUENCE user_seq RESTART WITH 2;