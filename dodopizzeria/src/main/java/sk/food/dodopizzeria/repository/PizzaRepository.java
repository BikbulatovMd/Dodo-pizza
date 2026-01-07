package sk.food.dodopizzeria.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import sk.food.dodopizzeria.entity.Pizza;

import java.util.List;
import java.util.Optional;

@Repository
public interface PizzaRepository extends JpaRepository<Pizza, Long> {

    // Find pizza by slug for detail page
    Optional<Pizza> findBySlug(String slug);

    // Search bar - search by name (case insensitive)
    List<Pizza> findByNameSkContainingIgnoreCaseAndActiveTrue(String nameSk);

    // Search with pagination
    Page<Pizza> findByNameSkContainingIgnoreCaseAndActiveTrue(String nameSk, Pageable pageable);

    // Find all active pizzas
    Page<Pizza> findByActiveTrue(Pageable pageable);

    List<Pizza> findByActiveTrue();

    // Filter by tag slug
    @Query("SELECT DISTINCT p FROM Pizza p JOIN p.tags t WHERE t.slug = :tagSlug AND p.active = true")
    Page<Pizza> findByTagSlug(@Param("tagSlug") String tagSlug, Pageable pageable);

    // Filter by ingredient (vegan pizzas, etc.)
    @Query("SELECT DISTINCT p FROM Pizza p JOIN p.ingredients i WHERE i.vegan = true AND p.active = true")
    Page<Pizza> findVeganPizzas(Pageable pageable);

    // Combined search and tag filter
    @Query("SELECT DISTINCT p FROM Pizza p LEFT JOIN p.tags t WHERE p.active = true " +
            "AND (:search IS NULL OR LOWER(p.nameSk) LIKE LOWER(CONCAT('%', :search, '%'))) " +
            "AND (:tagSlug IS NULL OR t.slug = :tagSlug)")
    Page<Pizza> searchWithFilters(@Param("search") String search,
                                   @Param("tagSlug") String tagSlug,
                                   Pageable pageable);

    // Check if slug exists
    boolean existsBySlug(String slug);
}

