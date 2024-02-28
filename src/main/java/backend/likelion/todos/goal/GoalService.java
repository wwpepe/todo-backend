package backend.likelion.todos.goal;

import backend.likelion.todos.common.NotFoundException;
import backend.likelion.todos.member.Member;
import backend.likelion.todos.member.MemberRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GoalService {

    private final MemberRepository memberRepository;
    private final GoalRepository goalRepository;

    public Long save(String name, String color, Long memberId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new NotFoundException("회원 정보가 없습니다."));
        Goal goal = new Goal(name, color, member);
        return goalRepository.save(goal)
                .getId();
    }

    public void update(Long goalId, String name, String color, Long memberId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new NotFoundException("회원 정보가 없습니다."));
        Goal goal = goalRepository.findById(goalId)
                .orElseThrow(() -> new NotFoundException("목표 정보가 없습니다."));
        goal.validateMember(member);
        goal.update(name, color);
    }

    public void delete(Long goalId, Long memberId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new NotFoundException("회원 정보가 없습니다."));
        Goal goal = goalRepository.findById(goalId)
                .orElseThrow(() -> new NotFoundException("목표 정보가 없습니다."));
        goal.validateMember(member);
        goalRepository.delete(goal);
    }

    public List<GoalResponse> findAllByMemberId(Long memberId) {
        List<Goal> goals = goalRepository.findAllByMemberId(memberId);
        return goals.stream()
                .map(it -> new GoalResponse(
                        it.getId(),
                        it.getName(),
                        it.getColor()
                ))
                .toList();
    }
}
