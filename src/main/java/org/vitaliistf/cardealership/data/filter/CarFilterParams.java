package org.vitaliistf.cardealership.data.filter;

import lombok.Getter;
import lombok.Setter;
import org.vitaliistf.cardealership.data.enums.BodyType;
import org.vitaliistf.cardealership.data.enums.FuelType;
import org.vitaliistf.cardealership.data.enums.TechnicalCondition;
import org.vitaliistf.cardealership.data.enums.Transmission;

/**
 * Represents parameters for filtering cars in the car dealership system.
 */
@Getter
@Setter
public class CarFilterParams {
    /** The brand of the car. */
    private String brand;

    /** The model of the car. */
    private String carModel;

    /** The minimum manufacturing year of the car. */
    private Integer yearFrom;

    /** The maximum manufacturing year of the car. */
    private Integer yearTo;

    /** The body type of the car. */
    private BodyType bodyType;

    /** The fuel type of the car. */
    private FuelType fuelType;

    /** The transmission type of the car. */
    private Transmission transmission;

    /** The technical condition of the car. */
    private TechnicalCondition condition;

    /** The color of the car. */
    private String color;

    /** The minimum engine displacement of the car. */
    private Double engineDisplacementFrom;

    /** The maximum engine displacement of the car. */
    private Double engineDisplacementTo;

    /** The minimum mileage of the car. */
    private Integer mileageFrom;

    /** The maximum mileage of the car. */
    private Integer mileageTo;

    /** The minimum price of the car. */
    private Double priceFrom;

    /** The maximum price of the car. */
    private Double priceTo;

    /** The field to sort by. */
    private String sortBy = "id";

    /** The order of sorting (ASC or DESC). */
    private String sortOrder = "ASC";

    /** The number of results to retrieve. */
    private int size = 12;
}
