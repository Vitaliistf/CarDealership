CREATE TABLE car_order
(
    id           BIGINT AUTO_INCREMENT PRIMARY KEY,
    seller_id    BIGINT                                             NOT NULL,
    buyer_id     BIGINT                                             NOT NULL,
    car_id       BIGINT                                             NOT NULL,
    order_date   DATE                                               NOT NULL,
    order_status ENUM ('NEW', 'CONFIRMED', 'CANCELED', 'COMPLETED') NOT NULL,
    CONSTRAINT fk_order_seller FOREIGN KEY (seller_id) REFERENCES app_user (id),
    CONSTRAINT fk_order_buyer FOREIGN KEY (buyer_id) REFERENCES app_user (id),
    CONSTRAINT fk_order_car FOREIGN KEY (car_id) REFERENCES car (id)
);
