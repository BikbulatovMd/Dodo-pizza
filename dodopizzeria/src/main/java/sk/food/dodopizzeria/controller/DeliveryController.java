package sk.food.dodopizzeria.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import sk.food.dodopizzeria.entity.Order;
import sk.food.dodopizzeria.entity.OrderStatus;
import sk.food.dodopizzeria.service.OrderService;

import java.util.Arrays;
import java.util.List;

@Controller
@RequestMapping("/delivery")
public class DeliveryController {

    private final OrderService orderService;

    @Autowired
    public DeliveryController(OrderService orderService) {
        this.orderService = orderService;
    }

    @GetMapping
    public String deliveryPanel(Model model) {
        // Show ready and delivering orders
        List<Order> readyOrders = orderService.findByStatus(OrderStatus.READY);
        List<Order> deliveringOrders = orderService.findByStatus(OrderStatus.DELIVERING);

        model.addAttribute("readyOrders", readyOrders);
        model.addAttribute("deliveringOrders", deliveringOrders);

        model.addAttribute("readyCount", orderService.countByStatus(OrderStatus.READY));
        model.addAttribute("deliveringCount", orderService.countByStatus(OrderStatus.DELIVERING));

        return "delivery/orders";
    }

    @GetMapping("/order/{id}")
    public String orderDetail(@PathVariable Long id, Model model) {
        Order order = orderService.findById(id);
        model.addAttribute("order", order);

        // Available status transitions for delivery
        List<OrderStatus> availableStatuses = switch (order.getStatus()) {
            case READY -> List.of(OrderStatus.DELIVERING);
            case DELIVERING -> List.of(OrderStatus.DELIVERED);
            default -> List.of();
        };
        model.addAttribute("availableStatuses", availableStatuses);

        return "delivery/order-detail";
    }

    @PostMapping("/order/{id}/status")
    public String updateStatus(
            @PathVariable Long id,
            @RequestParam OrderStatus status,
            RedirectAttributes redirectAttributes) {

        try {
            orderService.updateStatus(id, status);
            redirectAttributes.addFlashAttribute("success", "Stav objednávky bol aktualizovaný");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }

        return "redirect:/delivery";
    }
}

