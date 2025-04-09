--Users
DROP SEQUENCE IF EXISTS user_seq;
CREATE SEQUENCE user_seq start 1 increment 1;

DROP TABLE IF EXISTS users CASCADE;

CREATE TABLE users(
    id int8 not null,
    archive boolean not null,
    email VARCHAR(255),
    name VARCHAR(255),
    password VARCHAR(255),
    role VARCHAR(255),
    primary key (id)
);

--Bucket
DROP SEQUENCE IF EXISTS bucket_seq;
CREATE SEQUENCE bucket_seq start 1 increment 1;

DROP TABLE IF EXISTS buckets CASCADE;
CREATE TABLE buckets (
    id int8 not null,
    user_id int8,
    primary key (id)
);

--Link between bucket and user
ALTER TABLE IF EXISTS buckets
    ADD CONSTRAINT bucket_fk_user
        FOREIGN KEY (user_id) REFERENCES users;


-- Category
DROP SEQUENCE IF EXISTS category_seq;
CREATE SEQUENCE category_seq start 1 increment 1;

DROP TABLE IF EXISTS categories CASCADE;
CREATE TABLE categories (
    id int8 not null,
    title VARCHAR(255),
    primary key (id)
);

--Product
DROP TABLE IF EXISTS product_seq;
CREATE SEQUENCE product_seq start 1 increment 1;

DROP TABLE IF EXISTS products CASCADE;
CREATE TABLE products (
    id int8 not null,
    price numeric(19,2),
    title VARCHAR(255),
    primary key (id)
);

--Category and product
DROP TABLE IF EXISTS products_categories CASCADE;
CREATE TABLE products_categories (
    product_id int8 not null,
    category_id int8 not null
);

--добавил
--alter table if exists products_categories
--    add constraint products_categories_fk_categories
--        foreign key (category_id) references categories

ALTER TABLE IF EXISTS products_categories
    ADD CONSTRAINT products_categories_fk_category
        FOREIGN KEY (product_id) REFERENCES products;

--Product in bucket
DROP TABLE IF EXISTS buckets_products CASCADE;
CREATE TABLE buckets_products (
    bucket_id int8 not null,
    product_id int8 not null
);

ALTER TABLE IF EXISTS buckets_products
    ADD CONSTRAINT buckets_products_fk_products
        FOREIGN KEY (product_id) REFERENCES products;

ALTER TABLE IF EXISTS buckets_products
    ADD CONSTRAINT buckets_products_fk_bucket
        FOREIGN KEY (bucket_id) REFERENCES buckets;

--Orders
DROP SEQUENCE IF EXISTS order_seq;
CREATE SEQUENCE order_seq start 1 increment 1;

DROP TABLE IF EXISTS orders CASCADE;
CREATE TABLE orders(
    id int8 not null,
    address VARCHAR(255),
    updated timestamp,
    created timestamp,
    order_status VARCHAR(255),
    sum numeric(19,2),
    user_id int8,
    primary key (id)
);

ALTER TABLE IF EXISTS orders
    ADD CONSTRAINT orders_fk_user
        FOREIGN KEY (user_id) REFERENCES users;

--Order details
DROP SEQUENCE IF EXISTS order_details_seq;
CREATE SEQUENCE order_details_seq start 1 increment 1;

DROP TABLE IF EXISTS orders_details CASCADE;
CREATE TABLE orders_details(
    id int8 not null,
    amount numeric(19,2),
    price numeric(19,2),
    order_id int8,
    product_id int8,
    details_id int8 not null,
    primary key (id)
);

ALTER TABLE IF EXISTS orders_details
    ADD CONSTRAINT orders_details_fk_order
        FOREIGN KEY (order_id) REFERENCES orders;

ALTER TABLE IF EXISTS orders_details
    ADD CONSTRAINT orders_details_fk_products
        FOREIGN KEY (product_id) REFERENCES products;


