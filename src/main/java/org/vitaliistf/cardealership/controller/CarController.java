package org.vitaliistf.cardealership.controller;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.vitaliistf.cardealership.data.Car;
import org.vitaliistf.cardealership.data.User;
import org.vitaliistf.cardealership.data.enums.CarStatus;
import org.vitaliistf.cardealership.data.filter.CarFilterParams;
import org.vitaliistf.cardealership.exception.ForbiddenException;
import org.vitaliistf.cardealership.service.CarService;
import org.vitaliistf.cardealership.util.UserExtractor;

import java.util.Optional;

/**
 * Controller for handling car-related operations.
 */
@Controller
@RequestMapping("/car")
@AllArgsConstructor
public class CarController {

    private final CarService carService;

    /**
     * Retrieves the page for creating a new car.
     *
     * @param model The model to be populated.
     * @return The view name for creating a new car.
     */
    @GetMapping("/create")
    public String createCarPage(Model model) {
        model.addAttribute("car", new Car());
        return "car/create-car";
    }

    /**
     * Handles the submission of the car creation form.
     *
     * @param car            New car object to create
     * @param bindingResult  Binding result for validation errors
     * @param photo          Car photo file
     * @param authentication Authentication object representing the current user
     * @return Redirect URL after car creation
     */
    @PostMapping("/create")
    public String createCar(@Valid @ModelAttribute("car") Car car, BindingResult bindingResult,
                            @RequestParam("photo") MultipartFile photo, Authentication authentication) {
        if (bindingResult.hasErrors()) {
            return "car/create-car";
        }

        User user = UserExtractor.getUser(authentication);
        carService.createCar(car, photo, user);
        return "redirect:/car";
    }

    /**
     * Retrieves details of a car by its ID.
     *
     * @param id    ID of the car
     * @param model Model object for adding attributes
     * @return View name for displaying car details
     */
    @GetMapping("/{id}")
    public String getCarById(@PathVariable Long id, Model model) {
        model.addAttribute("car", carService.getCarById(id));
        return "car/car-details";
    }

    /**
     * Deletes a car by its ID.
     *
     * @param id             ID of the car to delete
     * @param authentication Authentication object representing the current user
     * @return Redirect URL after car deletion
     */
    @PostMapping("/{id}/delete")
    public String deleteCarById(@PathVariable Long id, Authentication authentication) {
        User user = UserExtractor.getUser(authentication);
        carService.deleteCarById(id, user);
        return "redirect:/car/my-cars";
    }

    /**
     * Displays the page for editing a car.
     *
     * @param id             ID of the car to edit
     * @param model          Model object for adding attributes
     * @param authentication Authentication object representing the current user
     * @return View name for the edit car page
     * @throws ForbiddenException if the user does not have permission to edit the car
     */
    @GetMapping("/{id}/edit")
    public String editCarPage(@PathVariable Long id,
                              Model model,
                              Authentication authentication) {
        User user = UserExtractor.getUser(authentication);
        Car car = carService.getCarById(id);
        carService.validateUserAccessToEdit(car, user);
        model.addAttribute("car", car);
        return "car/edit-car";
    }

    /**
     * Handles the submission of the car edit form.
     *
     * @param id              ID of the car being edited
     * @param editedCar       Edited car object
     * @param bindingResult   Binding result for validation errors
     * @param photo           Car photo file
     * @param authentication  Authentication object representing the current user
     * @return Redirect URL after car edit
     */
    @PostMapping("/{id}/edit")
    public String editCar(@PathVariable Long id,
                          @Valid @ModelAttribute("car") Car editedCar,
                          BindingResult bindingResult,
                          @RequestParam("photo") MultipartFile photo,
                          Authentication authentication) {
        if (bindingResult.hasErrors()) {
            return "car/edit-car";
        }
        User user = UserExtractor.getUser(authentication);
        carService.editCar(id, editedCar, photo, user);
        return "redirect:/car";
    }

    /**
     * Retrieves cars owned by the current user.
     *
     * @param model          Model object for adding attributes
     * @param authentication Authentication object representing the current user
     * @param carStatus      Status of cars to filter (optional)
     * @param pageable       Pageable object for pagination
     * @return View name for displaying the user's cars
     */
    @GetMapping("/my-cars")
    public String getMyCars(Model model, Authentication authentication,
                            @RequestParam(required = false) CarStatus carStatus,
                            @PageableDefault(size = 12, sort = "id") Pageable pageable) {
        User user = UserExtractor.getUser(authentication);
        Page<Car> cars;
        if (carStatus == null) {
            cars = carService.getCarsByOwner(user, pageable);
        } else {
            cars = carService.getCarsByOwnerAndStatus(user, carStatus, pageable);
        }
        model.addAttribute("cars", cars);
        model.addAttribute("carStatus", carStatus);
        return "car/my-cars";
    }

    /**
     * Retrieves all available cars based on filtering parameters.
     *
     * @param filterParams Filter parameters for car search
     * @param model        Model object for adding attributes
     * @param pageable     Pageable object for pagination
     * @return View name for displaying all available cars
     */
    @GetMapping
    public String getAllCars(@ModelAttribute CarFilterParams filterParams, Model model,
                             @PageableDefault(size = 12) Pageable pageable) {
        Sort sort = Sort.by(Sort.Direction.fromString(filterParams.getSortOrder()), filterParams.getSortBy());
        Pageable sortedPageable = PageRequest.of(pageable.getPageNumber(), filterParams.getSize(), sort);
        Page<Car> cars = carService.getAvailableCars(filterParams, sortedPageable);
        model.addAttribute("cars", cars);
        model.addAttribute("filterParams", filterParams);
        return "car/all-cars";
    }

    /**
     * Retrieves the photo of a car by its ID.
     *
     * @param carId ID of the car
     * @return ResponseEntity with car photo bytes if found, or 404 if not found
     */
    @GetMapping("/{carId}/photo")
    public ResponseEntity<byte[]> getCarPhotoById(@PathVariable Long carId) {
        Optional<byte[]> photoBytes = carService.getCarPhotoById(carId);
        if (photoBytes.isPresent()) {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.IMAGE_JPEG);
            return new ResponseEntity<>(photoBytes.get(), headers, HttpStatus.OK);
        }
        return ResponseEntity.notFound().build();
    }
}
