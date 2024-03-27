package org.vitaliistf.cardealership.service.implementation;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;
import org.vitaliistf.cardealership.data.Car;
import org.vitaliistf.cardealership.data.User;
import org.vitaliistf.cardealership.data.enums.*;
import org.vitaliistf.cardealership.exception.BadRequestException;
import org.vitaliistf.cardealership.exception.ForbiddenException;
import org.vitaliistf.cardealership.exception.NotFoundException;
import org.vitaliistf.cardealership.repository.CarRepository;
import org.vitaliistf.cardealership.service.ImageService;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

//@ExtendWith(MockitoExtension.class)
class CarServiceImplTest {

    @Mock
    private CarRepository carRepository;

    @Mock
    private ImageService imageService;

    @InjectMocks
    private CarServiceImpl carService;

    private User owner;
    private Car car;
    private MultipartFile validImage;
    private MultipartFile invalidImage;
    private MultipartFile emptyFile;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        owner = new User();
        owner.setId(1L);

        car = new Car();
        car.setId(1L);
        car.setBrand("Toyota");
        car.setModel("Camry");
        car.setYear(2020);
        car.setColor("Red");
        car.setTransmission(Transmission.AUTOMATIC);
        car.setFuelType(FuelType.PETROL);
        car.setBodyType(BodyType.SEDAN);
        car.setStatus(CarStatus.AVAILABLE);
        car.setEngineDisplacement(2.5);
        car.setMileage(10000);
        car.setDescription("Excellent condition, low mileage.");
        car.setPrice(25000.0);
        car.setCondition(TechnicalCondition.EXCELLENT);
        car.setOwner(owner);

        validImage = mock(MultipartFile.class);
        when(validImage.getContentType()).thenReturn("image/jpeg");

        invalidImage = mock(MultipartFile.class);
        when(invalidImage.getContentType()).thenReturn("application/pdf");

