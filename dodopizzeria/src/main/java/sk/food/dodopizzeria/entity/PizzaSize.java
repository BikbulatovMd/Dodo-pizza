package sk.food.dodopizzeria.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "pizza_sizes")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PizzaSize {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pizza_id", nullable = false)
    private Pizza pizza;

    @Enumerated(EnumType.STRING)
    @Column(name = "size_code", nullable = false)
    private SizeCode sizeCode;

    @Column(name = "diameter_cm", nullable = false)
    private Integer diameterCm;

    @Column(name = "price_eur", nullable = false, precision = 10, scale = 2)
    private BigDecimal priceEur;

    @Column(nullable = false)
    private Boolean available = true;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

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
}

