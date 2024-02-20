package backend.likelion.todos.common;

public class ConflictException extends RuntimeException {

    public ConflictException(String message) {
        super(message);
    }
}
