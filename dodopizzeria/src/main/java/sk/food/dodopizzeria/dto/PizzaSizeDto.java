package sk.food.dodopizzeria.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import sk.food.dodopizzeria.entity.PizzaSize;

import java.math.BigDecimal;

public class PizzaSizeDto {

    private Long id;

    private Long pizzaId;

    @NotNull(message = "Veľkosť je povinná")
    private PizzaSize.SizeCode sizeCode;

    @NotNull(message = "Priemer je povinný")
    @Min(value = 20, message = "Priemer musí byť minimálne 20 cm")
    @Max(value = 60, message = "Priemer môže byť maximálne 60 cm")
    private Integer diameterCm;

    @NotNull(message = "Cena je povinná")
    @DecimalMin(value = "0.01", message = "Cena musí byť vyššia ako 0")
    private BigDecimal priceEur;

    private Boolean available = true;

    public PizzaSizeDto() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getPizzaId() {
        return pizzaId;
    }

    public void setPizzaId(Long pizzaId) {
        this.pizzaId = pizzaId;
    }

    public PizzaSize.SizeCode getSizeCode() {
        return sizeCode;
    }

    public void setSizeCode(PizzaSize.SizeCode sizeCode) {
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
}
