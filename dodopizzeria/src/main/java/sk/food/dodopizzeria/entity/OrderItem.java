package sk.food.dodopizzeria.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "order_items")
public class OrderItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "Objednávka je povinná")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", nullable = false)
    private Order order;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "size_id")
    private PizzaSize size;

    @NotBlank(message = "Názov pizze je povinný")
    @Size(max = 120, message = "Názov pizze nesmie presiahnuť 120 znakov")
    @Column(name = "pizza_name_snapshot", nullable = false, length = 120)
    private String pizzaNameSnapshot;

    @NotNull(message = "Kód veľkosti je povinný")
    @Enumerated(EnumType.STRING)
    @Column(name = "size_snapshot", nullable = false)
    private PizzaSize.SizeCode sizeSnapshot;

    @NotNull(message = "Priemer je povinný")
    @Min(value = 1, message = "Priemer musí byť aspoň 1 cm")
    @Column(name = "diameter_snapshot_cm", nullable = false)
    private Integer diameterSnapshotCm;

    @NotNull(message = "Jednotková cena je povinná")
    @DecimalMin(value = "0.01", message = "Jednotková cena musí byť väčšia ako 0")
    @Column(name = "unit_price_snapshot_eur", nullable = false, precision = 10, scale = 2)
    private BigDecimal unitPriceSnapshotEur;

    @NotNull(message = "Množstvo je povinné")
    @Min(value = 1, message = "Množstvo musí byť aspoň 1")
    @Column(nullable = false)
    private Integer quantity;

    @NotNull(message = "Celková suma riadku je povinná")
    @DecimalMin(value = "0.0", message = "Celková suma riadku musí byť aspoň 0")
    @Column(name = "line_total_eur", nullable = false, precision = 10, scale = 2)
    private BigDecimal lineTotalEur;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @OneToMany(mappedBy = "orderItem", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderItemExtra> extras = new ArrayList<>();

    public OrderItem() {
    }

    public OrderItem(Long id, Order order, PizzaSize size, String pizzaNameSnapshot,
                     PizzaSize.SizeCode sizeSnapshot, Integer diameterSnapshotCm,
                     BigDecimal unitPriceSnapshotEur, Integer quantity, BigDecimal lineTotalEur,
                     LocalDateTime createdAt, List<OrderItemExtra> extras) {
        this.id = id;
        this.order = order;
        this.size = size;
        this.pizzaNameSnapshot = pizzaNameSnapshot;
        this.sizeSnapshot = sizeSnapshot;
        this.diameterSnapshotCm = diameterSnapshotCm;
        this.unitPriceSnapshotEur = unitPriceSnapshotEur;
        this.quantity = quantity;
        this.lineTotalEur = lineTotalEur;
        this.createdAt = createdAt;
        this.extras = extras;
    }

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

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public PizzaSize getSize() {
        return size;
    }

    public void setSize(PizzaSize size) {
        this.size = size;
    }

    public String getPizzaNameSnapshot() {
        return pizzaNameSnapshot;
    }

    public void setPizzaNameSnapshot(String pizzaNameSnapshot) {
        this.pizzaNameSnapshot = pizzaNameSnapshot;
    }

    public PizzaSize.SizeCode getSizeSnapshot() {
        return sizeSnapshot;
    }

    public void setSizeSnapshot(PizzaSize.SizeCode sizeSnapshot) {
        this.sizeSnapshot = sizeSnapshot;
    }

    public Integer getDiameterSnapshotCm() {
        return diameterSnapshotCm;
    }

    public void setDiameterSnapshotCm(Integer diameterSnapshotCm) {
        this.diameterSnapshotCm = diameterSnapshotCm;
    }

    public BigDecimal getUnitPriceSnapshotEur() {
        return unitPriceSnapshotEur;
    }

    public void setUnitPriceSnapshotEur(BigDecimal unitPriceSnapshotEur) {
        this.unitPriceSnapshotEur = unitPriceSnapshotEur;
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

    public List<OrderItemExtra> getExtras() {
        return extras;
    }

    public void setExtras(List<OrderItemExtra> extras) {
        this.extras = extras;
    }

    @Override
    public String toString() {
        return "OrderItem{" +
                "id=" + id +
                ", pizzaNameSnapshot='" + pizzaNameSnapshot + '\'' +
                ", sizeSnapshot=" + sizeSnapshot +
                ", quantity=" + quantity +
                ", lineTotalEur=" + lineTotalEur +
                '}';
    }
}
