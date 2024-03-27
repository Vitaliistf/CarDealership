package org.vitaliistf.cardealership.service.implementation;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.vitaliistf.cardealership.data.Car;
import org.vitaliistf.cardealership.data.CarRequest;
import org.vitaliistf.cardealership.data.enums.CarStatus;
import org.vitaliistf.cardealership.service.CarService;
import org.vitaliistf.cardealership.service.CarSuggestionService;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Implementation of {@link CarSuggestionService} providing functionalities for suggesting cars based on car requests.
 */
@Service
@AllArgsConstructor
public class CarSuggestionServiceImpl implements CarSuggestionService {

    private final CarService carService;

    /**
     * Finds cars that match the given car request.
     *
     * @param carRequest CarRequest object representing the request
     * @return List of cars matching the request
     */
    @Override
    public List<Car> findCarsByRequest(CarRequest carRequest) {
        List<Car> allCars = carService.getCarsByStatus(CarStatus.AVAILABLE);
        List<Car> perfectMatches = allCars.stream()
                .filter(car -> !car.getOwner().getId().equals(carRequest.getUser().getId()))
                .filter(car -> matchesRequest(car, carRequest))
                .collect(Collectors.toList());

        if (!perfectMatches.isEmpty()) {
            return perfectMatches;
        }

        return allCars.stream()
                .filter(car -> !car.getOwner().getId().equals(carRequest.getUser().getId()))
                .sorted(Comparator.comparingDouble(car -> calculateScore((Car) car, carRequest)).reversed())
                .limit(6)
                .collect(Collectors.toList());
    }

    /**
     * Checks if a car matches a car request.
     *
     * @param car        Car to check
     * @param carRequest CarRequest object representing the request
     * @return true if the car matches the request, false otherwise
     */
    private boolean matchesRequest(Car car, CarRequest carRequest) {
        return (carRequest.getBrand() == null || car.getBrand().equalsIgnoreCase(carRequest.getBrand())) &&
                (carRequest.getModel() == null || car.getModel().equalsIgnoreCase(carRequest.getModel())) &&
                (carRequest.getYear() == null || car.getYear().equals(carRequest.getYear())) &&
                (carRequest.getColor() == null || car.getColor().equalsIgnoreCase(carRequest.getColor())) &&
                (carRequest.getTransmission() == null || car.getTransmission().equals(carRequest.getTransmission())) &&
                (carRequest.getFuelType() == null || car.getFuelType().equals(carRequest.getFuelType())) &&
                (carRequest.getBodyType() == null || car.getBodyType().equals(carRequest.getBodyType())) &&
                (carRequest.getEngineDisplacement() == null || car.getEngineDisplacement().equals(carRequest.getEngineDisplacement())) &&
                (carRequest.getMaxMileage() == null || car.getMileage() <= carRequest.getMaxMileage()) &&
                (carRequest.getMaxPrice() == null || car.getPrice() <= carRequest.getMaxPrice()) &&
                (carRequest.getCondition() == null || car.getCondition().equals(carRequest.getCondition()));
    }

    /**
     * Calculates the score of a car based on a car request.
     *
     * @param car        Car to calculate the score for
     * @param carRequest CarRequest object representing the request
     * @return Score of the car
     */
    private double calculateScore(Car car, CarRequest carRequest) {
        double score = 0.0;
        if (!carRequest.getBrand().isBlank() && car.getBrand().equalsIgnoreCase(carRequest.getBrand())) {
            score += carRequest.getBrandWeight();
        }
        if (!carRequest.getModel().isBlank() && car.getModel().equalsIgnoreCase(carRequest.getModel())) {
            score += carRequest.getModelWeight();
        }
        if (carRequest.getYear() != null && car.getYear().equals(carRequest.getYear())) {
            score += carRequest.getYearWeight();
        }
        if (!carRequest.getColor().isBlank() && car.getColor().equalsIgnoreCase(carRequest.getColor())) {
            score += carRequest.getColorWeight();
        }
        if (carRequest.getTransmission() != null && car.getTransmission().equals(carRequest.getTransmission())) {
            score += carRequest.getTransmissionWeight();
        }
        if (carRequest.getFuelWeight() != null && car.getFuelType().equals(carRequest.getFuelType())) {
            score += carRequest.getFuelWeight();
        }
        if (carRequest.getBodyType() != null && car.getBodyType().equals(carRequest.getBodyType())) {
            score += carRequest.getBodyTypeWeight();
        }
        if (carRequest.getEngineDisplacement() != null && car.getEngineDisplacement().equals(carRequest.getEngineDisplacement())) {
            score += carRequest.getEngineDisplacementWeight();
        }
        if (carRequest.getMaxMileage() != null && car.getMileage() <= carRequest.getMaxMileage()) {
            score += carRequest.getMaxMileageWeight();
        }
        if (carRequest.getMaxPrice() != null && car.getPrice() <= carRequest.getMaxPrice()) {
            score += carRequest.getMaxPriceWeight();
        }
        if (carRequest.getCondition() != null && car.getCondition().equals(carRequest.getCondition())) {
            score += carRequest.getConditionWeight();
        }
        return score;
    }

}
