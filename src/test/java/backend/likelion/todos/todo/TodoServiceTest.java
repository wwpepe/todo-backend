package backend.likelion.todos.todo;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import backend.likelion.todos.common.ForbiddenException;
import backend.likelion.todos.common.NotFoundException;
import backend.likelion.todos.goal.Goal;
import backend.likelion.todos.goal.GoalRepository;
import backend.likelion.todos.member.Member;
import backend.likelion.todos.member.MemberRepository;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@DisplayName("투두 서비스 (TodoService) 은(는)")
@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(ReplaceUnderscores.class)
class TodoServiceTest {

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private GoalRepository goalRepository;

    @Autowired
    private TodoRepository todoRepository;

    @Autowired
    private TodoService todoService;

    private Member member;
    private Member other;
    private Goal goal;

    @BeforeEach
    void setUp() {
        todoRepository.clear();
        goalRepository.clear();
        memberRepository.clear();
        member = memberRepository.save(new Member("1", "1", "1", "1"));
        other = memberRepository.save(new Member("2", "2", "2", "2"));
        goal = goalRepository.save(new Goal("g", "c", member));
    }

    @Nested
    class 투두_생성_시 {

        @Test
        void 회원_정보가_없으면_예외() {
            // when & then
            assertThatThrownBy(() -> {
                todoService.save(1L, 100L, "", LocalDate.of(2000, 10, 4));
            }).isInstanceOf(NotFoundException.class)
                    .hasMessage("회원 정보가 없습니다.");
        }

        @Test
        void 목표_정보가_없으면_예외() {
            // when & then
            assertThatThrownBy(() -> {
                todoService.save(100L, member.getId(), "", LocalDate.of(2000, 10, 4));
            }).isInstanceOf(NotFoundException.class)
                    .hasMessage("목표 정보가 없습니다.");
        }

        @Test
        void 나의_목표가_아니면_예외() {
            // when & then
            assertThatThrownBy(() -> {
                todoService.save(goal.getId(), other.getId(), "", LocalDate.of(2000, 10, 4));
            }).isInstanceOf(ForbiddenException.class)
                    .hasMessage("해당 목표에 대한 권한이 없습니다.");
        }

        @Test
        void 투두를_생성한다() {
            // when
            Long todoId = todoService.save(goal.getId(), member.getId(), "", LocalDate.of(2000, 10, 4));

            // then
            assertThat(todoRepository.findById(todoId)).isPresent();
        }
    }

    @Nested
    class 투두_수정_시 {

        private Long todoId;

        @BeforeEach
        void setUp() {
            todoId = todoService.save(goal.getId(), member.getId(), "", LocalDate.of(2000, 10, 4));
        }

        @Test
        void 회원_정보가_없으면_예외() {
            // when & then
            assertThatThrownBy(() -> {
                todoService.update(todoId, 100L, "", LocalDate.of(2000, 10, 4));
            }).isInstanceOf(NotFoundException.class)
                    .hasMessage("회원 정보가 없습니다.");
        }

        @Test
        void 투두_정보가_없으면_예외() {
            // when & then
            assertThatThrownBy(() -> {
                todoService.update(100L, member.getId(), "", LocalDate.of(2000, 10, 4));
            }).isInstanceOf(NotFoundException.class)
                    .hasMessage("투두 정보가 없습니다.");
        }

        @Test
        void 나의_투두가_아니면_예외() {
            // when & then
            assertThatThrownBy(() -> {
                todoService.update(todoId, other.getId(), "", LocalDate.of(2000, 10, 4));
            }).isInstanceOf(ForbiddenException.class)
                    .hasMessage("해당 투두에 대한 권한이 없습니다.");
        }

        @Test
        void 투두의_내용과_날짜를_수정한다() {
            // when
            todoService.update(todoId, member.getId(), "u", LocalDate.of(1000, 12, 4));

            // then
            Todo todo = todoRepository.findById(todoId).get();
            assertThat(todo.getContent()).isEqualTo("u");
            assertThat(todo.getDate()).isEqualTo(LocalDate.of(1000, 12, 4));
        }
    }

    @Nested
    class 투두_체크_시 {

        private Long todoId;

        @BeforeEach
        void setUp() {
            todoId = todoService.save(goal.getId(), member.getId(), "", LocalDate.of(2000, 10, 4));
        }

        @Test
        void 회원_정보가_없으면_예외() {
            // when & then
            assertThatThrownBy(() -> {
                todoService.check(todoId, 100L);
            }).isInstanceOf(NotFoundException.class)
                    .hasMessage("회원 정보가 없습니다.");
        }

