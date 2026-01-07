package sk.food.dodopizzeria.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;
import sk.food.dodopizzeria.config.CustomUserDetails;
import sk.food.dodopizzeria.service.CartService;

@ControllerAdvice
@RequiredArgsConstructor
public class GlobalControllerAdvice {

    private final CartService cartService;

    @ModelAttribute("cartItemCount")
    public int cartItemCount() {
        return cartService.getItemCount();
    }

    @ModelAttribute("currentUser")
    public CustomUserDetails currentUser(@AuthenticationPrincipal CustomUserDetails userDetails) {
        return userDetails;
    }
}

