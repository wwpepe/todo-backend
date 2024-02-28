package backend.likelion.todos.member;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import backend.likelion.todos.common.ConflictException;
import backend.likelion.todos.common.NotFoundException;
import backend.likelion.todos.common.UnAuthorizedException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Parameter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.stereotype.Service;

@SpringBootTest
@DisplayName("회원 서비스 (MemberService) 은(는)")
@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(ReplaceUnderscores.class)
class MemberServiceTest {

    @Autowired
    private MemberService memberService;

    @Autowired
    private MemberRepository memberRepository;

    @BeforeEach
    void setUp() {
        memberRepository.clear();
    }

    @Test
    void Service_빈으로_등록한다() {
        // when
        boolean isService = MemberService.class.isAnnotationPresent(Service.class);

        // then
        assertThat(isService).isTrue();
    }

    @Test
    void MemberRepository_를_받는_생성자_단_하나만이_존재한다() {
        // when
        Constructor<?>[] declaredConstructors = MemberService.class.getDeclaredConstructors();

        // then
        assertThat(declaredConstructors).hasSize(1);
        Constructor<?> ctor = declaredConstructors[0];
        assertThat(ctor.getParameters())
                .hasSize(1)
                .extracting(Parameter::getType)
                .containsOnly((Class) MemberRepository.class);
    }

    @Nested
    class 회원가입_시 {

        @Test
        void 아이디가_중복되면_예외() {
            // given
            memberService.signup("u", "p", "n", "p");

            // when & then
            assertThatThrownBy(() -> {
                memberService.signup("u", "p", "n", "p");
            }).isInstanceOf(ConflictException.class)
                    .hasMessage("해당 아이디로 이미 가입한 회원이 있습니다");
        }

        @Test
        void 중복되는_아이디가_없으면_회원가입에_성공한다() {
            // when
            Long memberId = memberService.signup("u", "p", "n", "p");

            // then
            assertThat(memberId).isNotNull();
        }
    }

    @Nested
    class 로그인_시 {

        @Test
        void 아이디가_없으면_로그인_실패() {
            // when & then
            assertThatThrownBy(() -> {
                memberService.login("u", "p");
            }).isInstanceOf(UnAuthorizedException.class)
                    .hasMessage("존재하지 않는 아이디입니다.");
        }

        @Test
        void 비밀번호가_다르면_로그인_실패() {
            // given
            memberService.signup("u", "p", "n", "p");

            // when & then
            assertThatThrownBy(() -> {
                memberService.login("u", "1");
            }).isInstanceOf(UnAuthorizedException.class)
                    .hasMessage("비밀번호가 일치하지 않습니다");
        }

        @Test
        void 아이디와_비밀번호가_올바르면_로그인_성공() {
            // given
            Long memberId = memberService.signup("u", "p", "n", "p");

            // when
            Long loginId = memberService.login("u", "p");

            // then
            assertThat(memberId).isEqualTo(loginId);
        }
    }

    @Nested
    class 회원_정보_조회_시 {

        private Long memberId;

        @BeforeEach
        void setUp() {
            memberId = memberService.signup("u", "p", "n", "p");
        }

        @Test
        void Id_로_회원_정보를_조회한다() {
            // when
            MemberResponse response = memberService.findById(memberId);

            // then
            assertThat(response.getMemberId()).isEqualTo(memberId);
            assertThat(response.getUsername()).isEqualTo("u");
            assertThat(response.getNickname()).isEqualTo("n");
            assertThat(response.getProfileImageUrl()).isEqualTo("p");
        }

        @Test
        void 회원_정보가_없으면_예외() {
            // when & then
            assertThatThrownBy(() -> {
                memberService.findById(100L);
            }).isInstanceOf(NotFoundException.class)
                    .hasMessage("회원 정보가 없습니다.");
        }
    }
}
