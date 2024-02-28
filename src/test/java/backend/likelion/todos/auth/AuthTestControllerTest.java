package backend.likelion.todos.auth;


import static org.assertj.core.api.Assertions.assertThat;

import backend.likelion.todos.ApiTest;
import backend.likelion.todos.auth.jwt.JwtService;
import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;

@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(ReplaceUnderscores.class)
@DisplayName("인증이 필요한 요청 테스트")
class AuthTestControllerTest extends ApiTest {

    @Autowired
    private JwtService jwtService;

    @Test
    void 인증이_필요한_요청에_대해서는_accessToken을_Request_Header의_Authorization_값으로_설정하여_보낸다_이때_값은_Bearer_accessToken_형식이다() {
        // given
        String accessToken = jwtService.createToken(10L);

        // when
        ExtractableResponse<Response> response = RestAssured.given()
                .header("Authorization", "Bearer " + accessToken)
                .get("/test/auth")
                .then()
                .extract();

        // then
        assertThat(response.asString()).isEqualTo("아이디가 10인 회원 인증 성공!");
    }

    @Test
    void accessToken이_올바르지_않은_경우_접근할_수_없다() {
        // when
        ExtractableResponse<Response> response = RestAssured.given()
                .header("Authorization", "Bearer " + "wrong")
                .get("/test/auth")
                .then()
                .extract();

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.UNAUTHORIZED.value());
    }
}
