package sk.food.dodopizzeria.dto;

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class PasswordChangeDto {

    @NotBlank(message = "Aktuálne heslo je povinné")
    private String currentPassword;

    @NotBlank(message = "Nové heslo je povinné")
    @Size(min = 8, message = "Heslo musí mať minimálne 8 znakov")
    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$",
            message = "Heslo musí obsahovať veľké písmeno, malé písmeno, číslicu a špeciálny znak")
    private String newPassword;

    @NotBlank(message = "Potvrdenie hesla je povinné")
    private String confirmPassword;
}

