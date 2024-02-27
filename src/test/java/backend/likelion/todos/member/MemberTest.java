package backend.likelion.todos.member;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import backend.likelion.todos.common.UnAuthorizedException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;

@DisplayName("회원 (Member) 은(는)")
@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(ReplaceUnderscores.class)
class MemberTest {

    @Test
    void 로그인_시_비밀번호가_다르면_예외() {
        // given
        Member member = new Member("u", "p", "n", "p");

        // when & then
        assertThatThrownBy(() -> {
            member.login("1");
        }).isInstanceOf(UnAuthorizedException.class)
                .hasMessage("비밀번호가 일치하지 않습니다");
    }

    @Test
    void 로그인_시_비밀번호가_일치하면_성공() {
        // given
        Member member = new Member("u", "p", "n", "p");

        // when & then
        assertDoesNotThrow(() -> {
            member.login("p");
        });
    }
}
