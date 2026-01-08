package sk.food.dodopizzeria.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;

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

    public IngredientDto() {
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
}
