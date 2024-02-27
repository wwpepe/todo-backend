package backend.likelion.todos.member;

import backend.likelion.todos.common.ConflictException;
import backend.likelion.todos.common.NotFoundException;
import backend.likelion.todos.common.UnAuthorizedException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;

    public Long signup(String username, String password, String nickname, String profileImageUrl) {
        memberRepository.findByUsername(username)
                .ifPresent(it -> {
                    throw new ConflictException("해당 아이디로 이미 가입한 회원이 있습니다");
                });
        Member member = new Member(username, password, nickname, profileImageUrl);
        return memberRepository.save(member)
                .getId();
    }

    public Long login(String username, String password) {
        Member member = memberRepository.findByUsername(username)
                .orElseThrow(() -> new UnAuthorizedException("존재하지 않는 아이디입니다."));
        member.login(password);
        return member.getId();
    }

    public MemberResponse findById(Long memberId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new NotFoundException("회원 정보가 없습니다."));
        return new MemberResponse(
                member.getId(),
                member.getUsername(),
                member.getNickname(),
                member.getProfileImageUrl()
        );
    }
}
