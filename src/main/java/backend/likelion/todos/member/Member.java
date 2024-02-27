package backend.likelion.todos.member;

import backend.likelion.todos.common.UnAuthorizedException;
import lombok.Getter;

@Getter
public class Member {

    private Long id;
    private String username;
    private String password;
    private String nickname;
    private String profileImageUrl;

    public Member(String username, String password, String nickname, String profileImageUrl) {
        this.username = username;
        this.password = password;
        this.nickname = nickname;
        this.profileImageUrl = profileImageUrl;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void login(String password) {
        if (this.password.equals(password)) {
            return;
        }
        throw new UnAuthorizedException("비밀번호가 일치하지 않습니다");
    }
}
