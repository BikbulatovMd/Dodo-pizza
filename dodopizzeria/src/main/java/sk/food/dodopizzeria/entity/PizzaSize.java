package sk.food.dodopizzeria.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "pizza_sizes")
public class PizzaSize {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "Pizza je povinná")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pizza_id", nullable = false)
    private Pizza pizza;

    @NotNull(message = "Kód veľkosti je povinný")
    @Enumerated(EnumType.STRING)
    @Column(name = "size_code", nullable = false)
    private SizeCode sizeCode;

    @NotNull(message = "Priemer je povinný")
    @Min(value = 1, message = "Priemer musí byť aspoň 1 cm")
    @Column(name = "diameter_cm", nullable = false)
    private Integer diameterCm;

    @NotNull(message = "Cena je povinná")
    @DecimalMin(value = "0.01", message = "Cena musí byť väčšia ako 0")
    @Column(name = "price_eur", nullable = false, precision = 10, scale = 2)
    private BigDecimal priceEur;

    @NotNull(message = "Stav dostupnosti musí byť zadaný")
    @Column(nullable = false)
    private Boolean available = true;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    public PizzaSize() {
    }

    public PizzaSize(Long id, Pizza pizza, SizeCode sizeCode, Integer diameterCm,
                     BigDecimal priceEur, Boolean available, LocalDateTime createdAt,
                     LocalDateTime updatedAt) {
        this.id = id;
        this.pizza = pizza;
        this.sizeCode = sizeCode;
        this.diameterCm = diameterCm;
        this.priceEur = priceEur;
        this.available = available;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
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

    public enum SizeCode {
        S, M, L
    }

    public String getSizeDisplayName() {
        return switch (sizeCode) {
            case S -> "Malá (" + diameterCm + " cm)";
            case M -> "Stredná (" + diameterCm + " cm)";
            case L -> "Veľká (" + diameterCm + " cm)";
        };
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Pizza getPizza() {
        return pizza;
    }

    public void setPizza(Pizza pizza) {
        this.pizza = pizza;
    }

    public SizeCode getSizeCode() {
        return sizeCode;
    }

    public void setSizeCode(SizeCode sizeCode) {
        this.sizeCode = sizeCode;
    }

    public Integer getDiameterCm() {
        return diameterCm;
    }

    public void setDiameterCm(Integer diameterCm) {
        this.diameterCm = diameterCm;
    }

    public BigDecimal getPriceEur() {
        return priceEur;
    }

    public void setPriceEur(BigDecimal priceEur) {
        this.priceEur = priceEur;
    }

    public Boolean getAvailable() {
        return available;
    }

    public void setAvailable(Boolean available) {
        this.available = available;
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

    @Override
    public String toString() {
        return "PizzaSize{" +
                "id=" + id +
                ", sizeCode=" + sizeCode +
                ", diameterCm=" + diameterCm +
                ", priceEur=" + priceEur +
                ", available=" + available +
                '}';
    }
}
