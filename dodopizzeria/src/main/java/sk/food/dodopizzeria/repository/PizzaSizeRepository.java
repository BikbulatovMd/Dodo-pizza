package sk.food.dodopizzeria.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import sk.food.dodopizzeria.entity.PizzaSize;

import java.util.List;

@Repository
public interface PizzaSizeRepository extends JpaRepository<PizzaSize, Long> {

    List<PizzaSize> findByPizzaIdAndAvailableTrue(Long pizzaId);

    List<PizzaSize> findByPizzaId(Long pizzaId);
}

