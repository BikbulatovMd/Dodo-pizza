package sk.food.dodopizzeria.exception;

public class InvalidPasswordException extends RuntimeException {

    public InvalidPasswordException() {
        super("Nesprávne aktuálne heslo");
    }

    public InvalidPasswordException(String message) {
        super(message);
    }
}

