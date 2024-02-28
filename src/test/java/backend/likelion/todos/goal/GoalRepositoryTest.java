package backend.likelion.todos.goal;

import static org.assertj.core.api.Assertions.assertThat;

import backend.likelion.todos.member.Member;
import backend.likelion.todos.member.MemberRepository;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;

@DisplayName("목표 저장소 (GoalRepository) 은(는)")
@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(ReplaceUnderscores.class)
class GoalRepositoryTest {

    private final GoalRepository goalRepository = new GoalRepository();
    private final MemberRepository memberRepository = new MemberRepository();
    private final Member member1 = new Member("u", "p", "n", "p");
    private final Member member2 = new Member("u", "p", "n", "p");

    @BeforeEach
    void setUp() {
        memberRepository.save(member1);
        memberRepository.save(member2);
    }

    @Test
    void 목표를_제거한다() {
        // given
        Goal goal = goalRepository.save(new Goal("n", "c", member1));

        // when
        goalRepository.delete(goal);

        // then
        assertThat(goalRepository.findById(goal.getId())).isEmpty();
    }

    @Test
    void 특정_회원의_목표를_모두_조회한다() {
        // given
        Goal goal1 = goalRepository.save(new Goal("n1", "c", member1));
        Goal goal2 = goalRepository.save(new Goal("n2", "c", member2));
        Goal goal3 = goalRepository.save(new Goal("n3", "c", member1));
        Goal goal4 = goalRepository.save(new Goal("n4", "c", member2));

        // when
        List<Goal> goals = goalRepository.findAllByMemberId(member1.getId());

        // then
        assertThat(goals)
                .hasSize(2)
                .extracting(Goal::getName)
                .containsExactlyInAnyOrder("n1", "n3");
    }
}
