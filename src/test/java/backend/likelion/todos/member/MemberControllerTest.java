package backend.likelion.todos.member;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.springframework.http.HttpStatus.CONFLICT;
import static org.springframework.http.HttpStatus.CREATED;

import backend.likelion.todos.ApiTest;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@DisplayName("회원 컨트롤러 (MemberController) 은(는)")
@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(ReplaceUnderscores.class)
class MemberControllerTest extends ApiTest {

    @Test
    void RestController_빈으로_등록한다() {
        // when
        boolean isRestController = MemberController.class.isAnnotationPresent(RestController.class);

        // then
        assertThat(isRestController).isTrue();
    }

    @Test
    void members_로_들어오는_요청을_처리한다() {
        // given
        RequestMapping requestMapping = MemberController.class.getAnnotation(RequestMapping.class);

        // when
        String uri = requestMapping.value()[0];

        // then
        assertThat(uri).isEqualTo("/members");
    }

    @Nested
    class 회원가입_시 {

        @Test
        void 성공하면_201_상태코드와_함께_응답_헤더의_Location_값으로_생성된_회원을_조회할_수_있는_URL이_반환된다() {
            // given
            SignupRequest request = new SignupRequest(
                    "likelion",
                    "likelion1234",
                    "멋사",
                    "profileurl"
            );

            // when
            ExtractableResponse<Response> response = RestAssured.given()
                    .log().all()
                    .contentType(ContentType.JSON)
                    .body(request)
                    .post("/members")
                    .then()
                    .log().all()
                    .extract();

            // then
            assertThat(response.statusCode()).isEqualTo(CREATED.value());
        }

        @Test
        void 아이디가_중복되면_409_상태코드와_예외_메세지를_반환한다() {
            // given
            SignupRequest request = new SignupRequest(
                    "likelion",
                    "likelion1234",
                    "멋사",
                    "profileurl"
            );
            RestAssured.given()
                    .contentType(ContentType.JSON)
                    .body(request)
                    .post("/members")
                    .then()
                    .extract();

            // when
            ExtractableResponse<Response> response = RestAssured.given()
                    .contentType(ContentType.JSON)
                    .body(request)
                    .post("/members")
                    .then()
                    .log().all()
                    .extract();

            // then
            assertThat(response.statusCode()).isEqualTo(CONFLICT.value());
        }
    }
}
