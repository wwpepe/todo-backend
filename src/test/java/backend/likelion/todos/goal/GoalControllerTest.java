package backend.likelion.todos.goal;

import static org.assertj.core.api.Assertions.assertThat;

import backend.likelion.todos.auth.Auth;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.Arrays;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@DisplayName("목표 컨트롤러 (GoalController) 은(는)")
@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(ReplaceUnderscores.class)
class GoalControllerTest {

    @Test
    void RestController_빈으로_등록한다() {
        // when
        boolean isRestController = GoalController.class.isAnnotationPresent(RestController.class);

        // then
        assertThat(isRestController).isTrue();
    }

    @Test
    void goals_로_들어오는_요청을_처리한다() {
        // given
        RequestMapping requestMapping = GoalController.class.getAnnotation(RequestMapping.class);

        // when
        String uri = requestMapping.value()[0];

        // then
        assertThat(uri).isEqualTo("/goals");
    }

    @Test
    @DisplayName("생성 시 회원 정보가 필요하므로 @Auth 를 통해 회원 Id를 받아와야 한다.")
    void 생성_메서드_테스트() throws NoSuchMethodException {
        // given
        Method create = Arrays.stream(GoalController.class.getDeclaredMethods())
                .filter(it -> it.getName().equals("create"))
                .findAny().get();

        // when
        Optional<Parameter> authParam = Arrays.stream(create.getParameters())
                .filter(it -> it.isAnnotationPresent(Auth.class))
                .findAny();

        // then
        assertThat(authParam).isPresent();
    }

    @Test
    @DisplayName("수정 메서드는 PUT /goals/{id} 형태이며, id는 PathVariable 로 받아온다")
    void 수정_메서드_테스트() {
        // given
        Method update = Arrays.stream(GoalController.class.getDeclaredMethods())
                .filter(it -> it.getName().equals("update"))
                .findAny().get();

        // when
        PutMapping putMapping = update.getDeclaredAnnotation(PutMapping.class);
        Optional<Parameter> pathVariableParam = Arrays.stream(update.getParameters())
                .filter(it -> it.isAnnotationPresent(PathVariable.class))
                .findAny();

        // then
        assertThat(putMapping.value()).containsOnly("/{id}");
        assertThat(pathVariableParam).isPresent();
        Parameter parameter = pathVariableParam.get();
        PathVariable pathVariable = parameter.getDeclaredAnnotation(PathVariable.class);
        assertThat(pathVariable.value()).isEqualTo("id");
    }
}
