package backend.likelion.todos.auth;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

import backend.likelion.todos.auth.jwt.JwtService;
import backend.likelion.todos.common.UnAuthorizedException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

@DisplayName("AuthArgumentResolver 은(는)")
@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(ReplaceUnderscores.class)
class AuthArgumentResolverTest {

    @Test
    void HandlerMethodArgumentResolver_를_구현한다() {
        // when
        Class<?>[] interfaces = AuthArgumentResolver.class.getInterfaces();

        // then
        assertThat(interfaces).containsOnly(HandlerMethodArgumentResolver.class);
    }

    @Nested
    class 파라미터_지원여부_확인_시 {

        @Test
        void 파라미터가_Long타입이며_Auth_어노테이션이_붙어있는_경우에만_지원한다() {
            // given
            AuthArgumentResolver resolver = new AuthArgumentResolver(mock(JwtService.class));
            MethodParameter methodParameter = mock(MethodParameter.class);
            given(methodParameter.hasParameterAnnotation(Auth.class))
                    .willReturn(true);
            given(methodParameter.getParameterType())
                    .willReturn((Class) Long.class);

            // when
            boolean isSupport = resolver.supportsParameter(methodParameter);

            // then
            assertThat(isSupport).isTrue();
        }

        @Test
        void 파라미터가_Long타입이_아니면_지원하지_않는다() {
            // given
            AuthArgumentResolver resolver = new AuthArgumentResolver(mock(JwtService.class));
            MethodParameter methodParameter = mock(MethodParameter.class);
            given(methodParameter.hasParameterAnnotation(Auth.class))
                    .willReturn(true);
            given(methodParameter.getParameterType())
                    .willReturn((Class) String.class);

            // when
            boolean isSupport = resolver.supportsParameter(methodParameter);

            // then
            assertThat(isSupport).isFalse();
        }

        @Test
        void 파라미터에_Auth_어노테이션이_없으면_지원하지_않는다() {
            // given
            AuthArgumentResolver resolver = new AuthArgumentResolver(mock(JwtService.class));
            MethodParameter methodParameter = mock(MethodParameter.class);
            given(methodParameter.hasParameterAnnotation(Auth.class))
                    .willReturn(false);
            given(methodParameter.getParameterType())
                    .willReturn((Class) Long.class);

            // when
            boolean isSupport = resolver.supportsParameter(methodParameter);

            // then
            assertThat(isSupport).isFalse();
        }
    }

    @Nested
    class 파라미터_resolve_시 {

        private final JwtService jwtService = mock(JwtService.class);
        private final AuthArgumentResolver resolver = new AuthArgumentResolver(jwtService);
        private final MethodParameter parameter = mock(MethodParameter.class);
        private final ModelAndViewContainer mavContainer = mock(ModelAndViewContainer.class);
        private final NativeWebRequest webRequest = mock(NativeWebRequest.class);
        private final WebDataBinderFactory binderFactor = mock(WebDataBinderFactory.class);

        @Test
        void request_의_Authorization_헤더가_없으면_예외() {
            // given
            given(webRequest.getHeader("Authorization"))
                    .willReturn(null);

            // when & then
            assertThatThrownBy(() -> {
                resolver.resolveArgument(parameter, mavContainer, webRequest, binderFactor);
            }).isInstanceOf(UnAuthorizedException.class)
                    .hasMessage("로그인 후 접근할 수 있습니다.");
        }

        @Test
        void request_의_Authorization_헤더의_값이_Bearer_토큰_형태가_아니면_예외() {
            // given
            given(webRequest.getHeader("Authorization"))
                    .willReturn("jwt");

            // when & then
            assertThatThrownBy(() -> {
                resolver.resolveArgument(parameter, mavContainer, webRequest, binderFactor);
            }).isInstanceOf(UnAuthorizedException.class)
                    .hasMessage("로그인 후 접근할 수 있습니다.");
        }

        @Test
        void request_의_Authorization_헤더의_Bearer_토큰이_유효하지_않으면_예외() {
            // given
            given(webRequest.getHeader("Authorization"))
                    .willReturn("Bearer invalid");
            given(jwtService.extractMemberId("invalid"))
                    .willThrow(new UnAuthorizedException("유효하지 않은 토큰입니다."));

            // when & then
            assertThatThrownBy(() -> {
                resolver.resolveArgument(parameter, mavContainer, webRequest, binderFactor);
            }).isInstanceOf(UnAuthorizedException.class)
                    .hasMessage("유효하지 않은 토큰입니다.");
        }

        @Test
        void request_의_Authorization_헤더의_Bearer_토큰이_유효하다면_해당_토큰에서_회원_id를_꺼내어_세팅한다() {
            // given
            given(webRequest.getHeader("Authorization"))
                    .willReturn("Bearer valid");
            given(jwtService.extractMemberId("valid"))
                    .willReturn(1L);

            // when
            Object result = resolver.resolveArgument(parameter, mavContainer, webRequest, binderFactor);

            // then
            assertThat(result).isEqualTo(1L);
        }
    }
}

