package backend.likelion.todos.member;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.http.HttpStatus.CONFLICT;
import static org.springframework.http.HttpStatus.CREATED;

import backend.likelion.todos.ApiTest;
import backend.likelion.todos.common.ExceptionResponse;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
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
                    .log().all()
                    .body(request)
                    .post("/members")
                    .then()
                    .log().all()
                    .extract();

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
            assertThat(response.statusCode()).isEqualTo(CONFLICT.value());
        }
    }

    @Nested
    class 로그인_시 {

        @BeforeEach
        void setUp() {
            SignupRequest request = new SignupRequest(
                    "likelion",
                    "likelion1234",
                    "멋사",
                    "profile"
            );
            RestAssured.given()
                    .contentType(ContentType.JSON)
                    .log().all()
                    .body(request)
                    .post("/members")
                    .then()
                    .log().all()
                    .extract();
        }

        @Test
        void 아이디가_없다면_401_상태코드와_예외_메세지를_보낸다() {
            // given
            LoginRequest request = new LoginRequest(
                    "none",
                    "likelion1234"
            );

            // when
            ExtractableResponse<Response> response = RestAssured.given()
                    .log().all()
                    .contentType(ContentType.JSON)
                    .body(request)
                    .post("/members/login")
                    .then()
                    .log().all()
                    .extract();

            // then
            assertThat(response.statusCode()).isEqualTo(HttpStatus.UNAUTHORIZED.value());
            assertThat(response.as(ExceptionResponse.class).message())
                    .isEqualTo("존재하지 않는 아이디입니다.");
        }

        @Test
        void 비밀번호가_틀렸다면_401_상태코드와_예외_메세지를_보낸다() {
            // given
            LoginRequest request = new LoginRequest(
                    "likelion",
                    "wrong"
            );

            // when
            ExtractableResponse<Response> response = RestAssured.given()
                    .log().all()
                    .contentType(ContentType.JSON)
                    .body(request)
                    .post("/members/login")
                    .then()
                    .log().all()
                    .extract();

            // then
            assertThat(response.statusCode()).isEqualTo(HttpStatus.UNAUTHORIZED.value());
            assertThat(response.as(ExceptionResponse.class).message())
                    .isEqualTo("비밀번호가 일치하지 않습니다");
        }

        @Test
        void 로그인에_성공하면_200_상태코드와_AccessToken을_반환한다() {
            // given
            LoginRequest request = new LoginRequest(
                    "likelion",
                    "likelion1234"
            );

            // when
            ExtractableResponse<Response> response = RestAssured.given()
                    .log().all()
                    .contentType(ContentType.JSON)
                    .body(request)
                    .post("/members/login")
                    .then()
                    .log().all()
                    .extract();

            // then
            assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
            assertThat(response.as(LoginResponse.class).accessToken())
                    .isNotNull();
        }
    }

    @Nested
    class 내_정보_조회_시 {

        private String accessToken;

        @BeforeEach
        void setUp() {
            SignupRequest request = new SignupRequest(
                    "likelion",
                    "likelion1234",
                    "멋사",
                    "profile"
            );
            RestAssured.given()
                    .contentType(ContentType.JSON)
                    .log().all()
                    .body(request)
                    .post("/members")
                    .then()
                    .log().all()
                    .extract();
            ExtractableResponse<Response> response = RestAssured.given()
                    .log().all()
                    .contentType(ContentType.JSON)
                    .body(new LoginRequest(
                            "likelion",
                            "likelion1234"
                    ))
                    .post("/members/login")
                    .then()
                    .log().all()
                    .extract();
            accessToken = response.as(LoginResponse.class).accessToken();
        }

        @Test
        void 인증되지_않았다면_401_오류() {
            // when
            ExtractableResponse<Response> response = RestAssured.given()
                    .log().all()
                    .get("/members/my")
                    .then()
                    .log().all()
                    .extract();

            // then
            assertThat(response.statusCode()).isEqualTo(401);
        }

        @Test
        void 인증정보가_있다면_내_정보를_반환한다() {
            // when
            ExtractableResponse<Response> response = RestAssured.given()
                    .log().all()
                    .header("Authorization", "Bearer " + accessToken)
                    .get("/members/my")
                    .then()
                    .log().all()
                    .extract();

            // then
            MemberResponse memberResponse = response.as(MemberResponse.class);
            assertThat(memberResponse.getUsername()).isEqualTo("likelion");
            assertThat(memberResponse.getNickname()).isEqualTo("멋사");
            assertThat(memberResponse.getProfileImageUrl()).isEqualTo("profile");
        }
    }
}
