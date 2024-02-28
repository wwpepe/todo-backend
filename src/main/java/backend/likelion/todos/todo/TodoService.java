package backend.likelion.todos.todo;


import backend.likelion.todos.common.NotFoundException;
import backend.likelion.todos.goal.Goal;
import backend.likelion.todos.goal.GoalRepository;
import backend.likelion.todos.member.Member;
import backend.likelion.todos.member.MemberRepository;
import backend.likelion.todos.todo.TodoWithDayResponse.TodoResponse;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TodoService {

    private final MemberRepository memberRepository;
    private final GoalRepository goalRepository;
    private final TodoRepository todoRepository;

    public Long save(Long goalId, Long memberId, String content, LocalDate date) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new NotFoundException("회원 정보가 없습니다."));
        Goal goal = goalRepository.findById(goalId)
                .orElseThrow(() -> new NotFoundException("목표 정보가 없습니다."));
        goal.validateMember(member);
        Todo todo = new Todo(content, date, goal);
        return todoRepository.save(todo)
                .getId();
    }

    public void update(Long todoId, Long memberId, String content, LocalDate date) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new NotFoundException("회원 정보가 없습니다."));
        Todo todo = todoRepository.findById(todoId)
                .orElseThrow(() -> new NotFoundException("투두 정보가 없습니다."));
        todo.validateMember(member);
        todo.update(content, date);
    }

    public void check(Long todoId, Long memberId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new NotFoundException("회원 정보가 없습니다."));
        Todo todo = todoRepository.findById(todoId)
                .orElseThrow(() -> new NotFoundException("투두 정보가 없습니다."));
        todo.validateMember(member);
        todo.check();
    }

    public void uncheck(Long todoId, Long memberId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new NotFoundException("회원 정보가 없습니다."));
        Todo todo = todoRepository.findById(todoId)
                .orElseThrow(() -> new NotFoundException("투두 정보가 없습니다."));
        todo.validateMember(member);
        todo.uncheck();
    }

    public void delete(Long todoId, Long memberId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new NotFoundException("회원 정보가 없습니다."));
        Todo todo = todoRepository.findById(todoId)
                .orElseThrow(() -> new NotFoundException("투두 정보가 없습니다."));
        todo.validateMember(member);
        todoRepository.delete(todo);
    }

    public List<TodoWithDayResponse> findAllByMemberIdAndDate(Long memberId, YearMonth date) {
        List<Todo> todos = todoRepository.findAllByMemberIdAndDate(memberId, date);
        Map<Integer, List<Todo>> todoWithDays = todos.stream()
                .collect(Collectors.groupingBy(it -> it.getDate().getDayOfMonth()));
        List<TodoWithDayResponse> responses = new ArrayList<>();
        for (Entry<Integer, List<Todo>> todo : todoWithDays.entrySet()) {
            List<TodoResponse> todoResponses = todo.getValue().stream()
                    .map(it -> new TodoResponse(
                            it.getId(),
                            it.getContent(),
                            it.getGoal().getId(),
                            it.isCompleted()
                    ))
                    .toList();
            responses.add(new TodoWithDayResponse(todo.getKey(), todoResponses));
        }
        return responses;
    }
}
