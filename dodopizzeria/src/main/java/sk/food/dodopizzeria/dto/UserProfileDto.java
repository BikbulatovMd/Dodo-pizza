package sk.food.dodopizzeria.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

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

    public UserProfileDto() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
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

    public String getProfileImageUrl() {
        return profileImageUrl;
    }

    public void setProfileImageUrl(String profileImageUrl) {
        this.profileImageUrl = profileImageUrl;
    }
}
