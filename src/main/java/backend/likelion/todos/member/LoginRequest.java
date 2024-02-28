package backend.likelion.todos.member;

public record LoginRequest(
        String username,
        String password
) {
}
