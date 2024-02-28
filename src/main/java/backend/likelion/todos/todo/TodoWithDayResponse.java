package backend.likelion.todos.todo;

import java.util.List;
import lombok.Getter;

@Getter
public class TodoWithDayResponse {

    private final int day;
    private final List<TodoResponse> todos;

    public TodoWithDayResponse(int day, List<TodoResponse> todos) {
        this.day = day;
        this.todos = todos;
    }

    @Getter
    public static class TodoResponse {

        private final Long todoId;
        private final String content;
        private final Long goalId;
        private final boolean isCompleted;

        public TodoResponse(Long todoId, String content, Long goalId, boolean isCompleted) {
            this.todoId = todoId;
            this.content = content;
            this.goalId = goalId;
            this.isCompleted = isCompleted;
        }
    }
}
