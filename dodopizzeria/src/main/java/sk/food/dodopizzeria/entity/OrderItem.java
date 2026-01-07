package sk.food.dodopizzeria.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "order_items")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", nullable = false)
    private Order order;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "size_id")
    private PizzaSize size;

    @Column(name = "pizza_name_snapshot", nullable = false, length = 120)
    private String pizzaNameSnapshot;

    @Enumerated(EnumType.STRING)
    @Column(name = "size_snapshot", nullable = false)
    private PizzaSize.SizeCode sizeSnapshot;

    @Column(name = "diameter_snapshot_cm", nullable = false)
    private Integer diameterSnapshotCm;

    @Column(name = "unit_price_snapshot_eur", nullable = false, precision = 10, scale = 2)
    private BigDecimal unitPriceSnapshotEur;

    @Column(nullable = false)
    private Integer quantity;

    @Column(name = "line_total_eur", nullable = false, precision = 10, scale = 2)
    private BigDecimal lineTotalEur;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @OneToMany(mappedBy = "orderItem", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderItemExtra> extras = new ArrayList<>();

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }

    public void addExtra(OrderItemExtra extra) {
        extras.add(extra);
        extra.setOrderItem(this);
    }

    public void calculateLineTotal() {
        BigDecimal baseTotal = unitPriceSnapshotEur.multiply(BigDecimal.valueOf(quantity));
        BigDecimal extrasTotal = extras.stream()
                .map(OrderItemExtra::getLineTotalEur)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        this.lineTotalEur = baseTotal.add(extrasTotal);
    }
}

