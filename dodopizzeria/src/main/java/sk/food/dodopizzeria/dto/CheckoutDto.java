package sk.food.dodopizzeria.dto;

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class CheckoutDto {

    @NotBlank(message = "Adresa doručenia je povinná")
    @Size(max = 255, message = "Adresa môže mať maximálne 255 znakov")
    private String deliveryAddress;

    @Size(max = 800, message = "Poznámka môže mať maximálne 800 znakov")
    private String customerNote;
}

