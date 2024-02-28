package backend.likelion.todos.member;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;

@DisplayName("로그인 요청 (LoginRequest) 은(는)")
@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(ReplaceUnderscores.class)
class LoginRequestTest {

    @Test
    void record_클래스도_사용_가능하다() {
        // when & then
        assertThat(LoginRequest.class.isRecord()).isTrue();
    }
}
