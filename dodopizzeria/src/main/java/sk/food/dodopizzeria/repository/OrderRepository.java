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

    Optional<Order> findByOrderNumber(String orderNumber);

    Page<Order> findByUserIdOrderByCreatedAtDesc(Long userId, Pageable pageable);

    List<Order> findByStatusOrderByCreatedAtAsc(OrderStatus status);

    List<Order> findByStatusInOrderByCreatedAtAsc(List<OrderStatus> statuses);

    long countByStatus(OrderStatus status);

    @Query("SELECT o FROM Order o JOIN o.user u WHERE " +
            "o.orderNumber LIKE CONCAT('%', :search, '%') OR " +
            "LOWER(u.firstName) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
            "LOWER(u.lastName) LIKE LOWER(CONCAT('%', :search, '%'))")
    Page<Order> searchOrders(@Param("search") String search, Pageable pageable);

    @Query("SELECT o FROM Order o ORDER BY o.createdAt DESC")
    Page<Order> findRecentOrders(Pageable pageable);

    boolean existsByUserId(Long userId);
}

