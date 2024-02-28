package backend.likelion.todos.auth.jwt;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

import backend.likelion.todos.common.UnAuthorizedException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@DisplayName("JWT 서비스 (JwtService) 은(는)")
@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(ReplaceUnderscores.class)
class JwtServiceTest {

    @Autowired
    private JwtService jwtService;

    @Test
    void 회원_id를_받아_JWT를_생성한다() {
        // when
        String token = jwtService.createToken(1L);

        // then
        assertThat(token).isNotNull();
    }

    @Test
    void 주어진_JWT내의_회원_ID_정보를_추출한다() {
        // given
        String token = jwtService.createToken(1L);

        // when
        Long id = jwtService.extractMemberId(token);

        // then
        assertThat(id).isEqualTo(1L);
    }

    @Test
    void 토큰_정보_추출_시_토큰이_만료되었거나_잘못되었다면_예외가_발생한다() {
        // when & then
        assertThatThrownBy(() -> {
            jwtService.extractMemberId("invalidToken");
        }).isInstanceOf(UnAuthorizedException.class)
                .hasMessage("유효하지 않은 토큰입니다.");
    }
}
