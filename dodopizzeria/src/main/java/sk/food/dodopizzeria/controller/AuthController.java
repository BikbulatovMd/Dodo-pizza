package sk.food.dodopizzeria.controller;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import sk.food.dodopizzeria.dto.UserRegistrationDto;
import sk.food.dodopizzeria.exception.EmailAlreadyExistsException;
import sk.food.dodopizzeria.service.UserService;

@Controller
public class AuthController {

    private final UserService userService;

    @Autowired
    public AuthController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/login")
    public String loginPage() {
        return "auth/login";
    }

    @GetMapping("/register")
    public String registerPage(Model model) {
        model.addAttribute("user", new UserRegistrationDto());
        return "auth/register";
    }

    @PostMapping("/register")
    public String register(
            @Valid @ModelAttribute("user") UserRegistrationDto dto,
            BindingResult result,
            RedirectAttributes redirectAttributes) {

        // Check password confirmation
        if (!dto.getPassword().equals(dto.getConfirmPassword())) {
            result.rejectValue("confirmPassword", "error.user", "Heslá sa nezhodujú");
        }

        if (result.hasErrors()) {
            return "auth/register";
        }

        try {
            userService.registerUser(dto);
            redirectAttributes.addFlashAttribute("success", "Registrácia úspešná! Môžete sa prihlásiť.");
            return "redirect:/login";
        } catch (EmailAlreadyExistsException e) {
            result.rejectValue("email", "error.user", e.getMessage());
            return "auth/register";
        }
    }
}

