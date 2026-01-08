package sk.food.dodopizzeria.controller;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import sk.food.dodopizzeria.config.CustomUserDetails;
import sk.food.dodopizzeria.dto.PasswordChangeDto;
import sk.food.dodopizzeria.dto.UserProfileDto;
import sk.food.dodopizzeria.entity.User;
import sk.food.dodopizzeria.exception.InvalidPasswordException;
import sk.food.dodopizzeria.service.FileStorageService;
import sk.food.dodopizzeria.service.UserService;

@Controller
@RequestMapping("/profile")
public class ProfileController {

    private final UserService userService;
    private final FileStorageService fileStorageService;

    @Autowired
    public ProfileController(UserService userService, FileStorageService fileStorageService) {
        this.userService = userService;
        this.fileStorageService = fileStorageService;
    }

    @GetMapping
    public String viewProfile(@AuthenticationPrincipal CustomUserDetails userDetails, Model model) {
        User user = userService.findById(userDetails.getId());
        model.addAttribute("user", user);
        return "profile/view";
    }

    @GetMapping("/edit")
    public String editProfileForm(@AuthenticationPrincipal CustomUserDetails userDetails, Model model) {
        User user = userService.findById(userDetails.getId());
        model.addAttribute("profile", userService.toProfileDto(user));
        return "profile/edit";
    }

    @PostMapping("/edit")
    public String updateProfile(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @Valid @ModelAttribute("profile") UserProfileDto dto,
            BindingResult result,
            RedirectAttributes redirectAttributes) {

        if (result.hasErrors()) {
            return "profile/edit";
        }

        try {
            userService.updateProfile(userDetails.getId(), dto);
            redirectAttributes.addFlashAttribute("success", "Profil bol úspešne aktualizovaný");
            return "redirect:/profile";
        } catch (Exception e) {
            result.rejectValue("email", "error.profile", e.getMessage());
            return "profile/edit";
        }
    }

    @PostMapping("/image")
    public String uploadProfileImage(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @RequestParam("image") MultipartFile file,
            RedirectAttributes redirectAttributes) {

        try {
            String imageUrl = fileStorageService.storeFile(file, "profiles");
            userService.updateProfileImage(userDetails.getId(), imageUrl);
            redirectAttributes.addFlashAttribute("success", "Profilový obrázok bol aktualizovaný");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }

        return "redirect:/profile";
    }

    @GetMapping("/password")
    public String changePasswordForm(Model model) {
        model.addAttribute("passwordChange", new PasswordChangeDto());
        return "profile/password";
    }

    @PostMapping("/password")
    public String changePassword(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @Valid @ModelAttribute("passwordChange") PasswordChangeDto dto,
            BindingResult result,
            RedirectAttributes redirectAttributes) {

        // Check password confirmation
        if (!dto.getNewPassword().equals(dto.getConfirmPassword())) {
            result.rejectValue("confirmPassword", "error.password", "Heslá sa nezhodujú");
        }

        if (result.hasErrors()) {
            return "profile/password";
        }

        try {
            userService.changePassword(userDetails.getId(), dto);
            redirectAttributes.addFlashAttribute("success", "Heslo bolo úspešne zmenené");
            return "redirect:/profile";
        } catch (InvalidPasswordException e) {
            result.rejectValue("currentPassword", "error.password", e.getMessage());
            return "profile/password";
        }
    }
}

