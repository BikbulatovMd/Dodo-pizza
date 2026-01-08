package sk.food.dodopizzeria.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sk.food.dodopizzeria.dto.CartItemDto;
import sk.food.dodopizzeria.dto.CheckoutDto;
import sk.food.dodopizzeria.entity.*;
import sk.food.dodopizzeria.exception.ResourceNotFoundException;
import sk.food.dodopizzeria.repository.IngredientRepository;
import sk.food.dodopizzeria.repository.OrderRepository;
import sk.food.dodopizzeria.repository.PizzaSizeRepository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final PizzaSizeRepository pizzaSizeRepository;
    private final IngredientRepository ingredientRepository;
    private final CartService cartService;

    @Autowired
    public OrderService(OrderRepository orderRepository, PizzaSizeRepository pizzaSizeRepository,
                        IngredientRepository ingredientRepository, CartService cartService) {
        this.orderRepository = orderRepository;
        this.pizzaSizeRepository = pizzaSizeRepository;
        this.ingredientRepository = ingredientRepository;
        this.cartService = cartService;
    }

    @Transactional
    public Order createOrder(User user, CheckoutDto checkoutDto) {
        if (cartService.isEmpty()) {
            throw new IllegalStateException("Košík je prázdny");
        }

        Order order = new Order();
        order.setUser(user);
        order.setOrderNumber(generateOrderNumber());
        order.setStatus(OrderStatus.PENDING);
        order.setDeliveryAddress(checkoutDto.getDeliveryAddress());
        order.setCustomerNote(checkoutDto.getCustomerNote());

        // Convert cart items to order items
        for (CartItemDto cartItem : cartService.getItems()) {
            OrderItem orderItem = new OrderItem();

            PizzaSize size = pizzaSizeRepository.findById(cartItem.getSizeId())
                    .orElse(null);
            orderItem.setSize(size);
            orderItem.setPizzaNameSnapshot(cartItem.getPizzaName());
            orderItem.setSizeSnapshot(cartItem.getSizeCode());
            orderItem.setDiameterSnapshotCm(cartItem.getDiameterCm());
            orderItem.setUnitPriceSnapshotEur(cartItem.getBasePrice());
            orderItem.setQuantity(cartItem.getQuantity());

            // Add extras
            for (CartItemDto.CartExtraDto extraDto : cartItem.getExtras()) {
                OrderItemExtra extra = new OrderItemExtra();
                Ingredient ingredient = ingredientRepository.findById(extraDto.getIngredientId())
                        .orElse(null);
                extra.setIngredient(ingredient);
                extra.setIngredientNameSnapshot(extraDto.getIngredientName());
                extra.setExtraPriceSnapshotEur(extraDto.getPrice());
                extra.setQuantity(extraDto.getQuantity());
                extra.calculateLineTotal();
                orderItem.addExtra(extra);
            }

            orderItem.calculateLineTotal();
            order.addItem(orderItem);
        }

        order.recalculateTotal();

        Order savedOrder = orderRepository.save(order);

        // Clear the cart after successful order
        cartService.clear();

        return savedOrder;
    }

    public Order findById(Long id) {
        return orderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Objednávka", id));
    }

    public Order findByOrderNumber(String orderNumber) {
        return orderRepository.findByOrderNumber(orderNumber)
                .orElseThrow(() -> new ResourceNotFoundException("Objednávka", "číslo", orderNumber));
    }

    public Page<Order> findUserOrders(Long userId, Pageable pageable) {
        return orderRepository.findByUserIdOrderByCreatedAtDesc(userId, pageable);
    }

    public List<Order> findByStatus(OrderStatus status) {
        return orderRepository.findByStatusOrderByCreatedAtAsc(status);
    }

    public List<Order> findByStatuses(List<OrderStatus> statuses) {
        return orderRepository.findByStatusInOrderByCreatedAtAsc(statuses);
    }

    public Page<Order> searchOrders(String search, Pageable pageable) {
        if (search == null || search.isBlank()) {
            return orderRepository.findRecentOrders(pageable);
        }
        return orderRepository.searchOrders(search, pageable);
    }

    @Transactional
    public Order updateStatus(Long orderId, OrderStatus newStatus) {
        Order order = findById(orderId);
        validateStatusTransition(order.getStatus(), newStatus);
        order.setStatus(newStatus);
        return orderRepository.save(order);
    }

    private void validateStatusTransition(OrderStatus current, OrderStatus next) {
        // Define valid transitions
        boolean valid = switch (current) {
            case PENDING -> next == OrderStatus.PREPARING || next == OrderStatus.CANCELLED;
            case PREPARING -> next == OrderStatus.READY || next == OrderStatus.CANCELLED;
            case READY -> next == OrderStatus.DELIVERING;
            case DELIVERING -> next == OrderStatus.DELIVERED;
            case DELIVERED, CANCELLED -> false;
        };

        if (!valid) {
            throw new IllegalStateException("Neplatný prechod stavu z " + current.getDisplayNameSk() +
                    " na " + next.getDisplayNameSk());
        }
    }

    public long countByStatus(OrderStatus status) {
        return orderRepository.countByStatus(status);
    }

    private String generateOrderNumber() {
        String datePart = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        int randomPart = ThreadLocalRandom.current().nextInt(1000, 9999);
        return "DP-" + datePart + "-" + randomPart;
    }
}

