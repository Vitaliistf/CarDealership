package org.vitaliistf.cardealership.service.implementation;
//
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.junit.jupiter.MockitoExtension;
//import org.vitaliistf.cardealership.data.Car;
//import org.vitaliistf.cardealership.data.CarRequest;
//import org.vitaliistf.cardealership.data.User;
//import org.vitaliistf.cardealership.data.enums.*;
//import org.vitaliistf.cardealership.service.CarService;
//
//import java.util.List;
//
//import static org.junit.jupiter.api.Assertions.*;
//import static org.mockito.Mockito.when;
//
//@ExtendWith(MockitoExtension.class)
//public class CarSuggestionServiceImplTest {
//
//    @Mock
//    private CarService carService;
//
//    @InjectMocks
//    private CarSuggestionServiceImpl carSuggestionService;
//
//    @Test
//    void testFindCarsByRequestWhenPerfectMatch() {
//        User buyer = new User();
//        buyer.setId(1L);
//
//        CarRequest request = new CarRequest();
//        request.setBrand("Toyota");
//        request.setModel("Camry");
//        request.setYear(2020);
//        request.setColor("Red");
//        request.setTransmission(Transmission.AUTOMATIC);
//        request.setFuelType(FuelType.PETROL);
//        request.setBodyType(BodyType.SEDAN);
//        request.setEngineDisplacement(2.5);
//        request.setMaxMileage(30000);
//        request.setMaxPrice(25000.0);
//        request.setCondition(TechnicalCondition.GOOD);
//        request.setUser(buyer);
//
//        User seller = new User();
//        seller.setId(2L);
//
//        Car car1 = new Car();
//        car1.setBrand("Toyota");
//        car1.setModel("Camry");
//        car1.setYear(2020);
//        car1.setColor("Red");
//        car1.setTransmission(Transmission.AUTOMATIC);
//        car1.setFuelType(FuelType.PETROL);
//        car1.setBodyType(BodyType.SEDAN);
//        car1.setEngineDisplacement(2.5);
//        car1.setMileage(25000);
//        car1.setPrice(22000.0);
//        car1.setCondition(TechnicalCondition.GOOD);
//        car1.setOwner(seller);
//
//        Car car2 = new Car();
//        car2.setBrand("Toyota");
//        car2.setModel("Camry");
//        car2.setYear(2000);
//        car2.setColor("Red");
//        car2.setTransmission(Transmission.MANUAL);
//        car2.setFuelType(FuelType.PETROL);
//        car2.setBodyType(BodyType.COUPE);
//        car2.setEngineDisplacement(2.8);
//        car2.setMileage(25000);
//        car2.setPrice(26000.0);
//        car2.setCondition(TechnicalCondition.GOOD);
//        car2.setOwner(seller);
//
//        when(carService.getCarsByStatus(CarStatus.AVAILABLE)).thenReturn(List.of(car1, car2));
//
//        List<Car> result = carSuggestionService.findCarsByRequest(request);
//
//        assertEquals(1, result.size());
//        assertEquals(car1, result.get(0));
//    }
//
//    @Test
//    void testFindCarsByRequestWhenNoPerfectMatch() {
//        User buyer = new User();
//        buyer.setId(1L);
//        CarRequest request = new CarRequest();
//        request.setBrand("Toyota");
//        request.setModel("Camry");
//        request.setYear(2020);
//        request.setColor("Red");
//        request.setTransmission(Transmission.AUTOMATIC);
//        request.setFuelType(FuelType.PETROL);
//        request.setBodyType(BodyType.SEDAN);
//        request.setEngineDisplacement(2.5);
//        request.setMaxMileage(30000);
//        request.setMaxPrice(25000.0);
//        request.setCondition(TechnicalCondition.GOOD);
//        request.setUser(buyer);
//
//        User seller = new User();
//        seller.setId(2L);
//
//        Car car1 = new Car();
//        car1.setBrand("Honda");
//        car1.setModel("Civic");
//        car1.setYear(2018);
//        car1.setColor("Blue");
//        car1.setTransmission(Transmission.MANUAL);
//        car1.setFuelType(FuelType.PETROL);
//        car1.setBodyType(BodyType.COUPE);
//        car1.setEngineDisplacement(1.8);
//        car1.setMileage(40000);
//        car1.setPrice(18000.0);
//        car1.setCondition(TechnicalCondition.POOR);
//        car1.setOwner(seller);
//
//        Car car2 = new Car();
//        car2.setBrand("Ford");
//        car2.setModel("Mustang");
//        car2.setYear(2021);
//        car2.setColor("Black");
//        car2.setTransmission(Transmission.AUTOMATIC);
//        car2.setFuelType(FuelType.PETROL);
//        car2.setBodyType(BodyType.COUPE);
//        car2.setEngineDisplacement(5.0);
//        car2.setMileage(10000);
//        car2.setPrice(35000.0);
//        car2.setCondition(TechnicalCondition.EXCELLENT);
//        car2.setOwner(seller);
//
//        when(carService.getCarsByStatus(CarStatus.AVAILABLE)).thenReturn(List.of(car1, car2));
//
//        List<Car> result = carSuggestionService.findCarsByRequest(request);
//
//        assertEquals(2, result.size());
//    }
//
//    @Test
//    void testFindCarsByRequest_TopThreeMatches() {
//        User user = new User();
//        user.setId(1L);
//
//        CarRequest request = new CarRequest();
//        request.setBrand("Toyota");
//        request.setModel("Camry");
//        request.setYear(2020);
//        request.setUser(user);
//        request.setBrandWeight(5);
//        request.setModelWeight(4);
//        request.setYearWeight(3);
//
//        Car car1 = new Car();
//        car1.setBrand("Toyota");
//        car1.setModel("Camry");
//        car1.setYear(2020);
//        car1.setOwner(user);
//
//        Car car2 = new Car();
//        car2.setBrand("Toyota");
//        car2.setModel("Camry");
//        car2.setYear(2019);
//
//        Car car3 = new Car();
//        car3.setBrand("Toyota");
//        car3.setModel("Corolla");
//        car3.setYear(2020);
//
//        Car car4 = new Car();
//        car4.setBrand("Honda");
//        car4.setModel("Civic");
//        car4.setYear(2020);
//
//        when(carService.getCarsByStatus(CarStatus.AVAILABLE)).thenReturn(List.of(car1, car2, car3, car4));
//
//        List<Car> result = carSuggestionService.findCarsByRequest(request);
//
//        assertEquals(3, result.size());
//        assertEquals(car2, result.get(0));
//        assertEquals(car3, result.get(1));
//        assertEquals(car4, result.get(2));
//    }
//}

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.vitaliistf.cardealership.data.Car;
import org.vitaliistf.cardealership.data.CarRequest;
import org.vitaliistf.cardealership.data.User;
import org.vitaliistf.cardealership.data.enums.*;
import org.vitaliistf.cardealership.service.CarService;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CarSuggestionServiceImplTest {

    @Mock
    private CarService carService;

    @InjectMocks
    private CarSuggestionServiceImpl carSuggestionService;

    @Test
    void findCarsByRequestPerfectMatches() {
        CarRequest carRequest = createCarRequest();
        List<Car> perfectMatches = createPerfectMatches();
        when(carService.getCarsByStatus(CarStatus.AVAILABLE)).thenReturn(perfectMatches);

        List<Car> result = carSuggestionService.findCarsByRequest(carRequest);

        assertEquals(perfectMatches.size(), result.size());
        assertEquals(perfectMatches, result);
    }

    @Test
    void findCarsByRequestRankedMatches() {
        CarRequest carRequest = createCarRequest();
        List<Car> allCars = createAllCars();
        when(carService.getCarsByStatus(CarStatus.AVAILABLE)).thenReturn(allCars);

        List<Car> result = carSuggestionService.findCarsByRequest(carRequest);

        assertEquals(3, result.size());
    }

    private CarRequest createCarRequest() {
        CarRequest carRequest = new CarRequest();
        carRequest.setBrand("Toyota");
        carRequest.setBrandWeight(5);
        carRequest.setModel("Camry");
        carRequest.setModelWeight(3);
        carRequest.setYear(2020);
        carRequest.setYearWeight(2);
        carRequest.setColor("Red");
        carRequest.setColorWeight(4);
        carRequest.setTransmission(Transmission.AUTOMATIC);
        carRequest.setTransmissionWeight(3);
        carRequest.setFuelType(FuelType.PETROL);
        carRequest.setFuelWeight(2);
        carRequest.setBodyType(BodyType.SEDAN);
        carRequest.setBodyTypeWeight(3);
        carRequest.setEngineDisplacement(2.5);
        carRequest.setEngineDisplacementWeight(2);
        carRequest.setMaxMileage(50000);
        carRequest.setMaxMileageWeight(3);
        carRequest.setMaxPrice(25000.0);
        carRequest.setMaxPriceWeight(4);
        carRequest.setCondition(TechnicalCondition.GOOD);
        carRequest.setConditionWeight(4);
        User user = new User();
        user.setId(2L);
        carRequest.setUser(user);
        return carRequest;
    }

    private List<Car> createPerfectMatches() {
        User owner = new User();
        owner.setId(1L);

        Car car1 = new Car();
        car1.setBrand("Toyota");
        car1.setModel("Camry");
        car1.setYear(2020);
        car1.setColor("Red");
        car1.setTransmission(Transmission.AUTOMATIC);
        car1.setFuelType(FuelType.PETROL);
        car1.setBodyType(BodyType.SEDAN);
        car1.setEngineDisplacement(2.5);
        car1.setMileage(40000);
        car1.setPrice(20000.0);
        car1.setCondition(TechnicalCondition.GOOD);
        car1.setOwner(owner);

        Car car2 = new Car();
        car2.setBrand("Toyota");
        car2.setModel("Camry");
        car2.setYear(2020);
        car2.setColor("Red");
        car2.setTransmission(Transmission.AUTOMATIC);
        car2.setFuelType(FuelType.PETROL);
        car2.setBodyType(BodyType.SEDAN);
        car2.setEngineDisplacement(2.5);
        car2.setMileage(45000);
        car2.setPrice(22000.0);
        car2.setCondition(TechnicalCondition.GOOD);
        car2.setOwner(owner);

        return List.of(car1, car2);
    }

    private List<Car> createAllCars() {
        User owner = new User();
        owner.setId(1L);

        Car car1 = new Car();
        car1.setBrand("Toyota");
        car1.setModel("Camry");
        car1.setYear(2015);
        car1.setColor("Blue");
        car1.setTransmission(Transmission.MANUAL);
        car1.setFuelType(FuelType.PETROL);
        car1.setBodyType(BodyType.SEDAN);
        car1.setEngineDisplacement(2.5);
        car1.setMileage(40000);
        car1.setPrice(20000.0);
        car1.setCondition(TechnicalCondition.GOOD);
        car1.setOwner(owner);

        Car car2 = new Car();
        car2.setBrand("Honda");
        car2.setModel("Accord");
        car2.setYear(2019);
        car2.setColor("Blue");
        car2.setTransmission(Transmission.AUTOMATIC);
        car2.setFuelType(FuelType.PETROL);
        car2.setBodyType(BodyType.SEDAN);
        car2.setEngineDisplacement(2.0);
        car2.setMileage(35000);
        car2.setPrice(18000.0);
        car2.setCondition(TechnicalCondition.FAIR);
        car2.setOwner(owner);

        Car car3 = new Car();
        car3.setBrand("Ford");
        car3.setModel("Mustang");
        car3.setYear(2021);
        car3.setColor("Black");
        car3.setTransmission(Transmission.MANUAL);
        car3.setFuelType(FuelType.DIESEL);
        car3.setBodyType(BodyType.COUPE);
        car3.setEngineDisplacement(5.0);
        car3.setMileage(20000);
        car3.setPrice(35000.0);
        car3.setCondition(TechnicalCondition.EXCELLENT);
        car3.setOwner(owner);

        Car car4 = new Car();
        car4.setBrand("BMW");
        car4.setModel("M3");
        car4.setYear(2021);
        car4.setColor("Black");
        car4.setTransmission(Transmission.AUTOMATIC);
        car4.setFuelType(FuelType.PETROL);
        car4.setBodyType(BodyType.COUPE);
        car4.setEngineDisplacement(4.4);
        car4.setMileage(15000);
        car4.setPrice(37000.0);
        car4.setCondition(TechnicalCondition.EXCELLENT);
        car4.setOwner(owner);

        return List.of(car1, car2, car3, car4);
    }
}
