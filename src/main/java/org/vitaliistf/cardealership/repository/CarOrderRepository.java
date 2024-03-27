package org.vitaliistf.cardealership.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.vitaliistf.cardealership.data.CarOrder;
import org.vitaliistf.cardealership.data.User;
import org.vitaliistf.cardealership.data.enums.OrderStatus;

import java.util.Collection;
import java.util.List;

/**
 * Repository interface for accessing CarOrder entities in the database.
 */
@Repository
public interface CarOrderRepository extends JpaRepository<CarOrder, Long> {

    /**
     * Retrieves a list of car orders made by the specified buyer with the given order statuses.
     *
     * @param buyer  the buyer of the orders
     * @param status the collection of order statuses
     * @return a list of car orders
     */
    List<CarOrder> findByBuyerAndOrderStatusIn(User buyer, Collection<OrderStatus> status);

    /**
     * Retrieves a list of car orders sold by the specified seller with the given order statuses.
     *
     * @param seller the seller of the orders
     * @param status the collection of order statuses
     * @return a list of car orders
     */
    List<CarOrder> findBySellerAndOrderStatusIn(User seller, Collection<OrderStatus> status);

}
