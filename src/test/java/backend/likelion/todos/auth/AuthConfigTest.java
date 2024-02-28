package backend.likelion.todos.auth;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@DisplayName("AuthConfig 은(는)")
@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(ReplaceUnderscores.class)
class AuthConfigTest {

    @Test
    void WebMvcConfigurer_를_구현한다() {
        // when
        Class<?> anInterface = AuthConfig.class.getInterfaces()[0];

        // then
        assertThat(anInterface).isEqualTo(WebMvcConfigurer.class);
    }

    @Test
    void addArgumentResolvers에_AuthArgumentResolver_를_등록한다() {
        // given
        AuthArgumentResolver resolver = mock(AuthArgumentResolver.class);
        AuthConfig authConfig = new AuthConfig(resolver);
        List<HandlerMethodArgumentResolver> resolvers = new ArrayList<>();

        // when
        authConfig.addArgumentResolvers(resolvers);

        // then
        assertThat(resolvers)
                .hasSize(1)
                .containsExactly(resolver);
    }
}
