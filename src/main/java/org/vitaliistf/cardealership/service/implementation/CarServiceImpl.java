package org.vitaliistf.cardealership.service.implementation;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.vitaliistf.cardealership.data.Car;
import org.vitaliistf.cardealership.data.User;
import org.vitaliistf.cardealership.data.enums.CarStatus;
import org.vitaliistf.cardealership.data.filter.CarFilterParams;
import org.vitaliistf.cardealership.exception.BadRequestException;
import org.vitaliistf.cardealership.exception.ForbiddenException;
import org.vitaliistf.cardealership.exception.NotFoundException;
import org.vitaliistf.cardealership.repository.CarRepository;
import org.vitaliistf.cardealership.repository.specification.CarSpecifications;
import org.vitaliistf.cardealership.service.CarService;
import org.vitaliistf.cardealership.service.ImageService;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * Implementation of {@link CarService} providing functionalities related to cars.
 */
@Service
@AllArgsConstructor
public class CarServiceImpl implements CarService {

    private final CarRepository carRepository;
    private final ImageService imageService;

    /**
     * Creates a new car.
     *
     * @param car   Car object representing the car to be created
     * @param photo MultipartFile containing the car's photo
     * @param user  User creating the car
     */
    @Override
    public void createCar(Car car, MultipartFile photo, User user) {
        if (!photo.isEmpty()) {
            validateImage(photo);
            car.setPhotoUrl(imageService.saveImage(photo));
        }
        car.setOwner(user);
        car.setStatus(CarStatus.AVAILABLE);
        carRepository.save(car);
    }

    /**
     * Edits an existing car.
     *
     * @param carId     ID of the car to be edited
     * @param editedCar Car object representing the edited car
     * @param photo     MultipartFile containing the car's photo
     * @param user      User editing the car
     */
    @Override
    public void editCar(Long carId, Car editedCar, MultipartFile photo, User user) {
        Car car = getCarById(carId);
        validateUserAccessToEdit(car, user);
        editedCar.setId(carId);
        editedCar.setOwner(user);
        editedCar.setStatus(car.getStatus());
        if (!photo.isEmpty()) {
            validateImage(photo);
            if (car.getPhotoUrl() != null) {
                imageService.deleteImage(car.getPhotoUrl());
            }
            editedCar.setPhotoUrl(imageService.saveImage(photo));
        }
        carRepository.save(editedCar);
    }

    /**
     * Deletes a car by its ID.
     *
     * @param carId ID of the car to delete
     * @param user  User deleting the car
     */
    @Override
    public void deleteCarById(Long carId, User user) {
        Car car = getCarById(carId);
        validateUserAccessToEdit(car, user);
        car.setStatus(CarStatus.ARCHIVED);
        carRepository.save(car);
    }

    /**
     * Retrieves a car by its ID.
     *
     * @param carId ID of the car to retrieve
     * @return Car object
     * @throws NotFoundException if the car is not found
     */
    @Override
    public Car getCarById(Long carId) {
        return carRepository.findById(carId).orElseThrow(
                () -> new NotFoundException("Car not found.")
        );
    }

    /**
     * Checks if a user can edit a car.
     *
     * @param car  Car to check
     * @param user User attempting to edit the car
     * @throws ForbiddenException if user has no permission
     */
    @Override
    public void validateUserAccessToEdit(Car car, User user) {
        if (!(Objects.equals(user.getId(), car.getOwner().getId()) &&
                car.getStatus().equals(CarStatus.AVAILABLE))) {
            throw new ForbiddenException("You have no permission for this operation.");
        }
    }

    /**
     * Retrieves cars by status.
     *
     * @param status CarStatus indicating the status of cars to retrieve
     * @return List of cars with the specified status
     */
    @Override
    public List<Car> getCarsByStatus(CarStatus status) {
        return carRepository.findByStatus(status);
    }

    /**
     * Retrieves cars by owner.
     *
     * @param owner    User who owns the cars
     * @param pageable Pageable object for pagination
     * @return Page of cars owned by the specified user
     */
    @Override
    public Page<Car> getCarsByOwner(User owner, Pageable pageable) {
        return carRepository.findByOwner(owner, pageable);
    }

    /**
     * Retrieves cars by owner and status.
     *
     * @param owner    User who owns the cars
     * @param status   CarStatus indicating the status of cars to retrieve
     * @param pageable Pageable object for pagination
     * @return Page of cars owned by the specified user with the specified status
     */
    @Override
    public Page<Car> getCarsByOwnerAndStatus(User owner, CarStatus status, Pageable pageable) {
        return carRepository.findByOwnerAndStatus(owner, status, pageable);
    }

