package backend.likelion.todos.goal;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.springframework.stereotype.Repository;

@Repository
public class GoalRepository {

    private final Map<Long, Goal> goals = new HashMap<>();
    private Long id = 1L;

    public Goal save(Goal goal) {
        goal.setId(id);
        goals.put(id++, goal);
        return goal;
    }

    public Optional<Goal> findById(Long id) {
        return Optional.ofNullable(goals.get(id));
    }

    public void clear() {
        goals.clear();
    }

    public void delete(Goal goal) {
        goals.remove(goal.getId());
    }

    public List<Goal> findAllByMemberId(Long memberId) {
        return goals.values()
                .stream()
                .filter(it -> it.getMember().getId().equals(memberId))
                .toList();
    }
}
