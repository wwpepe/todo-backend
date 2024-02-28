package backend.likelion.todos.todo;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalDate;

public record TodoUpdateRequest(
        String content,
        @JsonFormat(pattern = "yyyy-MM-dd") LocalDate date
) {
}
