package sk.food.dodopizzeria.exception;

public class ResourceNotFoundException extends RuntimeException {

    public ResourceNotFoundException(String message) {
        super(message);
    }

    public ResourceNotFoundException(String resourceName, Long id) {
        super(resourceName + " s ID " + id + " nebol nájdený");
    }

    public ResourceNotFoundException(String resourceName, String field, String value) {
        super(resourceName + " s " + field + " '" + value + "' nebol nájdený");
    }
}

