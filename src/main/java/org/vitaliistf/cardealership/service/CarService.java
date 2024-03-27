package org.vitaliistf.cardealership.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;
import org.vitaliistf.cardealership.data.Car;
import org.vitaliistf.cardealership.data.User;
import org.vitaliistf.cardealership.data.enums.CarStatus;
import org.vitaliistf.cardealership.data.filter.CarFilterParams;

import java.util.List;
import java.util.Optional;

/**
 * Service interface for managing cars.
 */
public interface CarService {

    /**
     * Creates a new car.
     *
     * @param car   Car object representing the car to be created
     * @param photo MultipartFile containing the car's photo
     * @param user  User creating the car
     */
    void createCar(Car car, MultipartFile photo, User user);

    /**
     * Edits an existing car.
     *
     * @param carId     ID of the car to be edited
     * @param editedCar Car object representing the edited car
     * @param photo     MultipartFile containing the car's photo
     * @param user      User editing the car
     */
    void editCar(Long carId, Car editedCar, MultipartFile photo, User user);

    /**
     * Deletes a car by its ID.
     *
     * @param carId ID of the car to delete
     * @param user  User deleting the car
     */
    void deleteCarById(Long carId, User user);

    /**
     * Retrieves a car by its ID.
     *
     * @param carId ID of the car to retrieve
     * @return Car object
     */
    Car getCarById(Long carId);

    /**
     * Checks if a user can edit a car.
     *
     * @param car  Car to check
     * @param user User attempting to edit the car
     */
    void validateUserAccessToEdit(Car car, User user);

    /**
     * Retrieves cars by status.
     *
     * @param status CarStatus indicating the status of cars to retrieve
     * @return List of cars with the specified status
     */
    List<Car> getCarsByStatus(CarStatus status);

    /**
     * Retrieves cars by owner.
     *
     * @param owner    User who owns the cars
     * @param pageable Pageable object for pagination
     * @return Page of cars owned by the specified user
     */
    Page<Car> getCarsByOwner(User owner, Pageable pageable);

    /**
     * Retrieves cars by owner and status.
     *
     * @param owner    User who owns the cars
     * @param status   CarStatus indicating the status of cars to retrieve
     * @param pageable Pageable object for pagination
     * @return Page of cars owned by the specified user with the specified status
     */
    Page<Car> getCarsByOwnerAndStatus(User owner, CarStatus status, Pageable pageable);

    /**
     * Retrieves available cars based on filter parameters.
     *
     * @param filterParams CarFilterParams object containing filter parameters
     * @param pageable     Pageable object for pagination
     * @return Page of available cars matching the specified filter parameters
     */
    Page<Car> getAvailableCars(CarFilterParams filterParams, Pageable pageable);

    /**
     * Retrieves the photo of a car by its ID.
     *
     * @param carId ID of the car
     * @return Optional containing the photo bytes if available, otherwise empty
     */
    Optional<byte[]> getCarPhotoById(Long carId);
}
