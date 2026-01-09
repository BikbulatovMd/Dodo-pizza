package sk.food.dodopizzeria.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

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
    @Pattern(
            regexp = "^$|^\\+421\\d{9}$",
            message = "Telefón musí byť vo formáte +421XXXXXXXXX"
    )
    private String phone;

    @Size(max = 255, message = "Adresa môže mať maximálne 255 znakov")
    private String deliveryAddress;

    public UserRegistrationDto() {
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getConfirmPassword() {
        return confirmPassword;
    }

    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getDeliveryAddress() {
        return deliveryAddress;
    }

    public void setDeliveryAddress(String deliveryAddress) {
        this.deliveryAddress = deliveryAddress;
    }
}
