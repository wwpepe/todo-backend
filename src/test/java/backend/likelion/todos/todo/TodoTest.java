package backend.likelion.todos.todo;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import backend.likelion.todos.common.ForbiddenException;
import backend.likelion.todos.goal.Goal;
import backend.likelion.todos.member.Member;
import java.time.LocalDate;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

@DisplayName("투두 (Todo) 은(는)")
@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(ReplaceUnderscores.class)
class TodoTest {

    private final Member member = new Member("u", "p", "n", "p");
    private final Member other = new Member("o", "p", "n", "p");
    private final Goal goal = new Goal("", "", member);

    @Nested
    class 회원_검증_시 {

        @Test
        void 주인이_아니면_예외() {
            // given
            Todo todo = new Todo("content", LocalDate.now(), goal);

            // when & then
            assertThatThrownBy(() -> {
                todo.validateMember(other);
            }).isInstanceOf(ForbiddenException.class)
                    .hasMessage("해당 투두에 대한 권한이 없습니다.");
        }

        @Test
        void 주인이라면_통과() {
            // given
            Todo todo = new Todo("content", LocalDate.now(), goal);

            // when & then
            assertDoesNotThrow(() -> {
                todo.validateMember(member);
            });
        }
    }

    @Test
    void 내용과_날짜를_수정할_수_있다() {
        // given
        Todo todo = new Todo("content", LocalDate.now(), goal);

        // when
        todo.update("u", LocalDate.now().plusDays(2));

        // then
        assertThat(todo.getContent()).isEqualTo("u");
        assertThat(todo.getDate()).isEqualTo(LocalDate.now().plusDays(2));
    }

    @Test
    void 체크할_수_있다() {
        // given
        Todo todo = new Todo("content", LocalDate.now(), goal);

        // when
        todo.check();

        // then
        assertThat(todo.isCompleted()).isTrue();
    }

    @Test
    void 체크를_취소할_수_있다() {
        // given
        Todo todo = new Todo("content", LocalDate.now(), goal);
        todo.check();

        // when
        todo.uncheck();

        // then
        assertThat(todo.isCompleted()).isFalse();
    }
}
