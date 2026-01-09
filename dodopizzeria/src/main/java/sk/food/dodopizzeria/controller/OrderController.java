package sk.food.dodopizzeria.controller;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import sk.food.dodopizzeria.config.CustomUserDetails;
import sk.food.dodopizzeria.dto.CheckoutDto;
import sk.food.dodopizzeria.entity.Order;
import sk.food.dodopizzeria.entity.User;
import sk.food.dodopizzeria.service.CartService;
import sk.food.dodopizzeria.service.OrderService;
import sk.food.dodopizzeria.service.UserService;

@Controller
public class OrderController {

    private final OrderService orderService;
    private final CartService cartService;
    private final UserService userService;

    @Autowired
    public OrderController(OrderService orderService, CartService cartService,
                           UserService userService) {
        this.orderService = orderService;
        this.cartService = cartService;
        this.userService = userService;
    }

    @GetMapping("/checkout")
    public String checkoutPage(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            Model model) {

        if (cartService.isEmpty()) {
            return "redirect:/cart";
        }

        User user = userService.findById(userDetails.getId());

        CheckoutDto checkout = new CheckoutDto();
        checkout.setDeliveryAddress(user.getDeliveryAddress());

        model.addAttribute("checkout", checkout);
        model.addAttribute("cartItems", cartService.getItems());
        model.addAttribute("totalPrice", cartService.getTotalPrice());
        return "orders/checkout";
    }

    @PostMapping("/checkout")
    public String placeOrder(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @Valid @ModelAttribute("checkout") CheckoutDto checkoutDto,
            BindingResult result,
            Model model,
            RedirectAttributes redirectAttributes) {

        if (result.hasErrors()) {
            model.addAttribute("cartItems", cartService.getItems());
            model.addAttribute("totalPrice", cartService.getTotalPrice());
            return "orders/checkout";
        }

        try {
            User user = userService.findById(userDetails.getId());
            Order order = orderService.createOrder(user, checkoutDto);
            redirectAttributes.addFlashAttribute("success",
                    "Objednávka " + order.getOrderNumber() + " bola úspešne vytvorená!");
            return "redirect:/orders/" + order.getId();
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            model.addAttribute("cartItems", cartService.getItems());
            model.addAttribute("totalPrice", cartService.getTotalPrice());
            return "orders/checkout";
        }
    }

    @GetMapping("/orders")
    public String listOrders(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            Model model) {

        Page<Order> orders = orderService.findUserOrders(
                userDetails.getId(),
                PageRequest.of(page, size));

        model.addAttribute("orders", orders);
        return "orders/list";
    }

    @GetMapping("/orders/{id}")
    public String orderDetail(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @PathVariable Long id,
            Model model) {

        Order order = orderService.findById(id);

        if (!order.getUser().getId().equals(userDetails.getId())) {
            return "redirect:/error/403";
        }

        model.addAttribute("order", order);
        return "orders/detail";
    }
    @PostMapping("/orders/{id}/cancel")
    public String cancelOrder(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @PathVariable Long id,
            RedirectAttributes redirectAttributes
    ) {
        try {
            orderService.cancelOrderByCustomer(id, userDetails.getId());
            redirectAttributes.addFlashAttribute("success", "Objednávka bola zrušená.");
        } catch (IllegalStateException ex) {
            redirectAttributes.addFlashAttribute("error", ex.getMessage());
        } catch (org.springframework.security.access.AccessDeniedException ex) {
            redirectAttributes.addFlashAttribute("error", "Access Denied");
        }
        return "redirect:/orders";
    }

}



