package backend.likelion.todos.auth;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/test")
public class AuthTestController {

    @GetMapping("/auth")
    public String test(
            @Auth Long memberId
    ) {
        return "아이디가 " + memberId + "인 회원 인증 성공!";
    }
}
