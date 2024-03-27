package org.vitaliistf.cardealership.data;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.vitaliistf.cardealership.data.enums.*;

/**
 * Represents a car entity in the car dealership system.
 */
@Entity
@Table(name = "car")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Car {

    /** The unique identifier of the car. */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    /** The brand of the car. */
    @NotBlank(message = "Brand is required.")
    @Size(max = 50, message = "Brand length must be less than 50 characters.")
    @Column(name = "brand")
    private String brand;

    /** The model of the car. */
    @NotBlank(message = "Model is required.")
    @Size(max = 50, message = "Model length must be less than 50 characters.")
    @Column(name = "model")
    private String model;

    /** The manufacturing year of the car. */
    @NotNull(message = "Year is required.")
    @Min(value = 1900, message = "Year must be greater than or equal to 1900.")
    @Max(value = 2100, message = "Year must be less than or equal to 2100.")
    @Column(name = "year")
    private Integer year;

    /** The color of the car. */
    @NotBlank(message = "Color is required.")
    @Size(max = 50, message = "Color length must be less than 50 characters.")
    @Column(name = "color")
    private String color;

    /** The transmission type of the car. */
    @NotNull(message = "Transmission is required.")
    @Enumerated(value = EnumType.STRING)
    @Column(name = "transmission")
    private Transmission transmission;

    /** The fuel type of the car. */
    @NotNull(message = "Fuel type is required.")
    @Enumerated(value = EnumType.STRING)
    @Column(name = "fuel")
    private FuelType fuelType;

    /** The body type of the car. */
    @NotNull(message = "Body type is required.")
    @Enumerated(value = EnumType.STRING)
    @Column(name = "body_type")
    private BodyType bodyType;

    /** The status of the car (in the system). */
    @Enumerated(value = EnumType.STRING)
    @Column(name = "status")
    private CarStatus status;

    /** The engine displacement of the car. */
    @NotNull(message = "Engine displacement is required.")
    @DecimalMin(value = "0.5", message = "Engine displacement must be greater than or equal to 0.5")
    @Column(name = "engine_displacement")
    private Double engineDisplacement;

    /** The mileage of the car. */
    @NotNull(message = "Mileage is required.")
    @Min(value = 0, message = "Mileage must be greater than or equal to 0.")
    @Column(name="mileage")
    private Integer mileage;

    /** The description of the car. */
    @Size(min = 50, max = 1000, message = "Description length must be less than 1000, but more then 50 characters.")
    @Column(name = "description")
    private String description;

    /** The price of the car. */
    @NotNull(message = "Price is required.")
    @Min(value = 1, message = "Price must be greater than or equal to 1.")
    @Column(name = "price")
    private Double price;

    /** The technical condition of the car. */
    @NotNull(message = "Technical condition is required.")
    @Enumerated(value = EnumType.STRING)
    @Column(name = "technical_condition")
    private TechnicalCondition condition;

    /** The owner of the car. */
    @ManyToOne
    @JoinColumn(name = "owner_id")
    private User owner;

    /** The URL of the car's photo. */
    @Size(max = 255, message = "Photo URL length must be less than 255 characters.")
    @Column(name = "photo_url")
    private String photoUrl;

}
