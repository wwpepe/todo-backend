package backend.likelion.todos.goal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;

import backend.likelion.todos.ApiTest;
import backend.likelion.todos.common.ExceptionResponse;
import backend.likelion.todos.member.LoginRequest;
import backend.likelion.todos.member.LoginResponse;
import backend.likelion.todos.member.SignupRequest;
import io.restassured.RestAssured;
import io.restassured.common.mapper.TypeRef;
import io.restassured.http.ContentType;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

@DisplayName("목표 인수테스트")
@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(ReplaceUnderscores.class)
class GoalAcceptanceTest extends ApiTest {

    private String member1Token;
    private String member2Token;

    @BeforeEach
    @Override
    protected void setUp() {
        super.setUp();
        RestAssured.given()
                .contentType(ContentType.JSON)
                .log().all()
                .body(new SignupRequest(
                        "likelion",
                        "likelion1234",
                        "멋사",
                        "profile"
                ))
                .post("/members")
                .then()
                .log().all()
                .extract();
        ExtractableResponse<Response> response1 = RestAssured.given()
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
        member1Token = response1.as(LoginResponse.class).accessToken();

        RestAssured.given()
                .contentType(ContentType.JSON)
                .log().all()
                .body(new SignupRequest(
                        "unlikelion",
                        "likelion1234",
                        "안멋사",
                        "profile"
                ))
                .post("/members")
                .then()
                .log().all()
                .extract();
        ExtractableResponse<Response> response2 = RestAssured.given()
                .log().all()
                .contentType(ContentType.JSON)
                .body(new LoginRequest(
                        "unlikelion",
                        "likelion1234"
                ))
                .post("/members/login")
                .then()
                .log().all()
                .extract();
        member2Token = response2.as(LoginResponse.class).accessToken();
    }

    @Nested
    class 목표_생성_시 {

        @Test
        void 인증정보가_없으면_401_예외() {
            // given
            GoalCreateRequest request = new GoalCreateRequest("목표", "#000000");

            // when
            ExtractableResponse<Response> response = RestAssured.given()
                    .contentType(ContentType.JSON)
                    .log().all()
                    .body(request)
                    .post("/goals")
                    .then()
                    .log().all()
                    .extract();

            // then
            assertThat(response.statusCode()).isEqualTo(401);
        }

        @Test
        void 목표를_생성하면_201_상태코드와_함께_응답의_Location_헤더에_목표_Id를_담아준다() {
            // given
            GoalCreateRequest request = new GoalCreateRequest("목표", "#000000");

            // when
            ExtractableResponse<Response> response = RestAssured.given()
                    .contentType(ContentType.JSON)
                    .log().all()
                    .header(AUTHORIZATION, "Bearer " + member1Token)
                    .body(request)
                    .post("/goals")
                    .then()
                    .log().all()
                    .extract();

            // then
            assertThat(response.statusCode()).isEqualTo(201);
            String goalId = response.header("Location").replace("/goals/", "");
            assertThat(goalId).isNotNull();
        }
    }

    @Nested
    class 목표_수정_시 {

        private Long goalId;

        @BeforeEach
        void setUp() {
            GoalCreateRequest request = new GoalCreateRequest("목표", "#000000");
            goalId = Long.valueOf(RestAssured.given()
                    .contentType(ContentType.JSON)
                    .log().all()
                    .header(AUTHORIZATION, "Bearer " + member1Token)
                    .body(request)
                    .post("/goals")
                    .then()
                    .log().all()
                    .extract()
                    .header("Location")
                    .replace("/goals/", ""));
        }

        @Test
        void 내_목표가_아니면_403_예외() {
            // given
            GoalUpdateRequest request = new GoalUpdateRequest("update", "#111111");

            // when
            ExtractableResponse<Response> response = RestAssured.given()
                    .contentType(ContentType.JSON)
                    .log().all()
                    .header(AUTHORIZATION, "Bearer " + member2Token)
                    .body(request)
                    .put("/goals/{id}", goalId)
                    .then()
                    .log().all()
                    .extract();

            // then
            assertThat(response.statusCode()).isEqualTo(403);
            assertThat(response.as(ExceptionResponse.class).message())
                    .isEqualTo("해당 목표에 대한 권한이 없습니다.");
        }

