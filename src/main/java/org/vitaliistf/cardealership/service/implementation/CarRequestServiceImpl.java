package org.vitaliistf.cardealership.service.implementation;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.vitaliistf.cardealership.data.Car;
import org.vitaliistf.cardealership.data.CarRequest;
import org.vitaliistf.cardealership.data.User;
import org.vitaliistf.cardealership.exception.ForbiddenException;
import org.vitaliistf.cardealership.exception.NotFoundException;
import org.vitaliistf.cardealership.repository.CarRequestRepository;
import org.vitaliistf.cardealership.service.CarRequestService;
import org.vitaliistf.cardealership.service.CarSuggestionService;

import java.util.List;
import java.util.Objects;

/**
 * Implementation of {@link CarRequestService} providing functionalities related to car requests.
 */
@Service
@AllArgsConstructor
public class CarRequestServiceImpl implements CarRequestService {

    private final CarRequestRepository carRequestRepository;

    private final CarSuggestionService carSuggestionService;

    /**
     * Creates a new car request.
     *
     * @param request CarRequest object representing the request
     * @param user    User creating the request
     */
    @Override
    public void createCarRequest(CarRequest request, User user) {
        request.setUser(user);
        carRequestRepository.save(request);
    }

    /**
     * Deletes a car request by its ID.
     *
     * @param id   ID of the car request to delete
     * @param user User deleting the request
     */
    @Override
    public void deleteCarRequestById(Long id, User user) {
        CarRequest request = getCarRequestById(id);
        validateCarRequestAccess(request, user);
        carRequestRepository.deleteById(id);
    }

    /**
     * Edits a car request.
     *
     * @param id            ID of the car request to edit
     * @param editedRequest CarRequest object representing the edited request
     * @param user          User editing the request
     */
    @Override
    public void editCarRequest(Long id, CarRequest editedRequest, User user) {
        CarRequest request = getCarRequestById(id);
        validateCarRequestAccess(request, user);
        editedRequest.setId(id);
        editedRequest.setUser(user);
        carRequestRepository.save(editedRequest);
    }

    /**
     * Retrieves a car request by its ID.
     *
     * @param id   ID of the car request to retrieve
     * @param user User requesting the request
     * @return CarRequest object
     */
    @Override
    public CarRequest getCarRequestById(Long id, User user) {
        CarRequest request = getCarRequestById(id);
        validateCarRequestAccess(request, user);
        return request;
    }

    /**
     * Retrieves car requests by user.
     *
     * @param user     User for whom to retrieve requests
     * @param pageable Pageable object for pagination
     * @return Page of CarRequest objects
     */
    @Override
    public Page<CarRequest> getCarRequestsByUser(User user, Pageable pageable) {
        return carRequestRepository.findByUser(user, pageable);
    }

    /**
     * Retrieves cars suggested based on a car request.
     *
     * @param request CarRequest object for which to suggest cars
     * @return List of suggested cars
     */
    @Override
    public List<Car> getCarsByCarRequest(CarRequest request) {
        return carSuggestionService.findCarsByRequest(request);
    }

    /**
     * Retrieves a car request by its ID, throwing an exception if not found.
     *
     * @param id ID of the car request
     * @return CarRequest object
     */
    private CarRequest getCarRequestById(Long id) {
        return carRequestRepository.findById(id).orElseThrow(
                () -> new NotFoundException("Request not found.")
        );
    }

    /**
     * Checks if a user has access to a particular car request.
     *
     * @param request CarRequest to check access for
     * @param user    User to check access against
     * @throws ForbiddenException if the user has no permission to access the request
     */
    private void validateCarRequestAccess(CarRequest request, User user) {
        if (!Objects.equals(request.getUser().getId(), user.getId())) {
            throw new ForbiddenException("You have no permission for this operation.");
        }
    }

}
