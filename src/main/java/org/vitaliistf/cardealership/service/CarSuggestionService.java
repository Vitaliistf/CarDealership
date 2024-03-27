package org.vitaliistf.cardealership.service;

import org.vitaliistf.cardealership.data.Car;
import org.vitaliistf.cardealership.data.CarRequest;

import java.util.List;

/**
 * Service interface providing functionalities for suggesting cars based on car requests.
 */
public interface CarSuggestionService {

    /**
     * Finds cars that match the given car request.
     *
     * @param carRequest CarRequest object representing the request
     * @return List of cars matching the request
     */
    List<Car> findCarsByRequest(CarRequest carRequest);

}
