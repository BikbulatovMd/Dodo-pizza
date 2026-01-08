package sk.food.dodopizzeria.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import java.util.HashSet;
import java.util.Set;

public class PizzaDto {

    private Long id;

    @NotBlank(message = "Názov pizze je povinný")
    @Size(max = 120, message = "Názov môže mať maximálne 120 znakov")
    private String nameSk;

    @Size(max = 800, message = "Popis môže mať maximálne 800 znakov")
    private String descriptionSk;

    @NotBlank(message = "Slug je povinný")
    @Size(max = 140, message = "Slug môže mať maximálne 140 znakov")
    @Pattern(regexp = "^[a-z0-9-]+$", message = "Slug môže obsahovať len malé písmená, čísla a pomlčky")
    private String slug;

    private String imageUrl;

    private Boolean active = true;

    private Set<Long> ingredientIds = new HashSet<>();

    private Set<Long> tagIds = new HashSet<>();

    public PizzaDto() {
    }

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

    public Set<Long> getIngredientIds() {
        return ingredientIds;
    }

    public void setIngredientIds(Set<Long> ingredientIds) {
        this.ingredientIds = ingredientIds;
    }

    public Set<Long> getTagIds() {
        return tagIds;
    }

    public void setTagIds(Set<Long> tagIds) {
        this.tagIds = tagIds;
    }
}
