package sk.food.dodopizzeria.exception;

public class EmailAlreadyExistsException extends RuntimeException {

    public EmailAlreadyExistsException(String email) {
        super("Používateľ s emailom '" + email + "' už existuje");
    }
}