    /**
     * Retrieves available cars based on filter parameters.
     *
     * @param filterParams CarFilterParams object containing filter parameters
     * @param pageable     Pageable object for pagination
     * @return Page of available cars matching the specified filter parameters
     */
    @Override
    public Page<Car> getAvailableCars(CarFilterParams filterParams, Pageable pageable) {
        Specification<Car> spec = getCarSpecification(filterParams);
        return carRepository.findAll(
                Specification.where(spec).and(CarSpecifications.carStatus(CarStatus.AVAILABLE)), pageable
        );
    }

    /**
     * Retrieves the photo of a car by its ID.
     *
     * @param carId ID of the car
     * @return Optional containing the photo bytes if available, otherwise empty
     */
    @Override
    public Optional<byte[]> getCarPhotoById(Long carId) {
        Car car = getCarById(carId);
        if (car.getPhotoUrl() != null) {
            byte[] photoBytes = imageService.getImage(car.getPhotoUrl());
            return Optional.of(photoBytes);
        }
        return Optional.empty();
    }

    /**
     * Validates the image.
     *
     * @param image Image to be validated
     * @throws BadRequestException if the file is not an image
     */
    private void validateImage(MultipartFile image) {
        String contentType = image.getContentType();
        if (contentType == null || !contentType.startsWith("image/")) {
            throw new BadRequestException("Only image files are allowed.");
        }
    }

    /**
     * Constructs a JPA Specification object based on the filter parameters.
     *
     * @param filterParams CarFilterParams object containing filter parameters
     * @return Specification object representing the filtering criteria
     */
    private Specification<Car> getCarSpecification(CarFilterParams filterParams) {
        Specification<Car> spec = Specification.where(null);

        if (filterParams.getBrand() != null && !filterParams.getBrand().isEmpty()) {
            spec = spec.and(CarSpecifications.brandContains(filterParams.getBrand()));
        }
        if (filterParams.getCarModel() != null && !filterParams.getCarModel().isEmpty()) {
            spec = spec.and(CarSpecifications.modelContains(filterParams.getCarModel()));
        }
        if (filterParams.getYearFrom() != null) {
            spec = spec.and(CarSpecifications.yearGreaterThanOrEqual(filterParams.getYearFrom()));
        }
        if (filterParams.getYearTo() != null) {
            spec = spec.and(CarSpecifications.yearLessThanOrEqual(filterParams.getYearTo()));
        }
        if (filterParams.getBodyType() != null) {
            spec = spec.and(CarSpecifications.bodyType(filterParams.getBodyType()));
        }
        if (filterParams.getFuelType() != null) {
            spec = spec.and(CarSpecifications.fuelType(filterParams.getFuelType()));
        }
        if (filterParams.getTransmission() != null) {
            spec = spec.and(CarSpecifications.transmission(filterParams.getTransmission()));
        }
        if (filterParams.getCondition() != null) {
            spec = spec.and(CarSpecifications.condition(filterParams.getCondition()));
        }
        if (filterParams.getColor() != null && !filterParams.getColor().isEmpty()) {
            spec = spec.and(CarSpecifications.colorContains(filterParams.getColor()));
        }
        if (filterParams.getEngineDisplacementFrom() != null) {
            spec = spec.and(CarSpecifications.engineDisplacementGreaterThanOrEqual(filterParams.getEngineDisplacementFrom()));
        }
        if (filterParams.getEngineDisplacementTo() != null) {
            spec = spec.and(CarSpecifications.engineDisplacementLessThanOrEqual(filterParams.getEngineDisplacementTo()));
        }
        if (filterParams.getMileageFrom() != null) {
            spec = spec.and(CarSpecifications.mileageGreaterThanOrEqual(filterParams.getMileageFrom()));
        }
        if (filterParams.getMileageTo() != null) {
            spec = spec.and(CarSpecifications.mileageLessThanOrEqual(filterParams.getMileageTo()));
        }
        if (filterParams.getPriceFrom() != null) {
            spec = spec.and(CarSpecifications.priceGreaterThanOrEqual(filterParams.getPriceFrom()));
        }
        if (filterParams.getPriceTo() != null) {
            spec = spec.and(CarSpecifications.priceLessThanOrEqual(filterParams.getPriceTo()));
        }
        return spec;
    }


}
