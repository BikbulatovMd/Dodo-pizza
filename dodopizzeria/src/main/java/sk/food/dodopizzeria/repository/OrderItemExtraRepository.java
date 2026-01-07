package sk.food.dodopizzeria.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import sk.food.dodopizzeria.entity.OrderItemExtra;

import java.util.List;

@Repository
public interface OrderItemExtraRepository extends JpaRepository<OrderItemExtra, Long> {

    List<OrderItemExtra> findByOrderItemId(Long orderItemId);
}

