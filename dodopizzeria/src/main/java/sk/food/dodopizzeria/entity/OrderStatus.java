package sk.food.dodopizzeria.entity;

public enum OrderStatus {
    PENDING("Čakajúca"),
    PREPARING("Pripravuje sa"),
    READY("Pripravená"),
    DELIVERING("Doručuje sa"),
    DELIVERED("Doručená"),
    CANCELLED("Zrušená");

    private final String displayNameSk;

    OrderStatus(String displayNameSk) {
        this.displayNameSk = displayNameSk;
    }

    public String getDisplayNameSk() {
        return displayNameSk;
    }
}

