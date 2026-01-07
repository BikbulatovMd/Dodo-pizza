package sk.food.dodopizzeria.dto;

import jakarta.validation.constraints.*;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class IngredientDto {

    private Long id;

    @NotBlank(message = "Názov ingrediencie je povinný")
    @Size(max = 120, message = "Názov môže mať maximálne 120 znakov")
    private String nameSk;

    @Size(max = 120, message = "Info o alergénoch môže mať maximálne 120 znakov")
    private String allergenInfoSk;

    private Boolean vegan = false;

    private Boolean spicy = false;

    @NotNull(message = "Cena za extra je povinná")
    @DecimalMin(value = "0.00", message = "Cena nemôže byť záporná")
    private BigDecimal extraPriceEur = BigDecimal.ZERO;
}

