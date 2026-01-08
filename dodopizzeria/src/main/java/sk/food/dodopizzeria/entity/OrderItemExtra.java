package sk.food.dodopizzeria.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "order_item_extras")
public class OrderItemExtra {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "Položka objednávky je povinná")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_item_id", nullable = false)
    private OrderItem orderItem;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ingredient_id")
    private Ingredient ingredient;

    @NotBlank(message = "Názov ingrediencie je povinný")
    @Size(max = 120, message = "Názov ingrediencie nesmie presiahnuť 120 znakov")
    @Column(name = "ingredient_name_snapshot", nullable = false, length = 120)
    private String ingredientNameSnapshot;

    @NotNull(message = "Príplatková cena je povinná")
    @DecimalMin(value = "0.0", message = "Príplatková cena musí byť aspoň 0")
    @Column(name = "extra_price_snapshot_eur", nullable = false, precision = 10, scale = 2)
    private BigDecimal extraPriceSnapshotEur;

    @NotNull(message = "Množstvo je povinné")
    @Min(value = 1, message = "Množstvo musí byť aspoň 1")
    @Column(nullable = false)
    private Integer quantity = 1;

    @NotNull(message = "Celková suma riadku je povinná")
    @DecimalMin(value = "0.0", message = "Celková suma riadku musí byť aspoň 0")
    @Column(name = "line_total_eur", nullable = false, precision = 10, scale = 2)
    private BigDecimal lineTotalEur;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    public OrderItemExtra() {
    }

    public OrderItemExtra(Long id, OrderItem orderItem, Ingredient ingredient,
                          String ingredientNameSnapshot, BigDecimal extraPriceSnapshotEur,
                          Integer quantity, BigDecimal lineTotalEur, LocalDateTime createdAt) {
        this.id = id;
        this.orderItem = orderItem;
        this.ingredient = ingredient;
        this.ingredientNameSnapshot = ingredientNameSnapshot;
        this.extraPriceSnapshotEur = extraPriceSnapshotEur;
        this.quantity = quantity;
        this.lineTotalEur = lineTotalEur;
        this.createdAt = createdAt;
    }

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        calculateLineTotal();
    }

    public void calculateLineTotal() {
        this.lineTotalEur = extraPriceSnapshotEur.multiply(BigDecimal.valueOf(quantity));
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public OrderItem getOrderItem() {
        return orderItem;
    }

    public void setOrderItem(OrderItem orderItem) {
        this.orderItem = orderItem;
    }

    public Ingredient getIngredient() {
        return ingredient;
    }

    public void setIngredient(Ingredient ingredient) {
        this.ingredient = ingredient;
    }

    public String getIngredientNameSnapshot() {
        return ingredientNameSnapshot;
    }

    public void setIngredientNameSnapshot(String ingredientNameSnapshot) {
        this.ingredientNameSnapshot = ingredientNameSnapshot;
    }

    public BigDecimal getExtraPriceSnapshotEur() {
        return extraPriceSnapshotEur;
    }

    public void setExtraPriceSnapshotEur(BigDecimal extraPriceSnapshotEur) {
        this.extraPriceSnapshotEur = extraPriceSnapshotEur;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public BigDecimal getLineTotalEur() {
        return lineTotalEur;
    }

    public void setLineTotalEur(BigDecimal lineTotalEur) {
        this.lineTotalEur = lineTotalEur;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    @Override
    public String toString() {
        return "OrderItemExtra{" +
                "id=" + id +
                ", ingredientNameSnapshot='" + ingredientNameSnapshot + '\'' +
                ", quantity=" + quantity +
                ", lineTotalEur=" + lineTotalEur +
                '}';
    }
}
