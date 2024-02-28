package backend.likelion.todos.goal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import backend.likelion.todos.common.ForbiddenException;
import backend.likelion.todos.member.Member;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

@DisplayName("목표 (Goal) 은(는)")
@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(ReplaceUnderscores.class)
class GoalTest {

    private final Member member = new Member("u", "p", "n", "p");
    private final Member other = new Member("o", "p", "n", "p");

    @Nested
    class 회원_검증_시 {

        @Test
        void 주인이_아니면_예외() {
            // given
            Goal goal = new Goal("name", "color", member);

            // when & then
            assertThatThrownBy(() -> {
                goal.validateMember(other);
            }).isInstanceOf(ForbiddenException.class)
                    .hasMessage("해당 목표에 대한 권한이 없습니다.");
        }

        @Test
        void 주인이라면_통과() {
            // given
            Goal goal = new Goal("name", "color", member);

            // when & then
            assertDoesNotThrow(() -> {
                goal.validateMember(member);
            });
        }
    }

    @Test
    void 이름과_색깔을_수정할_수_있다() {
        // given
        Goal goal = new Goal("name", "color", member);

        // when
        goal.update("uname", "ucolor");

        // then
        assertThat(goal.getName()).isEqualTo("uname");
        assertThat(goal.getColor()).isEqualTo("ucolor");
    }
}
