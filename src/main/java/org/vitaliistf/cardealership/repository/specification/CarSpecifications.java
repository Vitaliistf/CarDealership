package org.vitaliistf.cardealership.repository.specification;

import org.springframework.data.jpa.domain.Specification;
import org.vitaliistf.cardealership.data.Car;
import org.vitaliistf.cardealership.data.enums.*;

/**
 * Utility class providing various specifications for querying Car entities.
 */
public class CarSpecifications {

    /**
     * Creates a specification to filter cars by brand containing the specified value.
     *
     * @param brand the brand value to search for
     * @return the Specification for brand filtering
     */
    public static Specification<Car> brandContains(String brand) {
        return (root, query, builder) ->
                builder.like(root.get("brand"), "%" + brand + "%");
    }

    /**
     * Creates a specification to filter cars by model containing the specified value.
     *
     * @param model the model value to search for
     * @return the Specification for model filtering
     */
    public static Specification<Car> modelContains(String model) {
        return (root, query, builder) ->
                builder.like(root.get("model"), "%" + model + "%");
    }

    /**
     * Creates a specification to filter cars by year greater than or equal to the specified value.
     *
     * @param year the minimum year value to search for
     * @return the Specification for year filtering
     */
    public static Specification<Car> yearGreaterThanOrEqual(int year) {
        return (root, query, builder) ->
                builder.greaterThanOrEqualTo(root.get("year"), year);
    }

    /**
     * Creates a specification to filter cars by year less than or equal to the specified value.
     *
     * @param year the maximum year value to search for
     * @return the Specification for year filtering
     */
    public static Specification<Car> yearLessThanOrEqual(int year) {
        return (root, query, builder) ->
                builder.lessThanOrEqualTo(root.get("year"), year);
    }

    /**
     * Creates a specification to filter cars by body type.
     *
     * @param bodyType the body type to filter by
     * @return the Specification for body type filtering
     */
    public static Specification<Car> bodyType(BodyType bodyType) {
        return (root, query, builder) ->
                builder.equal(root.get("bodyType"), bodyType);
    }

    /**
     * Creates a specification to filter cars by fuel type.
     *
     * @param fuelType the fuel type to filter by
     * @return the Specification for fuel type filtering
     */
    public static Specification<Car> fuelType(FuelType fuelType) {
        return (root, query, builder) ->
                builder.equal(root.get("fuelType"), fuelType);
    }

    /**
     * Creates a specification to filter cars by transmission type.
     *
     * @param transmission the transmission type to filter by
     * @return the Specification for transmission type filtering
     */
    public static Specification<Car> transmission(Transmission transmission) {
        return (root, query, builder) ->
                builder.equal(root.get("transmission"), transmission);
    }

    /**
     * Creates a specification to filter cars by technical condition.
     *
     * @param condition the technical condition to filter by
     * @return the Specification for technical condition filtering
     */
    public static Specification<Car> condition(TechnicalCondition condition) {
        return (root, query, builder) ->
                builder.equal(root.get("condition"), condition);
    }

    /**
     * Creates a specification to filter cars by status.
     *
     * @param status the status to filter by
     * @return the Specification for status filtering
     */
    public static Specification<Car> carStatus(CarStatus status) {
        return (root, query, builder) ->
                builder.equal(root.get("status"), status);
    }

    /**
     * Creates a specification to filter cars by color containing the specified value.
     *
     * @param color the color value to search for
     * @return the Specification for color filtering
     */
    public static Specification<Car> colorContains(String color) {
        return (root, query, builder) ->
                builder.like(root.get("color"), "%" + color + "%");
    }

    /**
     * Creates a specification to filter cars by engine displacement greater than or equal to the specified value.
     *
     * @param engineDisplacement the minimum engine displacement value to search for
     * @return the Specification for engine displacement filtering
     */
    public static Specification<Car> engineDisplacementGreaterThanOrEqual(Double engineDisplacement) {
        return (root, query, builder) ->
                builder.greaterThanOrEqualTo(root.get("engineDisplacement"), engineDisplacement);
    }

    /**
     * Creates a specification to filter cars by engine displacement less than or equal to the specified value.
     *
     * @param engineDisplacement the maximum engine displacement value to search for
     * @return the Specification for engine displacement filtering
     */
    public static Specification<Car> engineDisplacementLessThanOrEqual(Double engineDisplacement) {
        return (root, query, builder) ->
                builder.lessThanOrEqualTo(root.get("engineDisplacement"), engineDisplacement);
    }

    /**
     * Creates a specification to filter cars by mileage greater than or equal to the specified value.
     *
     * @param mileage the minimum mileage value to search for
     * @return the Specification for mileage filtering
     */
    public static Specification<Car> mileageGreaterThanOrEqual(Integer mileage) {
        return (root, query, builder) ->
                builder.greaterThanOrEqualTo(root.get("mileage"), mileage);
    }

    /**
     * Creates a specification to filter cars by mileage less than or equal to the specified value.
     *
     * @param mileage the maximum mileage value to search for
     * @return the Specification for mileage filtering
     */
    public static Specification<Car> mileageLessThanOrEqual(Integer mileage) {
        return (root, query, builder) ->
                builder.lessThanOrEqualTo(root.get("mileage"), mileage);
    }

    /**
     * Creates a specification to filter cars by price greater than or equal to the specified value.
     *
     * @param price the minimum price value to search for
     * @return the Specification for price filtering
     */
    public static Specification<Car> priceGreaterThanOrEqual(Double price) {
        return (root, query, builder) ->
                builder.greaterThanOrEqualTo(root.get("price"), price);
    }

    /**
     * Creates a specification to filter cars by price less than or equal to the specified value.
     *
     * @param price the maximum price value to search for
     * @return the Specification for price filtering
     */
    public static Specification<Car> priceLessThanOrEqual(Double price) {
        return (root, query, builder) ->
                builder.lessThanOrEqualTo(root.get("price"), price);
    }

}
