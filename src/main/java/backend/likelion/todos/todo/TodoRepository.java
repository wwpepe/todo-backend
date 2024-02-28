package backend.likelion.todos.todo;

import java.time.YearMonth;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.stereotype.Repository;

@Repository
public class TodoRepository {

    private final Map<Long, Todo> todos = new HashMap<>();
    private Long id = 1L;

    public Todo save(Todo todo) {
        todo.setId(id);
        todos.put(id++, todo);
        return todo;
    }

    public Optional<Todo> findById(Long id) {
        return Optional.ofNullable(todos.get(id));
    }

    public void clear() {
        todos.clear();
    }

    public void delete(Todo todo) {
        todos.remove(todo.getId());
    }

    public List<Todo> findAllByMemberIdAndDate(Long memberId, YearMonth yearMonth) {
        List<Todo> list = todos.values()
                .stream()
                .filter(it -> it.getGoal().getMember().getId().equals(memberId))
                .filter(it -> it.getDate().getYear() == yearMonth.getYear())
                .filter(it -> it.getDate().getMonthValue() == yearMonth.getMonthValue())
                .collect(Collectors.toList());
        Collections.sort(list, Comparator.comparing(Todo::getDate));
        return list;
    }
}
