package backend.likelion.todos;

import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

import backend.likelion.todos.member.domain.MemberRepository;
import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;

@SpringBootTest(webEnvironment = RANDOM_PORT)
public abstract class ApiTest {

    @LocalServerPort
    private int port;

    @Autowired
    private MemberRepository memberRepository;

    @BeforeEach
    protected void setUp() {
        RestAssured.port = port;
        memberRepository.clear();
    }
}
