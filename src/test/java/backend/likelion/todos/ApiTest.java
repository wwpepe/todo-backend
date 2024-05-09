package backend.likelion.todos;

import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

import backend.likelion.todos.goal.GoalRepository;
import backend.likelion.todos.member.MemberRepository;
import backend.likelion.todos.todo.TodoRepository;
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

    @Autowired
    private GoalRepository goalRepository;

    @Autowired
    private TodoRepository todoRepository;

    @BeforeEach
    protected void setUp() {
        RestAssured.port = port;
        todoRepository.deleteAll();
        goalRepository.deleteAll();
        memberRepository.deleteAll();
    }
}
