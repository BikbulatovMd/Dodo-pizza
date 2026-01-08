package sk.food.dodopizzeria.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import sk.food.dodopizzeria.service.CartService;
import sk.food.dodopizzeria.service.IngredientService;

import java.util.List;

@Controller
@RequestMapping("/cart")
public class CartController {

    private final CartService cartService;
    private final IngredientService ingredientService;

    @Autowired
    public CartController(CartService cartService, IngredientService ingredientService) {
        this.cartService = cartService;
        this.ingredientService = ingredientService;
    }

    @GetMapping
    public String viewCart(Model model) {
        model.addAttribute("cartItems", cartService.getItems());
        model.addAttribute("totalPrice", cartService.getTotalPrice());
        return "cart/index";
    }

    @PostMapping("/add")
    public String addToCart(
            @RequestParam Long pizzaId,
            @RequestParam Long sizeId,
            @RequestParam(defaultValue = "1") Integer quantity,
            @RequestParam(required = false) List<Long> extras,
            RedirectAttributes redirectAttributes) {

        try {
            cartService.addItem(pizzaId, sizeId, quantity, extras);
            redirectAttributes.addFlashAttribute("success", "Pizza bola pridaná do košíka");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }

        return "redirect:/cart";
    }

    @PostMapping("/update")
    public String updateQuantity(
            @RequestParam String itemId,
            @RequestParam Integer quantity,
            RedirectAttributes redirectAttributes) {

        cartService.updateItemQuantity(itemId, quantity);
        redirectAttributes.addFlashAttribute("success", "Košík bol aktualizovaný");
        return "redirect:/cart";
    }

    @PostMapping("/remove")
    public String removeItem(
            @RequestParam String itemId,
            RedirectAttributes redirectAttributes) {

        cartService.removeItem(itemId);
        redirectAttributes.addFlashAttribute("success", "Položka bola odstránená z košíka");
        return "redirect:/cart";
    }

    @PostMapping("/clear")
    public String clearCart(RedirectAttributes redirectAttributes) {
        cartService.clear();
        redirectAttributes.addFlashAttribute("success", "Košík bol vyprázdnený");
        return "redirect:/cart";
    }

    // Expose cart count for header
    @ModelAttribute("cartItemCount")
    public int cartItemCount() {
        return cartService.getItemCount();
    }
}

