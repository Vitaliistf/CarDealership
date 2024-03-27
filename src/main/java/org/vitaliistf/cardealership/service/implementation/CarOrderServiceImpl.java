package org.vitaliistf.cardealership.service.implementation;

import lombok.AllArgsConstructor;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.stereotype.Service;
import org.vitaliistf.cardealership.data.Car;
import org.vitaliistf.cardealership.data.CarOrder;
import org.vitaliistf.cardealership.data.User;
import org.vitaliistf.cardealership.data.enums.CarStatus;
import org.vitaliistf.cardealership.data.enums.OrderStatus;
import org.vitaliistf.cardealership.exception.BadRequestException;
import org.vitaliistf.cardealership.exception.ForbiddenException;
import org.vitaliistf.cardealership.exception.NotFoundException;
import org.vitaliistf.cardealership.repository.CarOrderRepository;
import org.vitaliistf.cardealership.service.CarOrderService;
import org.vitaliistf.cardealership.service.CarService;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.Set;

/**
 * Implementation of {@link CarOrderService} providing functionalities related to car orders.
 */
@Service
@AllArgsConstructor
public class CarOrderServiceImpl implements CarOrderService {

    private final CarOrderRepository carOrderRepository;
    private final CarService carService;
    private final PDFGenerationService pdfGenerator;
    private final TextFileGenerationService textFileGenerator;

    /**
     * Creates a new car order in the system.
     *
     * @param carId ID of the car to be ordered
     * @param user  User placing the order
     */
    @Override
    public void createCarOrder(long carId, User user) {
        Car car = carService.getCarById(carId);
        validateCarOrderCreation(car, user);
        car.setStatus(CarStatus.ORDERED);
        CarOrder order = createNewCarOrder(car, user);
        carOrderRepository.save(order);
    }

    /**
     * Retrieves a car order by its ID.
     *
     * @param id   ID of the car order
     * @param user User requesting the order
     * @return CarOrder object
     */
    @Override
    public CarOrder getCarOrderById(Long id, User user) {
        CarOrder order = getCarOrderById(id);
        validateCarOrderAccess(order, user);
        return order;
    }

    /**
     * Completes a car order by its ID.
     *
     * @param id   ID of the car order
     * @param user User completing the order
     */
    @Override
    public void completeCarOrderById(Long id, User user) {
        CarOrder order = getCarOrderById(id);
        validateCarOrderCompletion(order, user);
        order.setOrderStatus(OrderStatus.COMPLETED);
        Car car = order.getCar();
        car.setStatus(CarStatus.ARCHIVED);
        carOrderRepository.save(order);
    }

    /**
     * Confirms a car order by its ID.
     *
     * @param id   ID of the car order
     * @param user User confirming the order
     */
    @Override
    public void confirmCarOrderById(Long id, User user) {
        CarOrder order = getCarOrderById(id);
        validateCarOrderConfirmation(order, user);
        order.setOrderStatus(OrderStatus.CONFIRMED);
        carOrderRepository.save(order);
    }

    /**
     * Cancels a car order by its ID.
     *
     * @param id   ID of the car order
     * @param user User canceling the order
     */
    @Override
    public void cancelCarOrderById(Long id, User user) {
        CarOrder order = getCarOrderById(id);
        validateCarOrderAccess(order, user);
        order.setOrderStatus(OrderStatus.CANCELED);
        Car car = order.getCar();
        car.setStatus(CarStatus.AVAILABLE);
        carOrderRepository.save(order);
    }

    /**
     * Generates a PDF document for a car order by its ID.
     *
     * @param id   ID of the car order
     * @param user User requesting the document
     * @return ByteArrayResource containing the PDF document
     */
    @Override
    public ByteArrayResource generateCarOrderPdfById(Long id, User user) {
        CarOrder order = getCarOrderById(id);
        validateCarOrderAccess(order, user);
        return pdfGenerator.generateDocumentByOrder(order);
    }

    /**
     * Generates a text file document for a car order by its ID.
     *
     * @param id   ID of the car order
     * @param user User requesting the document
     * @return ByteArrayResource containing the text file document
     */
    @Override
    public ByteArrayResource generateCarOrderTxtById(Long id, User user) {
        CarOrder order = getCarOrderById(id);
        validateCarOrderAccess(order, user);
        return textFileGenerator.generateDocumentByOrder(order);
    }