        @Test
        void 목표를_수정한다() {
            // given
            GoalUpdateRequest request = new GoalUpdateRequest("update", "#111111");

            // when
            ExtractableResponse<Response> response = RestAssured.given()
                    .contentType(ContentType.JSON)
                    .log().all()
                    .header(AUTHORIZATION, "Bearer " + member1Token)
                    .body(request)
                    .put("/goals/{id}", goalId)
                    .then()
                    .log().all()
                    .extract();

            // then
            assertThat(response.statusCode()).isEqualTo(200);
        }
    }

    @Nested
    class 목표_삭제_시 {

        private Long goalId;

        @BeforeEach
        void setUp() {
            GoalCreateRequest request = new GoalCreateRequest("목표", "#000000");
            goalId = Long.valueOf(RestAssured.given()
                    .contentType(ContentType.JSON)
                    .log().all()
                    .header(AUTHORIZATION, "Bearer " + member1Token)
                    .body(request)
                    .post("/goals")
                    .then()
                    .log().all()
                    .extract()
                    .header("Location")
                    .replace("/goals/", ""));
        }

        @Test
        void 내_목표가_아니면_403_예외() {
            // when
            ExtractableResponse<Response> response = RestAssured.given()
                    .contentType(ContentType.JSON)
                    .log().all()
                    .header(AUTHORIZATION, "Bearer " + member2Token)
                    .delete("/goals/{id}", goalId)
                    .then()
                    .log().all()
                    .extract();

            // then
            assertThat(response.statusCode()).isEqualTo(403);
            assertThat(response.as(ExceptionResponse.class).message())
                    .isEqualTo("해당 목표에 대한 권한이 없습니다.");
        }

        @Test
        void 목표를_삭제한다() {
            // when
            ExtractableResponse<Response> response = RestAssured.given()
                    .contentType(ContentType.JSON)
                    .log().all()
                    .header(AUTHORIZATION, "Bearer " + member1Token)
                    .delete("/goals/{id}", goalId)
                    .then()
                    .log().all()
                    .extract();

            // then
            assertThat(response.statusCode()).isEqualTo(200);
        }
    }

    @Nested
    class 내_목표_조회_시 {


        @BeforeEach
        void setUp() {
            RestAssured.given()
                    .contentType(ContentType.JSON)
                    .log().all()
                    .header(AUTHORIZATION, "Bearer " + member1Token)
                    .body(new GoalCreateRequest("회원1 목표1", "#000000"))
                    .post("/goals")
                    .then()
                    .log().all();
            RestAssured.given()
                    .contentType(ContentType.JSON)
                    .log().all()
                    .header(AUTHORIZATION, "Bearer " + member1Token)
                    .body(new GoalCreateRequest("회원1 목표2", "#000000"))
                    .post("/goals")
                    .then()
                    .log().all();
            RestAssured.given()
                    .contentType(ContentType.JSON)
                    .log().all()
                    .header(AUTHORIZATION, "Bearer " + member2Token)
                    .body(new GoalCreateRequest("회원2 목표1", "#000000"))
                    .post("/goals")
                    .then()
                    .log().all();
        }

        @Test
        void 내_목표를_조회한다() {
            // when
            ExtractableResponse<Response> response = RestAssured.given()
                    .log().all()
                    .header(AUTHORIZATION, "Bearer " + member1Token)
                    .get("/goals/my")
                    .then()
                    .log().all()
                    .extract();

            // then
            List<GoalResponse> responses = response.as(new TypeRef<>() {
            });
            assertThat(responses)
                    .hasSize(2)
                    .extracting(GoalResponse::getName)
                    .containsExactlyInAnyOrder("회원1 목표1", "회원1 목표2");
        }
    }
}
