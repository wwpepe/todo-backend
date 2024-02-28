package backend.likelion.todos.member;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;

@DisplayName("로그인 응답 (LoginResponse) 은(는)")
@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(ReplaceUnderscores.class)
class LoginResponseTest {

    @Test
    void 응답_클래스도_record_타입이_가능하다() {
        // when & then
        assertThat(LoginResponse.class.isRecord()).isTrue();
    }
}
