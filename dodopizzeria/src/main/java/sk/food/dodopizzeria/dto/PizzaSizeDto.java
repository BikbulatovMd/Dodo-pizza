package sk.food.dodopizzeria.dto;

import jakarta.validation.constraints.*;
import lombok.Data;
import sk.food.dodopizzeria.entity.PizzaSize;

import java.math.BigDecimal;

@Data
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
}

