package sk.food.dodopizzeria.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import sk.food.dodopizzeria.entity.Order;
import sk.food.dodopizzeria.entity.OrderStatus;

import java.util.List;
import java.util.Optional;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

    // Find order by order number
    Optional<Order> findByOrderNumber(String orderNumber);

    // Get user's orders with pagination (newest first)
    Page<Order> findByUserIdOrderByCreatedAtDesc(Long userId, Pageable pageable);

    // Get orders by status (for kitchen/delivery panels)
    List<Order> findByStatusOrderByCreatedAtAsc(OrderStatus status);

    // Get orders by multiple statuses
    List<Order> findByStatusInOrderByCreatedAtAsc(List<OrderStatus> statuses);

    // Count orders by status
    long countByStatus(OrderStatus status);

    // Search orders by order number or user name
    @Query("SELECT o FROM Order o JOIN o.user u WHERE " +
            "o.orderNumber LIKE CONCAT('%', :search, '%') OR " +
            "LOWER(u.firstName) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
            "LOWER(u.lastName) LIKE LOWER(CONCAT('%', :search, '%'))")
    Page<Order> searchOrders(@Param("search") String search, Pageable pageable);

    // Get recent orders for admin dashboard
    @Query("SELECT o FROM Order o ORDER BY o.createdAt DESC")
    Page<Order> findRecentOrders(Pageable pageable);

    // Check if user has any orders
    boolean existsByUserId(Long userId);
}

