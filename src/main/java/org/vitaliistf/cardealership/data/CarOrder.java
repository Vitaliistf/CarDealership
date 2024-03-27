package org.vitaliistf.cardealership.data;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.vitaliistf.cardealership.data.enums.OrderStatus;

import java.time.LocalDate;

/**
 * Represents an order for a car in the car dealership system.
 */
@Entity
@Table(name = "car_order")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CarOrder {

    /** The unique identifier of the order. */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    /** The seller of the car associated with the order. */
    @NotNull
    @ManyToOne
    @JoinColumn(name = "seller_id")
    private User seller;

    /** The buyer of the car associated with the order. */
    @NotNull
    @ManyToOne
    @JoinColumn(name = "buyer_id")
    private User buyer;

    /** The car associated with the order. */
    @NotNull
    @OneToOne
    @JoinColumn(name = "car_id")
    private Car car;

    /** The date when the order was placed. */
    @NotNull
    @Column(name = "order_date")
    private LocalDate orderDate;

    /** The status of the order. */
    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "order_status")
    private OrderStatus orderStatus;
}
