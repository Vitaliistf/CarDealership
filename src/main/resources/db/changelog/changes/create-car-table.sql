CREATE TABLE car
(
    id                  BIGINT AUTO_INCREMENT PRIMARY KEY,
    brand               VARCHAR(50)                                                                                       NOT NULL,
    model               VARCHAR(50)                                                                                       NOT NULL,
    year                INT                                                                                               NOT NULL CHECK (year BETWEEN 1900 AND 2100),
    color               VARCHAR(50)                                                                                       NOT NULL,
    transmission        ENUM ('MANUAL', 'AUTOMATIC')                                                                      NOT NULL,
    fuel                ENUM ('PETROL', 'DIESEL', 'ELECTRIC', 'HYBRID', 'LPG')                                            NOT NULL,
    body_type           ENUM ('SEDAN', 'HATCHBACK', 'SUV', 'COUPE', 'CONVERTIBLE', 'WAGON', 'PICKUP', 'MINIVAN', 'SPORT') NOT NULL,
    status              ENUM ('AVAILABLE', 'ORDERED', 'ARCHIVED')                                                         NOT NULL,
    engine_displacement DOUBLE                                                                                            NOT NULL CHECK (engine_displacement >= 0.5),
    mileage             INT                                                                                               NOT NULL CHECK (mileage >= 0),
    description         VARCHAR(1000)                                                                                     NOT NULL,
    price               DOUBLE                                                                                            NOT NULL CHECK (price >= 1),
    technical_condition ENUM ('EXCELLENT', 'GOOD', 'FAIR', 'POOR', 'DAMAGED')                                             NOT NULL,
    owner_id            BIGINT                                                                                            NOT NULL,
    photo_url           VARCHAR(255)                                                                                      NULL,
    CONSTRAINT fk_car_owner FOREIGN KEY (owner_id) REFERENCES app_user (id)
);
