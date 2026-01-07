package sk.food.dodopizzeria.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import sk.food.dodopizzeria.entity.Ingredient;

import java.util.List;
import java.util.Optional;

@Repository
public interface IngredientRepository extends JpaRepository<Ingredient, Long> {

    Optional<Ingredient> findByNameSk(String nameSk);

    // Get ingredients available as extras (with price > 0)
    @Query("SELECT i FROM Ingredient i WHERE i.extraPriceEur > 0 ORDER BY i.nameSk")
    List<Ingredient> findAvailableExtras();

    // Search ingredients by name
    Page<Ingredient> findByNameSkContainingIgnoreCase(String nameSk, Pageable pageable);

    // Find vegan ingredients
    List<Ingredient> findByVeganTrue();

    // Find spicy ingredients
    List<Ingredient> findBySpicyTrue();

    // Get ingredients not in a specific pizza
    @Query("SELECT i FROM Ingredient i WHERE i NOT IN " +
            "(SELECT pi FROM Pizza p JOIN p.ingredients pi WHERE p.id = :pizzaId)")
    List<Ingredient> findIngredientsNotInPizza(@Param("pizzaId") Long pizzaId);
}

