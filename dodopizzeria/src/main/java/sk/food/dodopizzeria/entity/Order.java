package sk.food.dodopizzeria.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "orders")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "Používateľ je povinný")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @NotBlank(message = "Číslo objednávky je povinné")
    @Size(max = 30, message = "Číslo objednávky nesmie presiahnuť 30 znakov")
    @Column(name = "order_number", nullable = false, unique = true, length = 30)
    private String orderNumber;

    @NotNull(message = "Stav objednávky je povinný")
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private OrderStatus status = OrderStatus.PENDING;

    @NotBlank(message = "Adresa doručenia je povinná")
    @Size(max = 255, message = "Adresa doručenia nesmie presiahnuť 255 znakov")
    @Column(name = "delivery_address", nullable = false, length = 255)
    private String deliveryAddress;

    @Size(max = 800, message = "Poznámka zákazníka nesmie presiahnuť 800 znakov")
    @Column(name = "customer_note", length = 800)
    private String customerNote;

    @NotNull(message = "Celková cena je povinná")
    @DecimalMin(value = "0.0", message = "Celková cena musí byť aspoň 0")
    @Column(name = "total_price_eur", nullable = false, precision = 10, scale = 2)
    private BigDecimal totalPriceEur = BigDecimal.ZERO;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderItem> items = new ArrayList<>();

    public Order() {
    }

    public Order(Long id, User user, String orderNumber, OrderStatus status, String deliveryAddress,
                 String customerNote, BigDecimal totalPriceEur, LocalDateTime createdAt,
                 LocalDateTime updatedAt, List<OrderItem> items) {
        this.id = id;
        this.user = user;
        this.orderNumber = orderNumber;
        this.status = status;
        this.deliveryAddress = deliveryAddress;
        this.customerNote = customerNote;
        this.totalPriceEur = totalPriceEur;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.items = items;
    }

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

    public void addItem(OrderItem item) {
        items.add(item);
        item.setOrder(this);
    }

    public void recalculateTotal() {
        this.totalPriceEur = items.stream()
                .map(OrderItem::getLineTotalEur)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(String orderNumber) {
        this.orderNumber = orderNumber;
    }

    public OrderStatus getStatus() {
        return status;
    }

    public void setStatus(OrderStatus status) {
        this.status = status;
    }

    public String getDeliveryAddress() {
        return deliveryAddress;
    }

    public void setDeliveryAddress(String deliveryAddress) {
        this.deliveryAddress = deliveryAddress;
    }

    public String getCustomerNote() {
        return customerNote;
    }

    public void setCustomerNote(String customerNote) {
        this.customerNote = customerNote;
    }

    public BigDecimal getTotalPriceEur() {
        return totalPriceEur;
    }

    public void setTotalPriceEur(BigDecimal totalPriceEur) {
        this.totalPriceEur = totalPriceEur;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public List<OrderItem> getItems() {
        return items;
    }

    public void setItems(List<OrderItem> items) {
        this.items = items;
    }

    @Override
    public String toString() {
        return "Order{" +
                "id=" + id +
                ", orderNumber='" + orderNumber + '\'' +
                ", status=" + status +
                ", totalPriceEur=" + totalPriceEur +
                '}';
    }
}
