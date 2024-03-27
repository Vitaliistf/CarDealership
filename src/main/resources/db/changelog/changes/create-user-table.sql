CREATE TABLE app_user
(
    id           BIGINT AUTO_INCREMENT PRIMARY KEY,
    first_name   VARCHAR(50)  NOT NULL,
    last_name    VARCHAR(50)  NOT NULL,
    email        VARCHAR(255) NOT NULL UNIQUE,
    password     VARCHAR(255) NOT NULL,
    phone_number VARCHAR(20)  NOT NULL UNIQUE,
    address      VARCHAR(100) NOT NULL
);
