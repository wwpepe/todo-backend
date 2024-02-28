package backend.likelion.todos.member;

import static org.springframework.http.HttpStatus.CREATED;

import backend.likelion.todos.member.request.SignupRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/members")
public class MemberController {

    private final MemberService memberService;

    @ResponseStatus(CREATED)
    @PostMapping
    public void signup(@RequestBody SignupRequest request) {
        memberService.signup(
                request.getUsername(),
                request.getPassword(),
                request.getNickname(),
                request.getProfileImageUrl()
        );
    }
}
