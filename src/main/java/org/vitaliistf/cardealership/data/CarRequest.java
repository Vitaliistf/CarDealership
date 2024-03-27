package org.vitaliistf.cardealership.data;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.Range;
import org.vitaliistf.cardealership.data.enums.BodyType;
import org.vitaliistf.cardealership.data.enums.FuelType;
import org.vitaliistf.cardealership.data.enums.TechnicalCondition;
import org.vitaliistf.cardealership.data.enums.Transmission;

/**
 * Represents a car request entity in the car dealership system.
 */
@Entity
@Table(name = "car_request")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CarRequest {

    /** The unique identifier of the car request. */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    /** The brand specified in the car request. */
    @Column(name = "brand")
    private String brand;

    /** The weight assigned to the brand in the car request. */
    @Range(min = 0, max = 10, message = "Weights should be in [0-10] range.")
    @Column(name = "brand_weight")
    private Integer brandWeight;

    /** The model specified in the car request. */
    @Column(name = "model")
    private String model;

    /** The weight assigned to the model in the car request. */
    @Range(min = 0, max = 10, message = "Weights should be in [0-10] range.")
    @Column(name = "model_weight")
    private Integer modelWeight;

    /** The manufacturing year specified in the car request. */
    @Column(name = "year")
    private Integer year;

    /** The weight assigned to the manufacturing year in the car request. */
    @Range(min = 0, max = 10, message = "Weights should be in [0-10] range.")
    @Column(name = "year_weight")
    private Integer yearWeight;

    /** The color specified in the car request. */
    @Column(name = "color")
    private String color;

    /** The weight assigned to the color in the car request. */
    @Range(min = 0, max = 10, message = "Weights should be in [0-10] range.")
    @Column(name = "color_weight")
    private Integer colorWeight;

    /** The transmission type specified in the car request. */
    @Enumerated(value = EnumType.STRING)
    @Column(name = "transmission")
    private Transmission transmission;

    /** The weight assigned to the transmission type in the car request. */
    @Range(min = 0, max = 10, message = "Weights should be in [0-10] range.")
    @Column(name = "transmission_weight")
    private Integer transmissionWeight;

    /** The fuel type specified in the car request. */
    @Enumerated(value = EnumType.STRING)
    @Column(name = "fuel")
    private FuelType fuelType;

    /** The weight assigned to the fuel type in the car request. */
    @Range(min = 0, max = 10, message = "Weights should be in [0-10] range.")
    @Column(name = "fuel_weight")
    private Integer fuelWeight;

    /** The body type specified in the car request. */
    @Enumerated(value = EnumType.STRING)
    @Column(name = "body_type")
    private BodyType bodyType;

    /** The weight assigned to the body type in the car request. */
    @Range(min = 0, max = 10, message = "Weights should be in [0-10] range.")
    @Column(name = "body_type_weight")
    private Integer bodyTypeWeight;

    /** The engine displacement specified in the car request. */
    @Column(name = "engine_displacement")
    private Double engineDisplacement;

    /** The weight assigned to the engine displacement in the car request. */
    @Range(min = 0, max = 10, message = "Weights should be in [0-10] range.")
    @Column(name = "engine_displacement_weight")
    private Integer engineDisplacementWeight;

    /** The maximum mileage specified in the car request. */
    @Column(name="max_mileage")
    private Integer maxMileage;

    /** The weight assigned to the maximum mileage in the car request. */
    @Range(min = 0, max = 10, message = "Weights should be in [0-10] range.")
    @Column(name="max_mileage_weight")
    private Integer maxMileageWeight;

    /** The maximum price specified in the car request. */
    @Column(name = "max_price")
    private Double maxPrice;

    /** The weight assigned to the maximum price in the car request. */
    @Range(min = 0, max = 10, message = "Weights should be in [0-10] range.")
    @Column(name = "max_price_weight")
    private Integer maxPriceWeight;

    /** The technical condition specified in the car request. */
    @Enumerated(value = EnumType.STRING)
    @Column(name = "technical_condition")
    private TechnicalCondition condition;

    /** The weight assigned to the technical condition in the car request. */
    @Range(min = 0, max = 10, message = "Weights should be in [0-10] range.")
    @Column(name = "technical_condition_weight")
    private Integer conditionWeight;

    /** The user who submitted the car request. */
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
}
