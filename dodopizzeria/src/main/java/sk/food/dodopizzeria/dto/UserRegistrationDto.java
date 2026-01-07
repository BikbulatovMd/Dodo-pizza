package sk.food.dodopizzeria.dto;

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class UserRegistrationDto {

    @NotBlank(message = "Email je povinný")
    @Email(message = "Zadajte platný email")
    private String email;

    @NotBlank(message = "Heslo je povinné")
    @Size(min = 8, message = "Heslo musí mať minimálne 8 znakov")
    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$",
            message = "Heslo musí obsahovať veľké písmeno, malé písmeno, číslicu a špeciálny znak")
    private String password;

    @NotBlank(message = "Potvrdenie hesla je povinné")
    private String confirmPassword;

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
}

