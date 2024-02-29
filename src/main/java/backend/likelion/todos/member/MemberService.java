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

    // 회원 가입을 처리하고, 저장된 회원의 ID를 반환합니다.
    public Long signup(String username, String password, String nickname, String profileImageUrl) {
        // TODO [1단계] username으로 기존 회원이 있는지 확인하고, 있으면 ConflictException을 발생시키세요.
        // TODO [1단계] Member 인스턴스를 생성하세요.
        // TODO [1단계] 생성된 Member 인스턴스를 memberRepository에 저장하고, 저장된 멤버의 ID를 반환하세요.
        return null;
    }

    // 로그인을 처리하고, 로그인한 회원의 ID를 반환합니다.
    public Long login(String username, String password) {
        // TODO [1단계] username으로 회원을 찾아오고, 없으면 UnAuthorizedException을 발생시키세요.
        // TODO [1단계] 찾아온 Member 인스턴스에 대해 password가 일치하는지 확인하세요.
        // TODO [1단계] 로그인이 성공적이면, 해당 회원의 ID를 반환하세요.
        return null;
    }

    // 회원 ID로 회원 정보를 조회하고, 그 결과를 MemberResponse로 반환합니다.
    public MemberResponse findById(Long memberId) {
        // TODO [1단계] memberId로 회원 정보를 찾아오고, 없으면 NotFoundException을 발생시키세요.
        // TODO [1단계] 찾아온 Member 인스턴스로부터 MemberResponse 객체를 생성하여 반환하세요.
        return null;
    }

}
