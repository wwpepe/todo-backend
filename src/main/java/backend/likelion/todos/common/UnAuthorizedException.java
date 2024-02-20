package backend.likelion.todos.common;

public class UnAuthorizedException extends RuntimeException {

    public UnAuthorizedException(String message) {
        super(message);
    }
}
