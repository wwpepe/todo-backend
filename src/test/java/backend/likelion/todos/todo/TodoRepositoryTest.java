package backend.likelion.todos.todo;

import static org.assertj.core.api.Assertions.assertThat;

import backend.likelion.todos.goal.Goal;
import backend.likelion.todos.goal.GoalRepository;
import backend.likelion.todos.member.Member;
import backend.likelion.todos.member.MemberRepository;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.jdbc.DataJdbcTest;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@DisplayName("투두 저장소 (TodoRepository) 은(는)")
@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(ReplaceUnderscores.class)
class TodoRepositoryTest {

    private Member member = new Member("u", "p", "n", "p");
    private Member other = new Member("o", "p", "n", "p");
    private Goal goal1 = new Goal("", "", member);
    private Goal goal2 = new Goal("", "", member);
    private Goal goal3 = new Goal("", "", other);

    @Autowired
    private TodoRepository todoRepository;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private GoalRepository goalRepository;

    @Test
    void 특정_년도와_달에_포함된_특정_회원의_TODO_를_찾아_1일부터_오름차순으로_조회한다() {
        // given
        member = memberRepository.save(member);
        other = memberRepository.save(other);
        goal1 = goalRepository.save(goal1);
        goal2 = goalRepository.save(goal2);
        goal3 = goalRepository.save(goal3);
        todoRepository.save(new Todo("1", LocalDate.of(2024, 10, 1), goal3));

        todoRepository.save(new Todo("3", LocalDate.of(2024, 10, 3), goal2));
        todoRepository.save(new Todo("4", LocalDate.of(2024, 10, 4), goal1));
        todoRepository.save(new Todo("2", LocalDate.of(2024, 10, 1), goal1));
        todoRepository.save(new Todo("5", LocalDate.of(2024, 10, 21), goal2));

        todoRepository.save(new Todo("6", LocalDate.of(2024, 11, 21), goal1));
        todoRepository.save(new Todo("7", LocalDate.of(2043, 10, 21), goal1));

        // when
        List<Todo> result = todoRepository.findAllByMemberIdAndDate(member.getId(),
                LocalDate.from(YearMonth.of(2024, 10))
        );

        // then
        assertThat(result)
                .hasSize(4)
                .extracting(Todo::getContent)
                .containsExactly("2", "3", "4", "5");
    }
}
