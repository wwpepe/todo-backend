package backend.likelion.todos.goal;

import lombok.Getter;

@Getter
public class GoalResponse {

    private final Long id;
    private final String name;
    private final String color;

    public GoalResponse(Long id, String name, String color) {
        this.id = id;
        this.name = name;
        this.color = color;
    }
}
