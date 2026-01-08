package sk.food.dodopizzeria.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class CheckoutDto {

    @NotBlank(message = "Adresa doručenia je povinná")
    @Size(max = 255, message = "Adresa môže mať maximálne 255 znakov")
    private String deliveryAddress;

    @Size(max = 800, message = "Poznámka môže mať maximálne 800 znakov")
    private String customerNote;

    public CheckoutDto() {
    }

    public String getDeliveryAddress() {
        return deliveryAddress;
    }

    public void setDeliveryAddress(String deliveryAddress) {
        this.deliveryAddress = deliveryAddress;
    }

    public String getCustomerNote() {
        return customerNote;
    }

    public void setCustomerNote(String customerNote) {
        this.customerNote = customerNote;
    }
}
