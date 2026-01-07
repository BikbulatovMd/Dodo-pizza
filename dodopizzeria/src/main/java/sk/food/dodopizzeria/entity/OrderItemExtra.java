package sk.food.dodopizzeria.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "order_item_extras")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderItemExtra {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_item_id", nullable = false)
    private OrderItem orderItem;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ingredient_id")
    private Ingredient ingredient;

    @Column(name = "ingredient_name_snapshot", nullable = false, length = 120)
    private String ingredientNameSnapshot;

    @Column(name = "extra_price_snapshot_eur", nullable = false, precision = 10, scale = 2)
    private BigDecimal extraPriceSnapshotEur;

    @Column(nullable = false)
    private Integer quantity = 1;

    @Column(name = "line_total_eur", nullable = false, precision = 10, scale = 2)
    private BigDecimal lineTotalEur;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        calculateLineTotal();
    }

    public void calculateLineTotal() {
        this.lineTotalEur = extraPriceSnapshotEur.multiply(BigDecimal.valueOf(quantity));
    }
}

