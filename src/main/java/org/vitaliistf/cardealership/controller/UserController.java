package org.vitaliistf.cardealership.controller;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.vitaliistf.cardealership.data.User;
import org.vitaliistf.cardealership.exception.ForbiddenException;
import org.vitaliistf.cardealership.service.UserService;
import org.vitaliistf.cardealership.util.UserExtractor;

/**
 * Controller for handling user-related operations.
 */
@Controller
@AllArgsConstructor
public class UserController {

    private final UserService userService;

    /**
     * Displays the login page.
     *
     * @return View name for the login page
     */
    @GetMapping("/login")
    public String loginPage() {
        return "user/login";
    }

    /**
     * Displays the user page.
     *
     * @param model          Model object for adding attributes
     * @param authentication Authentication object representing the current user
     * @return View name for the user page
     */
    @GetMapping("/user/user-page")
    public String userPage(Model model, Authentication authentication) {
        User user = UserExtractor.getUser(authentication);
        model.addAttribute("user", user);
        return "user/user-page";
    }

    /**
     * Displays the page for editing user information.
     *
     * @param id             ID of the user to edit
     * @param model          Model object for adding attributes
     * @param authentication Authentication object representing the current user
     * @return View name for the edit user page
     * @throws ForbiddenException if the current user is not authorized to edit the specified user
     */
    @GetMapping("/user/{id}/edit")
    public String editUserPage(@PathVariable Long id,
                               Model model,
                               Authentication authentication) {
        User user = UserExtractor.getUser(authentication);
        if (user.getId().equals(id)) {
            model.addAttribute("user", user);
            return "user/edit-user";
        } else {
            throw new ForbiddenException("You have no permission for this operation.");
        }
    }

    /**
     * Handles the submission of the user edit form.
     *
     * @param id              ID of the user being edited
     * @param user            Edited user object
     * @param bindingResult   Binding result for validation errors
     * @param authentication Authentication object representing the current user
     * @return Redirect URL after user edit
     * @throws ForbiddenException if the current user is not authorized to edit the specified user
     */
    @PostMapping("/user/{id}/edit")
    public String editUser(@PathVariable Long id,
                           @ModelAttribute("user") @Valid User user,
                           BindingResult bindingResult,
                           Authentication authentication) {
        if (bindingResult.hasErrors()) {
            return "user/edit-user";
        }
        User authenticatedUser = UserExtractor.getUser(authentication);
        if (authenticatedUser.getId().equals(id)) {
            userService.saveUser(user);
            return "redirect:/user/user-page";
        } else {
            throw new ForbiddenException("You have no permission for this operation.");
        }
    }

    /**
     * Displays the registration page.
     *
     * @param model Model object for adding attributes
     * @return View name for the registration page
     */
    @GetMapping("/register")
    public String registerPage(Model model) {
        model.addAttribute(new User());
        return "user/register";
    }

    /**
     * Handles the submission of the registration form.
     *
     * @param user           New user object to register
     * @param bindingResult  Binding result for validation errors
     * @return Redirect URL after successful registration
     */
    @PostMapping("/register")
    public String register(@ModelAttribute("user") @Valid User user,
                           BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "user/register";
        }
        userService.saveUser(user);
        return "redirect:/user/login";
    }
}