        @Test
        void 투두_정보가_없으면_예외() {
            // when & then
            assertThatThrownBy(() -> {
                todoService.check(100L, member.getId());
            }).isInstanceOf(NotFoundException.class)
                    .hasMessage("투두 정보가 없습니다.");
        }

        @Test
        void 나의_투두가_아니면_예외() {
            // when & then
            assertThatThrownBy(() -> {
                todoService.check(todoId, other.getId());
            }).isInstanceOf(ForbiddenException.class)
                    .hasMessage("해당 투두에 대한 권한이 없습니다.");
        }

        @Test
        void 투두를_체크한다() {
            // when
            todoService.check(todoId, member.getId());

            // then
            assertThat(todoRepository.findById(todoId).get().isCompleted()).isTrue();
        }
    }

    @Nested
    class 투두_체크_취소_시 {

        private Long todoId;

        @BeforeEach
        void setUp() {
            todoId = todoService.save(goal.getId(), member.getId(), "", LocalDate.of(2000, 10, 4));
        }

        @Test
        void 회원_정보가_없으면_예외() {
            // when & then
            assertThatThrownBy(() -> {
                todoService.uncheck(todoId, 100L);
            }).isInstanceOf(NotFoundException.class)
                    .hasMessage("회원 정보가 없습니다.");
        }

        @Test
        void 투두_정보가_없으면_예외() {
            // when & then
            assertThatThrownBy(() -> {
                todoService.uncheck(100L, member.getId());
            }).isInstanceOf(NotFoundException.class)
                    .hasMessage("투두 정보가 없습니다.");
        }

        @Test
        void 나의_투두가_아니면_예외() {
            // when & then
            assertThatThrownBy(() -> {
                todoService.uncheck(todoId, other.getId());
            }).isInstanceOf(ForbiddenException.class)
                    .hasMessage("해당 투두에 대한 권한이 없습니다.");
        }

        @Test
        void 투두를_체크한다() {
            // given
            todoService.check(todoId, member.getId());

            // when
            todoService.uncheck(todoId, member.getId());

            // then
            assertThat(todoRepository.findById(todoId).get().isCompleted()).isFalse();
        }
    }

    @Nested
    class 투두_삭제_시 {

        private Long todoId;

        @BeforeEach
        void setUp() {
            todoId = todoService.save(goal.getId(), member.getId(), "", LocalDate.of(2000, 10, 4));
        }

        @Test
        void 회원_정보가_없으면_예외() {
            // when & then
            assertThatThrownBy(() -> {
                todoService.delete(todoId, 100L);
            }).isInstanceOf(NotFoundException.class)
                    .hasMessage("회원 정보가 없습니다.");
        }

        @Test
        void 투두_정보가_없으면_예외() {
            // when & then
            assertThatThrownBy(() -> {
                todoService.delete(100L, member.getId());
            }).isInstanceOf(NotFoundException.class)
                    .hasMessage("투두 정보가 없습니다.");
        }

        @Test
        void 나의_투두가_아니면_예외() {
            // when & then
            assertThatThrownBy(() -> {
                todoService.delete(todoId, other.getId());
            }).isInstanceOf(ForbiddenException.class)
                    .hasMessage("해당 투두에 대한 권한이 없습니다.");
        }

        @Test
        void 투두를_제거한다() {
            // when
            todoService.delete(todoId, member.getId());

            // then
            assertThat(todoRepository.findById(todoId)).isEmpty();
        }
    }

    @Nested
    class 특정_년도와_달에_포함된_특정_회원의_TODO_조회_시 {

        @Test
        void 특정_년도와_달에_포함된_특정_회원의_TODO_를_찾아_1일부터_오름차순으로_조회한다() {
            // given
            todoRepository.save(new Todo("3", LocalDate.of(2024, 10, 3), goal));
            todoRepository.save(new Todo("4", LocalDate.of(2024, 10, 4), goal));
            todoRepository.save(new Todo("2", LocalDate.of(2024, 10, 1), goal));
            todoRepository.save(new Todo("5", LocalDate.of(2024, 10, 21), goal));

            todoRepository.save(new Todo("6", LocalDate.of(2024, 11, 21), goal));
            todoRepository.save(new Todo("7", LocalDate.of(2043, 10, 21), goal));

            // when
            List<Todo> result = todoRepository.findAllByMemberIdAndDate(member.getId(), YearMonth.of(2024, 10));

            // then
            assertThat(result)
                    .hasSize(4)
                    .extracting(Todo::getContent)
                    .containsExactly("2", "3", "4", "5");
        }
    }
}
