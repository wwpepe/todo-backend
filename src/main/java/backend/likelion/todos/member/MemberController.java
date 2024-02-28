package backend.likelion.todos.member;

import static org.springframework.http.HttpStatus.CREATED;

import backend.likelion.todos.auth.Auth;
import backend.likelion.todos.auth.jwt.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
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
    private final JwtService jwtService;

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

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(
            @RequestBody LoginRequest request
    ) {
        Long memberId = memberService.login(request.username(), request.password());
        String accessToken = jwtService.createToken(memberId);
        return ResponseEntity.ok(new LoginResponse(accessToken));
    }

    @GetMapping("/my")
    public ResponseEntity<MemberResponse> getProfile(
            @Auth Long memberId
    ) {
        MemberResponse response = memberService.findById(memberId);
        return ResponseEntity.ok(response);
    }
}
