package backend.likelion.todos.member;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.stereotype.Repository;

@DisplayName("회원 저장소 (MemberRepository) 은(는)")
@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(ReplaceUnderscores.class)
class MemberRepositoryTest {

    private final MemberRepository memberRepository = new MemberRepository();

    @Test
    void Repository_빈으로_등록한다() {
        // when
        boolean isRepository = MemberRepository.class.isAnnotationPresent(Repository.class);

        // then
        assertThat(isRepository).isTrue();
    }

    @Test
    void 회원_객체에_Id를_세팅하고_저장한다() {
        // given
        Member member = new Member("u", "p", "n", "p");

        // when
        Member saved = memberRepository.save(member);

        // then
        assertThat(saved.getId()).isEqualTo(1L);
    }

    @Nested
    class id로_회원_조회_시 {

        @Test
        void 존재하면_회원_객체가_Optional로_래핑되어_반환된다() {
            // given
            Member member = new Member("u", "p", "n", "p");
            Member saved = memberRepository.save(member);

            // when
            Optional<Member> found = memberRepository.findById(saved.getId());

            // then
            assertThat(found).isPresent();
            assertThat(found.get().getUsername()).isEqualTo("u");
        }

        @Test
        void 존재하지_않으면_Optional_empty가_반환된다() {
            // when
            Optional<Member> found = memberRepository.findById(100L);

            // then
            assertThat(found).isEmpty();
        }
    }

    @Nested
    class username_으로_회원_조회_시 {

        @Test
        void 존재하면_회원_객체가_Optional로_래핑되어_반환된다() {
            // given
            Member member = new Member("u", "p", "n", "p");
            Member saved = memberRepository.save(member);

            // when
            Optional<Member> found = memberRepository.findByUsername("u");

            // then
            assertThat(found).isPresent();
            assertThat(found.get().getUsername()).isEqualTo("u");
        }

        @Test
        void 존재하지_않으면_Optional_empty가_반환된다() {
            // when
            Optional<Member> found = memberRepository.findByUsername("u");

            // then
            assertThat(found).isEmpty();
        }
    }

    @Test
    void clear_시_데이터를_다_지운다() {
        // given
        Member member = new Member("u", "p", "n", "p");
        memberRepository.save(member);

        // when
        memberRepository.clear();

        // then
        assertThat(memberRepository.findById(member.getId())).isEmpty();
    }
}
