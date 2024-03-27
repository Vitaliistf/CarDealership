package org.vitaliistf.cardealership.service;

import org.springframework.core.io.ByteArrayResource;
import org.vitaliistf.cardealership.data.CarOrder;
import org.vitaliistf.cardealership.data.User;

import java.util.List;

/**
 * Service interface for managing car orders.
 */
public interface CarOrderService {

    /**
     * Creates a new car order.
     *
     * @param carId the ID of the car to order
     * @param user  the user placing the order
     */
    void createCarOrder(long carId, User user);

    /**
     * Retrieves a car order by its ID.
     *
     * @param orderId the ID of the car order to retrieve
     * @param user    the user accessing the order
     * @return the car order with the specified ID
     */
    CarOrder getCarOrderById(Long orderId, User user);

    /**
     * Completes a car order by its ID.
     *
     * @param orderId the ID of the car order to complete
     * @param user    the user completing the order
     */
    void completeCarOrderById(Long orderId, User user);

    /**
     * Confirms a car order by its ID.
     *
     * @param orderId the ID of the car order to confirm
     * @param user    the user confirming the order
     */
    void confirmCarOrderById(Long orderId, User user);

    /**
     * Cancels a car order by its ID.
     *
     * @param orderId the ID of the car order to cancel
     * @param user    the user cancelling the order
     */
    void cancelCarOrderById(Long orderId, User user);

    /**
     * Generates a PDF for the car order with the specified ID.
     *
     * @param orderId the ID of the car order to generate the PDF for
     * @param user    the user accessing the order
     * @return a ByteArrayResource containing the generated PDF
     */
    ByteArrayResource generateCarOrderPdfById(Long orderId, User user);

    /**
     * Generates a text file for the car order with the specified ID.
     *
     * @param orderId the ID of the car order to generate the text file for
     * @param user    the user accessing the order
     * @return a ByteArrayResource containing the generated text file
     */
    ByteArrayResource generateCarOrderTxtById(Long orderId, User user);

    /**
     * Retrieves non-active car orders for a buyer.
     *
     * @param buyer the buyer for whom to retrieve the orders
     * @return a list of non-active car orders for the specified buyer
     */
    List<CarOrder> getNonActiveCarOrdersByBuyer(User buyer);

    /**
     * Retrieves non-active car orders for a seller.
     *
     * @param seller the seller for whom to retrieve the orders
     * @return a list of non-active car orders for the specified seller
     */
    List<CarOrder> getNonActiveCarOrdersBySeller(User seller);

    /**
     * Retrieves active car orders for a buyer.
     *
     * @param buyer the buyer for whom to retrieve the orders
     * @return a list of active car orders for the specified buyer
     */
    List<CarOrder> getActiveCarOrdersByBuyer(User buyer);

    /**
     * Retrieves active car orders for a seller.
     *
     * @param seller the seller for whom to retrieve the orders
     * @return a list of active car orders for the specified seller
     */
    List<CarOrder> getActiveCarOrdersBySeller(User seller);

}
