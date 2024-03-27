package org.vitaliistf.cardealership.controller;

import lombok.AllArgsConstructor;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.vitaliistf.cardealership.data.CarOrder;
import org.vitaliistf.cardealership.data.User;
import org.vitaliistf.cardealership.service.CarOrderService;
import org.vitaliistf.cardealership.util.UserExtractor;

import java.util.List;

/**
 * Controller for handling car order operations.
 */
@Controller
@RequestMapping("/car-order")
@AllArgsConstructor
public class CarOrderController {

    private final CarOrderService orderService;

    /**
     * Creates a new car order.
     *
     * @param carId          ID of the car to order
     * @param authentication Authentication object representing the current user
     * @return Redirect URL after order creation
     */
    @PostMapping("/create")
    public String createCarOrder(@RequestParam("carId") Long carId,
                                 Authentication authentication) {
        User user = UserExtractor.getUser(authentication);
        orderService.createCarOrder(carId, user);
        return "redirect:/car-order/my-orders";
    }

    /**
     * Retrieves details of a car order by its ID.
     *
     * @param id             ID of the car order
     * @param model          Model object for adding attributes
     * @param authentication Authentication object representing the current user
     * @return View name for displaying car order details
     */
    @GetMapping("/{id}")
    public String getCarOrderById(@PathVariable Long id, Model model, Authentication authentication) {
        User currentUser = UserExtractor.getUser(authentication);
        CarOrder order = orderService.getCarOrderById(id, currentUser);
        model.addAttribute("order", order);
        return "car-order/order-details";
    }

    /**
     * Completes a car order by its ID.
     *
     * @param id             ID of the car order to complete
     * @param authentication Authentication object representing the current user
     * @return Redirect URL after order completion
     */
    @PostMapping("/{id}/complete")
    public String completeCarOrderById(@PathVariable Long id, Authentication authentication) {
        User currentUser = UserExtractor.getUser(authentication);
        orderService.completeCarOrderById(id, currentUser);
        return "redirect:/car-order/my-orders";
    }

    /**
     * Confirms a car order by its ID.
     *
     * @param id             ID of the car order to confirm
     * @param authentication Authentication object representing the current user
     * @return Redirect URL after order confirmation
     */
    @PostMapping("/{id}/confirm")
    public String confirmCarOrderById(@PathVariable Long id, Authentication authentication) {
        User currentUser = UserExtractor.getUser(authentication);
        orderService.confirmCarOrderById(id, currentUser);
        return "redirect:/car-order/my-orders";
    }

    /**
     * Cancels a car order by its ID.
     *
     * @param id             ID of the car order to cancel
     * @param authentication Authentication object representing the current user
     * @return Redirect URL after order cancellation
     */
    @PostMapping("/{id}/cancel")
    public String cancelCarOrderById(@PathVariable Long id, Authentication authentication) {
        User currentUser = UserExtractor.getUser(authentication);
        orderService.cancelCarOrderById(id, currentUser);
        return "redirect:/car-order/my-orders";
    }

    /**
     * Retrieves the PDF representation of a car order by its ID.
     *
     * @param id             ID of the car order
     * @param authentication Authentication object representing the current user
     * @return ResponseEntity with car order PDF bytes if found, or 404 if not found
     */
    @GetMapping("/{id}/pdf")
    @ResponseBody
    public ResponseEntity<ByteArrayResource> getCarOrderPdfById(@PathVariable Long id,
                                                                Authentication authentication) {
        User currentUser = UserExtractor.getUser(authentication);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.setContentDispositionFormData("filename", "order.pdf");

        ByteArrayResource resource = orderService.generateCarOrderPdfById(id, currentUser);
        return ResponseEntity.ok()
                .headers(headers)
                .contentLength(resource.contentLength())
                .body(resource);
    }

    /**
     * Retrieves the text representation of a car order by its ID.
     *
     * @param id             ID of the car order
     * @param authentication Authentication object representing the current user
     * @return ResponseEntity with car order text bytes if found, or 404 if not found
     */
    @GetMapping("/{id}/txt")
    @ResponseBody
    public ResponseEntity<ByteArrayResource> getCarOrderTxtById(@PathVariable Long id,
                                                                Authentication authentication) {
        User currentUser = UserExtractor.getUser(authentication);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.TEXT_PLAIN);
        headers.setContentDispositionFormData("filename", "order.txt");

        ByteArrayResource resource = orderService.generateCarOrderTxtById(id, currentUser);
        return ResponseEntity.ok()
                .headers(headers)
                .contentLength(resource.contentLength())
                .body(resource);
    }

    /**
     * Displays the page listing all active orders of the current user.
     *
     * @param model          Model object for adding attributes
     * @param authentication Authentication object representing the current user
     * @return View name for displaying the user's active orders
     */
    @GetMapping("/my-orders")
    public String getUserCarOrders(Model model, Authentication authentication) {
        User user = UserExtractor.getUser(authentication);
        List<CarOrder> incomingOrders = orderService.getActiveCarOrdersBySeller(user);
        model.addAttribute("incomingOrders", incomingOrders);
        List<CarOrder> outgoingOrders = orderService.getActiveCarOrdersByBuyer(user);
        model.addAttribute("outgoingOrders", outgoingOrders);
        return "car-order/my-orders";
    }

    /**
     * Displays the page listing all finished orders of the current user.
     *
     * @param model          Model object for adding attributes
     * @param authentication Authentication object representing the current user
     * @return View name for displaying the user's finished orders
     */
    @GetMapping("/my-orders/finished")
    public String getUserFinishedCarOrders(Model model, Authentication authentication) {
        User user = UserExtractor.getUser(authentication);
        List<CarOrder> incomingOrders = orderService.getNonActiveCarOrdersBySeller(user);
        model.addAttribute("incomingOrders", incomingOrders);
        List<CarOrder> outgoingOrders = orderService.getNonActiveCarOrdersByBuyer(user);
        model.addAttribute("outgoingOrders", outgoingOrders);
        return "car-order/finished-orders";
    }


}