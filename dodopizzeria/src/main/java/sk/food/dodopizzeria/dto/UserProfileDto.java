package sk.food.dodopizzeria.dto;

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class UserProfileDto {

    private Long id;

    @NotBlank(message = "Email je povinný")
    @Email(message = "Zadajte platný email")
    private String email;

    @NotBlank(message = "Meno je povinné")
    @Size(max = 60, message = "Meno môže mať maximálne 60 znakov")
    private String firstName;

    @NotBlank(message = "Priezvisko je povinné")
    @Size(max = 60, message = "Priezvisko môže mať maximálne 60 znakov")
    private String lastName;

    @Size(max = 30, message = "Telefón môže mať maximálne 30 znakov")
    private String phone;

    @Size(max = 255, message = "Adresa môže mať maximálne 255 znakov")
    private String deliveryAddress;

    private String profileImageUrl;
}

