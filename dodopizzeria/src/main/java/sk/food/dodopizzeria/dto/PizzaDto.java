package sk.food.dodopizzeria.dto;

import jakarta.validation.constraints.*;
import lombok.Data;

import java.util.HashSet;
import java.util.Set;

@Data
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
}

