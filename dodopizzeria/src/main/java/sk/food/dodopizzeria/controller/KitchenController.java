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
@RequestMapping("/kitchen")
public class KitchenController {

    private final OrderService orderService;

    @Autowired
    public KitchenController(OrderService orderService) {
        this.orderService = orderService;
    }

    @GetMapping
    public String kitchenPanel(Model model) {
        // Show pending and preparing orders
        List<Order> pendingOrders = orderService.findByStatus(OrderStatus.PENDING);
        List<Order> preparingOrders = orderService.findByStatus(OrderStatus.PREPARING);
        List<Order> readyOrders = orderService.findByStatus(OrderStatus.READY);

        model.addAttribute("pendingOrders", pendingOrders);
        model.addAttribute("preparingOrders", preparingOrders);
        model.addAttribute("readyOrders", readyOrders);

        model.addAttribute("pendingCount", orderService.countByStatus(OrderStatus.PENDING));
        model.addAttribute("preparingCount", orderService.countByStatus(OrderStatus.PREPARING));
        model.addAttribute("readyCount", orderService.countByStatus(OrderStatus.READY));

        return "kitchen/orders";
    }

    @GetMapping("/order/{id}")
    public String orderDetail(@PathVariable Long id, Model model) {
        Order order = orderService.findById(id);
        model.addAttribute("order", order);

        // Available status transitions for kitchen
        List<OrderStatus> availableStatuses = switch (order.getStatus()) {
            case PENDING -> Arrays.asList(OrderStatus.PREPARING, OrderStatus.CANCELLED);
            case PREPARING -> Arrays.asList(OrderStatus.READY, OrderStatus.CANCELLED);
            default -> List.of();
        };
        model.addAttribute("availableStatuses", availableStatuses);

        return "kitchen/order-detail";
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

        return "redirect:/kitchen";
    }
}

