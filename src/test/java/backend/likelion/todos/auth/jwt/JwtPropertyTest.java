package backend.likelion.todos.auth.jwt;

import static java.nio.charset.StandardCharsets.UTF_8;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.Base64;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@DisplayName("Jwt 설정 (JwtProperty) 은(는)")
@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(ReplaceUnderscores.class)
class JwtPropertyTest {

    @Autowired
    private JwtProperty jwtProperty;


    @Test
    void application_yml_파일_내의_jwt_하위의_속성과_매핑된다() {
        // when
        ConfigurationProperties configurationProperties = JwtProperty.class
                .getDeclaredAnnotation(ConfigurationProperties.class);

        // then
        assertThat(configurationProperties.value()).isEqualTo("jwt");
    }

    @Test
    @DisplayName("해당 속성의 시크릿 키는 CNU-LIKE-LION-APPLICATION-SECRET-KEY 를 base 64로 인코딩한 값이다.")
    void secretKeyTest() {
        // given
        String origin = "CNU-LIKE-LION-APPLICATION-SECRET-KEY";
        String expected = Base64.getEncoder().encodeToString(origin.getBytes(UTF_8));

        // when
        String actual = jwtProperty.secretKey();

        // then
        assertThat(actual).isEqualTo(expected);
    }
}
