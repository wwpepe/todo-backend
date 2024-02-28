package backend.likelion.todos.todo;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalDate;

public record TodoCreateRequest(
        String content,
        @JsonFormat(pattern = "yyyy-MM-dd") LocalDate date,
        Long goalId
) {
}
