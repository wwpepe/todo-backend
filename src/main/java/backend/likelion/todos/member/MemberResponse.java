package backend.likelion.todos.member;

import lombok.Getter;

@Getter
public class MemberResponse {

    private final Long memberId;
    private final String username;
    private final String nickname;
    private final String profileImageUrl;

    public MemberResponse(Long memberId, String username, String nickname, String profileImageUrl) {
        this.memberId = memberId;
        this.username = username;
        this.nickname = nickname;
        this.profileImageUrl = profileImageUrl;
    }
}
