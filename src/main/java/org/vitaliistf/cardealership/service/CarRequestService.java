package org.vitaliistf.cardealership.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.vitaliistf.cardealership.data.Car;
import org.vitaliistf.cardealership.data.CarRequest;
import org.vitaliistf.cardealership.data.User;

import java.util.List;

/**
 * Service interface for managing car requests.
 */
public interface CarRequestService {

    /**
     * Creates a new car request.
     *
     * @param request CarRequest object representing the request
     * @param user    User creating the request
     */
    void createCarRequest(CarRequest request, User user);

    /**
     * Deletes a car request by its ID.
     *
     * @param requestId ID of the car request to delete
     * @param user      User deleting the request
     */
    void deleteCarRequestById(Long requestId, User user);

    /**
     * Edits a car request.
     *
     * @param requestId     ID of the car request to edit
     * @param editedRequest CarRequest object representing the edited request
     * @param user          User editing the request
     */
    void editCarRequest(Long requestId, CarRequest editedRequest, User user);

    /**
     * Retrieves a car request by its ID.
     *
     * @param requestId ID of the car request to retrieve
     * @param user      User requesting the request
     * @return CarRequest object
     */
    CarRequest getCarRequestById(Long requestId, User user);

    /**
     * Retrieves car requests by user.
     *
     * @param user     User for whom to retrieve requests
     * @param pageable Pageable object for pagination
     * @return Page of CarRequest objects
     */
    Page<CarRequest> getCarRequestsByUser(User user, Pageable pageable);

    /**
     * Retrieves cars suggested based on a car request.
     *
     * @param request CarRequest object for which to suggest cars
     * @return List of suggested cars
     */
    List<Car> getCarsByCarRequest(CarRequest request);

}
