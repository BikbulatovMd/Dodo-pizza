package sk.food.dodopizzeria.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "ingredients")
public class Ingredient {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Názov ingrediencie je povinný")
    @Size(min = 2, max = 120, message = "Názov ingrediencie musí mať 2 až 120 znakov")
    @Column(name = "name_sk", nullable = false, unique = true, length = 120)
    private String nameSk;

    @Size(max = 120, message = "Informácie o alergénoch nesmú presiahnuť 120 znakov")
    @Column(name = "allergen_info_sk", length = 120)
    private String allergenInfoSk;

    @NotNull(message = "Vegánsky stav musí byť zadaný")
    @Column(nullable = false)
    private Boolean vegan = false;

    @NotNull(message = "Štipľavý stav musí byť zadaný")
    @Column(nullable = false)
    private Boolean spicy = false;

    @NotNull(message = "Príplatková cena je povinná")
    @DecimalMin(value = "0.0", message = "Príplatková cena musí byť aspoň 0")
    @Column(name = "extra_price_eur", nullable = false, precision = 10, scale = 2)
    private BigDecimal extraPriceEur = BigDecimal.ZERO;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @ManyToMany(mappedBy = "ingredients")
    private Set<Pizza> pizzas = new HashSet<>();

    public Ingredient() {
    }

    public Ingredient(Long id, String nameSk, String allergenInfoSk, Boolean vegan, Boolean spicy,
                      BigDecimal extraPriceEur, LocalDateTime createdAt, LocalDateTime updatedAt,
                      Set<Pizza> pizzas) {
        this.id = id;
        this.nameSk = nameSk;
        this.allergenInfoSk = allergenInfoSk;
        this.vegan = vegan;
        this.spicy = spicy;
        this.extraPriceEur = extraPriceEur;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.pizzas = pizzas;
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

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNameSk() {
        return nameSk;
    }

    public void setNameSk(String nameSk) {
        this.nameSk = nameSk;
    }

    public String getAllergenInfoSk() {
        return allergenInfoSk;
    }

    public void setAllergenInfoSk(String allergenInfoSk) {
        this.allergenInfoSk = allergenInfoSk;
    }

    public Boolean getVegan() {
        return vegan;
    }

    public void setVegan(Boolean vegan) {
        this.vegan = vegan;
    }

    public Boolean getSpicy() {
        return spicy;
    }

    public void setSpicy(Boolean spicy) {
        this.spicy = spicy;
    }

    public BigDecimal getExtraPriceEur() {
        return extraPriceEur;
    }

    public void setExtraPriceEur(BigDecimal extraPriceEur) {
        this.extraPriceEur = extraPriceEur;
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

    public Set<Pizza> getPizzas() {
        return pizzas;
    }

    public void setPizzas(Set<Pizza> pizzas) {
        this.pizzas = pizzas;
    }

    @Override
    public String toString() {
        return "Ingredient{" +
                "id=" + id +
                ", nameSk='" + nameSk + '\'' +
                ", vegan=" + vegan +
                ", spicy=" + spicy +
                '}';
    }
}