        emptyFile = new MockMultipartFile("file", new byte[0]);
    }

    @Test
    void testCreateCarWithValidInput() {
        when(imageService.saveImage(validImage)).thenReturn("https://example.com/image.jpg");

        carService.createCar(car, validImage, owner);

        verify(carRepository, times(1)).save(car);
        assertEquals(CarStatus.AVAILABLE, car.getStatus());
        assertEquals(owner, car.getOwner());
        assertEquals("https://example.com/image.jpg", car.getPhotoUrl());
    }

    @Test
    void testCreateCarWithNoImage() {
        carService.createCar(car, emptyFile, owner);

        verify(carRepository, times(1)).save(car);
        assertEquals(CarStatus.AVAILABLE, car.getStatus());
        assertEquals(owner, car.getOwner());
        assertNull(car.getPhotoUrl());
    }

    @Test
    void testCreateCarWithInvalidImage() {
        assertThrows(BadRequestException.class, () -> carService.createCar(car, invalidImage, owner));
        verifyNoInteractions(carRepository);
    }

    @Test
    void testEditCarWithValidInput() {
        when(carRepository.findById(car.getId())).thenReturn(Optional.of(car));
        when(imageService.saveImage(validImage)).thenReturn("https://example.com/new-image.jpg");

        Car editedCar = new Car();
        editedCar.setBrand("Honda");
        editedCar.setModel("Civic");

        carService.editCar(car.getId(), editedCar, validImage, owner);

        verify(carRepository, times(1)).save(editedCar);
        assertEquals(owner, editedCar.getOwner());
        assertEquals(CarStatus.AVAILABLE, editedCar.getStatus());
        assertEquals("https://example.com/new-image.jpg", editedCar.getPhotoUrl());
    }

    @Test
    void testEditCarWithNoImage() {
        when(carRepository.findById(car.getId())).thenReturn(Optional.of(car));

        Car editedCar = new Car();
        editedCar.setBrand("Honda");
        editedCar.setModel("Civic");

        carService.editCar(car.getId(), editedCar, emptyFile, owner);

        verify(carRepository, times(1)).save(editedCar);
        assertEquals(owner, editedCar.getOwner());
        assertEquals(CarStatus.AVAILABLE, editedCar.getStatus());
        assertEquals(car.getPhotoUrl(), editedCar.getPhotoUrl());
    }

    @Test
    void testEditCarWhenCarNotFound() {
        when(carRepository.findById(car.getId())).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> carService.editCar(car.getId(), car, validImage, owner));
        verifyNoInteractions(imageService);
    }

    @Test
    void testEditCarWhenUserNotOwner() {
        Car carOwnedByAnotherUser = new Car();
        carOwnedByAnotherUser.setOwner(new User());

        when(carRepository.findById(car.getId())).thenReturn(Optional.of(carOwnedByAnotherUser));

        assertThrows(ForbiddenException.class, () -> carService.editCar(car.getId(), car, validImage, owner));
        verifyNoInteractions(imageService);
    }

    @Test
    void testEditCarWhenNotAvailable() {
        Car carNotAvailable = new Car();
        carNotAvailable.setOwner(owner);
        carNotAvailable.setStatus(CarStatus.ORDERED);

        when(carRepository.findById(car.getId())).thenReturn(Optional.of(carNotAvailable));

        assertThrows(ForbiddenException.class, () -> carService.editCar(car.getId(), car, validImage, owner));
        verifyNoInteractions(imageService);
    }

    @Test
    void testDeleteCarByIdWithValidInput() {
        when(carRepository.findById(car.getId())).thenReturn(Optional.of(car));

        carService.deleteCarById(car.getId(), owner);

        verify(carRepository, times(1)).save(car);
        assertEquals(CarStatus.ARCHIVED, car.getStatus());
    }

    @Test
    void testDeleteCarByIdWhenCarNotFound() {
        when(carRepository.findById(car.getId())).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> carService.deleteCarById(car.getId(), owner));
    }

    @Test
    void testDeleteCarByIdWhenUserNotOwner() {
        User anotherUser = new User();
        Car carOwnedByAnotherUser = new Car();
        carOwnedByAnotherUser.setOwner(anotherUser);

        when(carRepository.findById(car.getId())).thenReturn(Optional.of(carOwnedByAnotherUser));

        assertThrows(ForbiddenException.class, () -> carService.deleteCarById(car.getId(), owner));
    }

    @Test
    void testGetCarByIdWithExistingCar() {
        when(carRepository.findById(car.getId())).thenReturn(Optional.of(car));

        Car result = carService.getCarById(car.getId());

        assertEquals(car, result);
    }

    @Test
    void testGetCarByIdWhenCarNotFound() {
        when(carRepository.findById(car.getId())).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> carService.getCarById(car.getId()));
    }

    @Test
    void testValidateUserAccessToEditWhenUserIsOwnerAndCarAvailable() {
        assertDoesNotThrow(() -> carService.validateUserAccessToEdit(car, owner));
    }

    @Test
    void testValidateUserAccessToEditWhenUserNotOwner() {
        User anotherUser = new User();

        assertThrows(ForbiddenException.class, () -> carService.validateUserAccessToEdit(car, anotherUser));
    }

    @Test
    void testValidateUserAccessToEditWhenCarNotAvailable() {
        Car unavailableCar = new Car();
        unavailableCar.setOwner(owner);
        unavailableCar.setStatus(CarStatus.ORDERED);

        assertThrows(ForbiddenException.class, () -> carService.validateUserAccessToEdit(unavailableCar, owner));
    }

    @Test
    void testGetCarsByStatusWithExistingCars() {
        List<Car> cars = List.of(car);
        when(carRepository.findByStatus(CarStatus.AVAILABLE)).thenReturn(cars);

        List<Car> result = carService.getCarsByStatus(CarStatus.AVAILABLE);

        assertEquals(cars, result);
    }

    @Test
    void testGetCarsByOwnerWithExistingCars() {
        Pageable pageable = PageRequest.of(0, 10);
        List<Car> cars = List.of(car);
        Page<Car> page = new PageImpl<>(cars, pageable, cars.size());

        when(carRepository.findByOwner(owner, pageable)).thenReturn(page);

        Page<Car> result = carService.getCarsByOwner(owner, pageable);

        assertEquals(page, result);
    }

    @Test
    void testGetCarsByOwnerAndStatusWithExistingCars() {
        Pageable pageable = PageRequest.of(0, 10);
        List<Car> cars = List.of(car);
        Page<Car> page = new PageImpl<>(cars, pageable, cars.size());

        when(carRepository.findByOwnerAndStatus(owner, CarStatus.AVAILABLE, pageable)).thenReturn(page);

        Page<Car> result = carService.getCarsByOwnerAndStatus(owner, CarStatus.AVAILABLE, pageable);

        assertEquals(page, result);
    }

    @Test
    void testGetCarPhotoByIdWithExistingCarWithPhoto() {
        when(carRepository.findById(car.getId())).thenReturn(Optional.of(car));
        car.setPhotoUrl("https://example.com/image.jpg");
        when(imageService.getImage(car.getPhotoUrl())).thenReturn("photo-bytes".getBytes());

        Optional<byte[]> result = carService.getCarPhotoById(car.getId());

        assertTrue(result.isPresent());
        assertArrayEquals("photo-bytes".getBytes(), result.get());
    }

    @Test
    void testGetCarPhotoByIdWithExistingCarWithoutPhoto() {
        Car carWithoutPhoto = new Car();
        carWithoutPhoto.setId(2L);

        when(carRepository.findById(carWithoutPhoto.getId())).thenReturn(Optional.of(carWithoutPhoto));

        Optional<byte[]> result = carService.getCarPhotoById(carWithoutPhoto.getId());

        assertTrue(result.isEmpty());
    }

    @Test
    void testGetCarPhotoByIdWhenCarNotFound() {
        when(carRepository.findById(car.getId())).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> carService.getCarPhotoById(car.getId()));
    }
}
