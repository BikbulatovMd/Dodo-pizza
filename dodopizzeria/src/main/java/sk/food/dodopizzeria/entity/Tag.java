package sk.food.dodopizzeria.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "tags")
public class Tag {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Názov štítku je povinný")
    @Size(min = 2, max = 80, message = "Názov štítku musí mať 2 až 80 znakov")
    @Column(name = "name_sk", nullable = false, unique = true, length = 80)
    private String nameSk;

    @NotBlank(message = "Slug je povinný")
    @Size(max = 100, message = "Slug nesmie presiahnuť 100 znakov")
    @Pattern(regexp = "^[a-z0-9-]+$", message = "Slug môže obsahovať iba malé písmená, čísla a pomlčky")
    @Column(nullable = false, unique = true, length = 100)
    private String slug;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @ManyToMany(mappedBy = "tags")
    private Set<Pizza> pizzas = new HashSet<>();

    public Tag() {
    }

    public Tag(Long id, String nameSk, String slug, LocalDateTime createdAt,
               LocalDateTime updatedAt, Set<Pizza> pizzas) {
        this.id = id;
        this.nameSk = nameSk;
        this.slug = slug;
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

    public String getSlug() {
        return slug;
    }

    public void setSlug(String slug) {
        this.slug = slug;
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
        return "Tag{" +
                "id=" + id +
                ", nameSk='" + nameSk + '\'' +
                ", slug='" + slug + '\'' +
                '}';
    }
}
