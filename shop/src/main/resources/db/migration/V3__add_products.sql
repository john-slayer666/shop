INSERT INTO products(id, price, title)
VALUES  (1, 450.0, 'Slayer'),
        (2, 45.0, 'Doom'),
        (3, 350.0, 'Arch Enemy'),
        (4, 50.0, 'Deicide'),
        (5, 53.0, 'Sepultura');
ALTER SEQUENCE product_seq RESTART WITH 6;