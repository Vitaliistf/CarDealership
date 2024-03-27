package org.vitaliistf.cardealership.service.implementation;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.io.ByteArrayResource;
import org.vitaliistf.cardealership.data.Car;
import org.vitaliistf.cardealership.data.CarOrder;
import org.vitaliistf.cardealership.data.User;
import org.vitaliistf.cardealership.data.enums.*;
import org.vitaliistf.cardealership.exception.ForbiddenException;
import org.vitaliistf.cardealership.exception.NotFoundException;
import org.vitaliistf.cardealership.repository.CarOrderRepository;
import org.vitaliistf.cardealership.service.CarService;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CarOrderServiceImplTest {

    @Mock
    private CarOrderRepository carOrderRepository;

    @Mock
    private CarService carService;

    @Mock
    private PDFGenerationService pdfGenerator;

    @Mock
    private TextFileGenerationService textFileGenerator;

    @InjectMocks
    private CarOrderServiceImpl carOrderService;

    private User buyer;
    private User seller;
    private User unauthorizedUser;
    private Car availableCar;
    private Car orderedCar;
    private CarOrder newOrder;
    private CarOrder confirmedOrder;
    private CarOrder completedOrder;

    @BeforeEach
    void setUp() {
        setUpBuyer();
        setUpSeller();
        setUpUnauthorizedUser();
        setUpAvailableCar();
        setUpOrderedCar();
        setUpNewOrder();
        setUpConfirmedOrder();
        setUpCompletedOrder();
    }

    @Test
    void testCreateCarOrderWithAvailableCarAndDifferentBuyerAndSeller() {
        when(carService.getCarById(availableCar.getId())).thenReturn(availableCar);

        carOrderService.createCarOrder(availableCar.getId(), buyer);

        verify(carOrderRepository, times(1)).save(any(CarOrder.class));
    }

    @Test
    void testCreateCarOrderWithUnavailableCarOrSameBuyerAndSeller() {
        when(carService.getCarById(availableCar.getId())).thenReturn(availableCar);
        availableCar.setStatus(CarStatus.ORDERED);

        assertThrows(ForbiddenException.class, () -> carOrderService.createCarOrder(availableCar.getId(), buyer));

        availableCar.setStatus(CarStatus.AVAILABLE);
        availableCar.setOwner(buyer);

        assertThrows(ForbiddenException.class, () -> carOrderService.createCarOrder(availableCar.getId(), buyer));
    }

    @Test
    void testGetCarOrderByIdWithExistingOrderAndAuthorizedUser() {
        when(carOrderRepository.findById(newOrder.getId())).thenReturn(Optional.of(newOrder));

        CarOrder result = carOrderService.getCarOrderById(newOrder.getId(), buyer);

        assertEquals(newOrder, result);
    }

    @Test
    void testGetCarOrderByIdWithNonExistingOrder() {
        when(carOrderRepository.findById(any())).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> carOrderService.getCarOrderById(123L, buyer));
    }

    @Test
    void testGetCarOrderByIdWithUnauthorizedUser() {
        when(carOrderRepository.findById(newOrder.getId())).thenReturn(Optional.of(newOrder));

        assertThrows(ForbiddenException.class, () -> carOrderService.getCarOrderById(newOrder.getId(), unauthorizedUser));
    }

    @Test
    void testCompleteCarOrderByIdWithConfirmedOrderAndBuyer() {
        when(carOrderRepository.findById(confirmedOrder.getId())).thenReturn(Optional.of(confirmedOrder));

        carOrderService.completeCarOrderById(confirmedOrder.getId(), buyer);

        verify(carOrderRepository, times(1)).save(argThat(order -> order.getOrderStatus().equals(OrderStatus.COMPLETED)));
    }

    @Test
    void testCompleteCarOrderByIdWithUnauthorizedUser() {
        when(carOrderRepository.findById(confirmedOrder.getId())).thenReturn(Optional.of(confirmedOrder));

        assertThrows(ForbiddenException.class, () -> carOrderService.completeCarOrderById(confirmedOrder.getId(), unauthorizedUser));
    }

    @Test
    void testConfirmCarOrderByIdWithNewOrderAndSeller() {
        when(carOrderRepository.findById(newOrder.getId())).thenReturn(Optional.of(newOrder));

        carOrderService.confirmCarOrderById(newOrder.getId(), seller);

        verify(carOrderRepository, times(1)).save(argThat(order -> order.getOrderStatus().equals(OrderStatus.CONFIRMED)));
    }

    @Test
    void testConfirmCarOrderByIdWithUnauthorizedUser() {
        when(carOrderRepository.findById(newOrder.getId())).thenReturn(Optional.of(newOrder));

        assertThrows(ForbiddenException.class, () -> carOrderService.confirmCarOrderById(newOrder.getId(), unauthorizedUser));
    }

    @Test
    void testCancelCarOrderByIdWithExistingOrderAndAuthorizedUser() {
        when(carOrderRepository.findById(newOrder.getId())).thenReturn(Optional.of(newOrder));

        carOrderService.cancelCarOrderById(newOrder.getId(), buyer);

        verify(carOrderRepository, times(1)).save(argThat(order -> order.getOrderStatus().equals(OrderStatus.CANCELED)));
    }

    @Test
    void testCancelCarOrderByIdWithUnauthorizedUser() {
        when(carOrderRepository.findById(newOrder.getId())).thenReturn(Optional.of(newOrder));

        assertThrows(ForbiddenException.class, () -> carOrderService.cancelCarOrderById(newOrder.getId(), unauthorizedUser));
    }

    @Test
    void testGenerateCarOrderPdfByIdWithExistingOrderAndAuthorizedUser() {
        ByteArrayResource expectedResource = new ByteArrayResource("PDF content".getBytes());
        when(carOrderRepository.findById(newOrder.getId())).thenReturn(Optional.of(newOrder));
        when(pdfGenerator.generateDocumentByOrder(newOrder)).thenReturn(expectedResource);

        ByteArrayResource result = carOrderService.generateCarOrderPdfById(newOrder.getId(), buyer);

        assertEquals(expectedResource, result);
    }

    @Test
    void testGenerateCarOrderPdfByIdWithUnauthorizedUser() {
        when(carOrderRepository.findById(newOrder.getId())).thenReturn(Optional.of(newOrder));

        assertThrows(ForbiddenException.class, () -> carOrderService.generateCarOrderPdfById(newOrder.getId(), unauthorizedUser));
    }

    @Test
    void testGenerateCarOrderTxtByIdWithExistingOrderAndAuthorizedUser() {
        ByteArrayResource expectedResource = new ByteArrayResource("Text content".getBytes());
        when(carOrderRepository.findById(newOrder.getId())).thenReturn(Optional.of(newOrder));
        when(textFileGenerator.generateDocumentByOrder(newOrder)).thenReturn(expectedResource);

        ByteArrayResource result = carOrderService.generateCarOrderTxtById(newOrder.getId(), buyer);

        assertEquals(expectedResource, result);
    }

    @Test
    void testGenerateCarOrderTxtByIdWithUnauthorizedUser() {
        when(carOrderRepository.findById(newOrder.getId())).thenReturn(Optional.of(newOrder));

        assertThrows(ForbiddenException.class, () -> carOrderService.generateCarOrderTxtById(newOrder.getId(), unauthorizedUser));
    }

    @Test
    void testGetNonActiveCarOrdersByBuyerWithExistingNonActiveOrders() {
        List<CarOrder> nonActiveOrders = List.of(completedOrder);
        when(carOrderRepository.findByBuyerAndOrderStatusIn(buyer, Set.of(OrderStatus.COMPLETED, OrderStatus.CANCELED)))
                .thenReturn(nonActiveOrders);

        List<CarOrder> result = carOrderService.getNonActiveCarOrdersByBuyer(buyer);

        assertEquals(nonActiveOrders, result);
    }

    @Test
    void testGetNonActiveCarOrdersBySellerWithExistingNonActiveOrders() {
        List<CarOrder> nonActiveOrders = List.of(completedOrder);
        when(carOrderRepository.findBySellerAndOrderStatusIn(seller, Set.of(OrderStatus.COMPLETED, OrderStatus.CANCELED)))
                .thenReturn(nonActiveOrders);

        List<CarOrder> result = carOrderService.getNonActiveCarOrdersBySeller(seller);

        assertEquals(nonActiveOrders, result);
    }

    @Test
    void testGetActiveCarOrdersByBuyerWithExistingActiveOrders() {
        List<CarOrder> activeOrders = List.of(newOrder, confirmedOrder);
        when(carOrderRepository.findByBuyerAndOrderStatusIn(buyer, Set.of(OrderStatus.NEW, OrderStatus.CONFIRMED)))
                .thenReturn(activeOrders);

        List<CarOrder> result = carOrderService.getActiveCarOrdersByBuyer(buyer);

        assertEquals(activeOrders, result);
    }

    @Test
    void testGetActiveCarOrdersBySellerWithExistingActiveOrders() {
        List<CarOrder> activeOrders = List.of(newOrder, confirmedOrder);
        when(carOrderRepository.findBySellerAndOrderStatusIn(seller, Set.of(OrderStatus.NEW, OrderStatus.CONFIRMED)))
                .thenReturn(activeOrders);

        List<CarOrder> result = carOrderService.getActiveCarOrdersBySeller(seller);

        assertEquals(activeOrders, result);
    }

    private void setUpBuyer() {
        buyer = new User();
        buyer.setId(1L);
        buyer.setFirstName("John");
        buyer.setLastName("Doe");
        buyer.setEmail("john.doe@example.com");
        buyer.setPassword("password123");
        buyer.setPhoneNumber("+12345678901");
        buyer.setAddress("123 Main St, AnyTown USA");
    }

    private void setUpSeller() {
        seller = new User();
        seller.setId(2L);
        seller.setFirstName("Jane");
        seller.setLastName("Smith");
        seller.setEmail("jane.smith@example.com");
        seller.setPassword("password456");
        seller.setPhoneNumber("+98765432109");
        seller.setAddress("456 Oak Ave, SomeCity USA");
    }

    private void setUpAvailableCar() {
        availableCar = new Car();
        availableCar.setId(1L);
        availableCar.setBrand("Toyota");
        availableCar.setModel("Camry");
        availableCar.setYear(2020);
        availableCar.setColor("Red");
        availableCar.setTransmission(Transmission.AUTOMATIC);
        availableCar.setFuelType(FuelType.PETROL);
        availableCar.setBodyType(BodyType.SEDAN);
        availableCar.setStatus(CarStatus.AVAILABLE);
        availableCar.setEngineDisplacement(2.5);
        availableCar.setMileage(10000);
        availableCar.setDescription("Excellent condition, low mileage.");
        availableCar.setPrice(25000.0);
        availableCar.setCondition(TechnicalCondition.EXCELLENT);
        availableCar.setOwner(seller);
        availableCar.setPhotoUrl("https://example.com/car1.jpg");
    }

    private void setUpOrderedCar() {
        orderedCar = new Car();
        orderedCar.setId(2L);
        orderedCar.setBrand("Honda");
        orderedCar.setModel("Civic");
        orderedCar.setYear(2018);
        orderedCar.setColor("Blue");
        orderedCar.setTransmission(Transmission.MANUAL);
        orderedCar.setFuelType(FuelType.PETROL);
        orderedCar.setBodyType(BodyType.COUPE);
        orderedCar.setStatus(CarStatus.ORDERED);
        orderedCar.setEngineDisplacement(1.8);
        orderedCar.setMileage(30000);
        orderedCar.setDescription("Good condition, well-maintained.");
        orderedCar.setPrice(18000.0);
        orderedCar.setCondition(TechnicalCondition.GOOD);
        orderedCar.setOwner(seller);
        orderedCar.setPhotoUrl("https://example.com/car2.jpg");
    }

    private void setUpNewOrder() {
        newOrder = new CarOrder();
        newOrder.setId(1L);
        newOrder.setBuyer(buyer);
        newOrder.setSeller(seller);
        newOrder.setCar(orderedCar);
        newOrder.setOrderDate(LocalDate.now());
        newOrder.setOrderStatus(OrderStatus.NEW);
    }

    private void setUpConfirmedOrder() {
        confirmedOrder = new CarOrder();
        confirmedOrder.setId(2L);
        confirmedOrder.setBuyer(buyer);
        confirmedOrder.setSeller(seller);
        confirmedOrder.setCar(orderedCar);
        confirmedOrder.setOrderDate(LocalDate.now().minusDays(7));
        confirmedOrder.setOrderStatus(OrderStatus.CONFIRMED);
    }

    private void setUpCompletedOrder() {
        completedOrder = new CarOrder();
        completedOrder.setId(3L);
        completedOrder.setBuyer(buyer);
        completedOrder.setSeller(seller);
        completedOrder.setCar(orderedCar);
        completedOrder.setOrderDate(LocalDate.now().minusMonths(1));
        completedOrder.setOrderStatus(OrderStatus.COMPLETED);
    }

    private void setUpUnauthorizedUser() {
        unauthorizedUser = new User();
        unauthorizedUser.setId(3L);
        unauthorizedUser.setFirstName("Alice");
        unauthorizedUser.setLastName("Johnson");
        unauthorizedUser.setEmail("alice.johnson@example.com");
        unauthorizedUser.setPassword("password789");
        unauthorizedUser.setPhoneNumber("+11122233344");
        unauthorizedUser.setAddress("789 Maple Rd, AnotherTown USA");
    }
}