    /**
     * Retrieves non-active car orders by the buyer.
     *
     * @param buyer User who placed the orders
     * @return List of non-active car orders
     */
    @Override
    public List<CarOrder> getNonActiveCarOrdersByBuyer(User buyer) {
        Set<OrderStatus> nonActiveStatuses = Set.of(OrderStatus.COMPLETED, OrderStatus.CANCELED);
        return carOrderRepository.findByBuyerAndOrderStatusIn(buyer, nonActiveStatuses);
    }

    /**
     * Retrieves non-active car orders by the seller.
     *
     * @param seller User who sold the cars
     * @return List of non-active car orders
     */
    @Override
    public List<CarOrder> getNonActiveCarOrdersBySeller(User seller) {
        Set<OrderStatus> nonActiveStatuses = Set.of(OrderStatus.COMPLETED, OrderStatus.CANCELED);
        return carOrderRepository.findBySellerAndOrderStatusIn(seller, nonActiveStatuses);
    }

    /**
     * Retrieves active car orders by the buyer.
     *
     * @param buyer User who placed the orders
     * @return List of active car orders
     */
    @Override
    public List<CarOrder> getActiveCarOrdersByBuyer(User buyer) {
        Set<OrderStatus> activeStatuses = Set.of(OrderStatus.NEW, OrderStatus.CONFIRMED);
        return carOrderRepository.findByBuyerAndOrderStatusIn(buyer, activeStatuses);
    }

    /**
     * Retrieves active car orders by the seller.
     *
     * @param seller User who sold the cars
     * @return List of active car orders
     */
    @Override
    public List<CarOrder> getActiveCarOrdersBySeller(User seller) {
        Set<OrderStatus> activeStatuses = Set.of(OrderStatus.NEW, OrderStatus.CONFIRMED);
        return carOrderRepository.findBySellerAndOrderStatusIn(seller, activeStatuses);
    }

    /**
     * Retrieves a car order by its ID, throwing an exception if not found.
     *
     * @param id ID of the car order
     * @return CarOrder object
     * @throws NotFoundException if the order is not found
     */
    private CarOrder getCarOrderById(Long id) {
        return carOrderRepository.findById(id).orElseThrow(() -> new NotFoundException("Order not found."));
    }

    /**
     * Checks if a user has access to a particular car order.
     *
     * @param order CarOrder to check access for
     * @param user  User to check access against
     * @throws ForbiddenException if user has no permission
     */
    private void validateCarOrderAccess(CarOrder order, User user) {
        if (!((Objects.equals(order.getBuyer().getId(), user.getId()) ||
                Objects.equals(order.getSeller().getId(), user.getId())) &&
                order.getOrderStatus() != OrderStatus.CANCELED)) {
            throw new ForbiddenException("You have no permission for this operation.");
        }
    }

    /**
     * Checks if a user can complete a car order.
     *
     * @param order CarOrder to check
     * @param user  User attempting to complete the order
     * @throws ForbiddenException if user has no permission
     */
    private void validateCarOrderCompletion(CarOrder order, User user) {
        if (!(order.getOrderStatus() == OrderStatus.CONFIRMED &&
                Objects.equals(order.getBuyer().getId(), user.getId()))) {
            throw new ForbiddenException("You have no permission for this operation.");
        }
    }

    /**
     * Checks if a user can confirm a car order.
     *
     * @param order CarOrder to check
     * @param user  User attempting to confirm the order
     * @throws ForbiddenException if user has no permission
     */
    private void validateCarOrderConfirmation(CarOrder order, User user) {
        if (!(order.getOrderStatus() == OrderStatus.NEW &&
                Objects.equals(order.getSeller().getId(), user.getId()))) {
            throw new ForbiddenException("You have no permission for this operation.");
        }
    }

    /**
     * Checks if a user can order a car .
     *
     * @param car  Car to be ordered
     * @param user User that creates order
     * @throws BadRequestException if the car is not available
     */
    private void validateCarOrderCreation(Car car, User user) {
        if (car.getStatus() != CarStatus.AVAILABLE || user.getId().equals(car.getOwner().getId())) {
            throw new ForbiddenException("You cannot order this car.");
        }
    }

    /**
     * Creates a new car order.
     *
     * @param car  Car to be ordered
     * @param user User placing the order
     */
    private CarOrder createNewCarOrder(Car car, User user) {
        CarOrder order = new CarOrder();
        order.setCar(car);
        order.setBuyer(user);
        order.setSeller(car.getOwner());
        order.setOrderDate(LocalDate.now());
        order.setOrderStatus(OrderStatus.NEW);
        return order;
    }
}