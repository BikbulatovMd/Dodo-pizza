package sk.food.dodopizzeria.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "pizzas")
public class Pizza {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Názov pizze je povinný")
    @Size(min = 2, max = 120, message = "Názov pizze musí mať 2 až 120 znakov")
    @Column(name = "name_sk", nullable = false, length = 120)
    private String nameSk;

    @Size(max = 800, message = "Popis nesmie presiahnuť 800 znakov")
    @Column(name = "description_sk", length = 800)
    private String descriptionSk;

    @NotBlank(message = "Slug je povinný")
    @Size(max = 140, message = "Slug nesmie presiahnuť 140 znakov")
    @Pattern(regexp = "^[a-z0-9-]+$", message = "Slug môže obsahovať iba malé písmená, čísla a pomlčky")
    @Column(nullable = false, unique = true, length = 140)
    private String slug;

    @Size(max = 500, message = "URL obrázka nesmie presiahnuť 500 znakov")
    @Column(name = "image_url", length = 500)
    private String imageUrl;

    @NotNull(message = "Stav aktivity musí byť zadaný")
    @Column(nullable = false)
    private Boolean active = true;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @OneToMany(mappedBy = "pizza", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PizzaSize> sizes = new ArrayList<>();

    @ManyToMany
    @JoinTable(
            name = "pizza_ingredients",
            joinColumns = @JoinColumn(name = "pizza_id"),
            inverseJoinColumns = @JoinColumn(name = "ingredient_id")
    )
    private Set<Ingredient> ingredients = new HashSet<>();

    @ManyToMany
    @JoinTable(
            name = "pizza_tags",
            joinColumns = @JoinColumn(name = "pizza_id"),
            inverseJoinColumns = @JoinColumn(name = "tag_id")
    )
    private Set<Tag> tags = new HashSet<>();

    public Pizza() {
    }

    public Pizza(Long id, String nameSk, String descriptionSk, String slug, String imageUrl,
                 Boolean active, LocalDateTime createdAt, LocalDateTime updatedAt,
                 List<PizzaSize> sizes, Set<Ingredient> ingredients, Set<Tag> tags) {
        this.id = id;
        this.nameSk = nameSk;
        this.descriptionSk = descriptionSk;
        this.slug = slug;
        this.imageUrl = imageUrl;
        this.active = active;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.sizes = sizes;
        this.ingredients = ingredients;
        this.tags = tags;
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

    // Get minimum price from available sizes
    public java.math.BigDecimal getMinPrice() {
        return sizes.stream()
                .filter(PizzaSize::getAvailable)
                .map(PizzaSize::getPriceEur)
                .min(java.math.BigDecimal::compareTo)
                .orElse(java.math.BigDecimal.ZERO);
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

    public String getDescriptionSk() {
        return descriptionSk;
    }

    public void setDescriptionSk(String descriptionSk) {
        this.descriptionSk = descriptionSk;
    }

    public String getSlug() {
        return slug;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
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

    public List<PizzaSize> getSizes() {
        return sizes;
    }

    public void setSizes(List<PizzaSize> sizes) {
        this.sizes = sizes;
    }

    public Set<Ingredient> getIngredients() {
        return ingredients;
    }

    public void setIngredients(Set<Ingredient> ingredients) {
        this.ingredients = ingredients;
    }

    public Set<Tag> getTags() {
        return tags;
    }

    public void setTags(Set<Tag> tags) {
        this.tags = tags;
    }

    @Override
    public String toString() {
        return "Pizza{" +
                "id=" + id +
                ", nameSk='" + nameSk + '\'' +
                ", slug='" + slug + '\'' +
                ", active=" + active +
                '}';
    }
}
