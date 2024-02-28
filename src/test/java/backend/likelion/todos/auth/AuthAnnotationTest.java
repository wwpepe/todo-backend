package backend.likelion.todos.auth;

import static java.lang.annotation.ElementType.PARAMETER;
import static org.assertj.core.api.Assertions.assertThat;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;

@DisplayName("Auth 어노테이션은")
@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(ReplaceUnderscores.class)
class AuthAnnotationTest {

    @Test
    void 파라미터에_붙을_수_있다() {
        // given
        Target target = Auth.class.getDeclaredAnnotation(Target.class);

        // when
        ElementType elementType = target.value()[0];

        // then
        assertThat(elementType).isEqualTo(PARAMETER);
    }

    @Test
    void 런타임_시점까지_어노테이션이_유지된다() {
        // given
        Retention retention = Auth.class.getDeclaredAnnotation(Retention.class);

        // when
        RetentionPolicy policy = retention.value();

        // then
        assertThat(policy).isEqualTo(RetentionPolicy.RUNTIME);
    }
}
