package org.vitaliistf.cardealership.controller;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.vitaliistf.cardealership.data.CarRequest;
import org.vitaliistf.cardealership.data.User;
import org.vitaliistf.cardealership.service.CarRequestService;
import org.vitaliistf.cardealership.util.UserExtractor;

/**
 * Controller for handling car request operations.
 */
@Controller
@RequestMapping("/car-request")
@AllArgsConstructor
public class CarRequestController {

    private final CarRequestService carRequestService;

    /**
     * Displays the page for creating a new car request.
     *
     * @param model Model object for adding attributes
     * @return View name for the create car request page
     */
    @GetMapping("/create")
    public String createCarRequestPage(Model model) {
        model.addAttribute("carRequest", new CarRequest());
        return "car-request/create-request";
    }

    /**
     * Handles the submission of the car request creation form.
     *
     * @param carRequest     New car request object to create
     * @param authentication Authentication object representing the current user
     * @return Redirect URL after car request creation
     */
    @PostMapping("/create")
    public String createCarRequest(@ModelAttribute("carRequest") @Valid CarRequest carRequest,
                                   Authentication authentication) {
        User user = UserExtractor.getUser(authentication);
        carRequestService.createCarRequest(carRequest, user);
        return "redirect:/car-request/my-requests";
    }

    /**
     * Retrieves details of a car request by its ID.
     *
     * @param id             ID of the car request
     * @param model          Model object for adding attributes
     * @param authentication Authentication object representing the current user
     * @return View name for displaying car request details
     */
    @GetMapping("/{id}")
    public String getCarRequestById(@PathVariable Long id, Model model,
                                    Authentication authentication) {
        User currentUser = UserExtractor.getUser(authentication);
        CarRequest request = carRequestService.getCarRequestById(id, currentUser);
        model.addAttribute("carRequest", request);
        model.addAttribute("matchingCars", carRequestService.getCarsByCarRequest(request));
        return "car-request/request-details";
    }

    /**
     * Displays the page for editing a car request.
     *
     * @param id             ID of the car request to edit
     * @param model          Model object for adding attributes
     * @param authentication Authentication object representing the current user
     * @return View name for the edit car request page
     */
    @GetMapping("/{id}/edit")
    public String editCarRequestPage(@PathVariable Long id, Model model,
                                     Authentication authentication) {
        User currentUser = UserExtractor.getUser(authentication);
        CarRequest request = carRequestService.getCarRequestById(id, currentUser);
        model.addAttribute("carRequest", request);
        return "car-request/edit-request";
    }

    /**
     * Handles the submission of the car request edit form.
     *
     * @param id              ID of the car request being edited
     * @param carRequest      Edited car request object
     * @param bindingResult   Binding result for validation errors
     * @param authentication Authentication object representing the current user
     * @return Redirect URL after car request edit
     */
    @PostMapping("/{id}/edit")
    public String editCarRequest(@PathVariable Long id,
                                 @ModelAttribute("carRequest") @Valid CarRequest carRequest,
                                 BindingResult bindingResult,
                                 Authentication authentication) {
        if (bindingResult.hasErrors()) {
            return "car-request/edit-request";
        }
        User user = UserExtractor.getUser(authentication);
        carRequestService.editCarRequest(id, carRequest, user);
        return "redirect:/car-request/my-requests";
    }

    /**
     * Deletes a car request by its ID.
     *
     * @param id             ID of the car request to delete
     * @param authentication Authentication object representing the current user
     * @return Redirect URL after car request deletion
     */
    @PostMapping("/{id}/delete")
    public String deleteCarRequest(@PathVariable Long id, Authentication authentication) {
        User currentUser = UserExtractor.getUser(authentication);
        carRequestService.deleteCarRequestById(id, currentUser);
        return "redirect:/car-request/my-requests";
    }

    /**
     * Retrieves all car requests of the current user.
     *
     * @param model          Model object for adding attributes
     * @param authentication Authentication object representing the current user
     * @param page           Page number for pagination
     * @param size           Page size for pagination
     * @return View name for displaying the user's car requests
     */
    @GetMapping("/my-requests")
    public String getUserCarRequests(Model model, Authentication authentication,
                                       @RequestParam(defaultValue = "0") int page,
                                       @RequestParam(defaultValue = "10") int size) {
        User user = UserExtractor.getUser(authentication);
        Pageable pageable = PageRequest.of(page, size);
        Page<CarRequest> requests = carRequestService.getCarRequestsByUser(user, pageable);
        model.addAttribute("requests", requests);
        return "car-request/my-requests";
    }

}
