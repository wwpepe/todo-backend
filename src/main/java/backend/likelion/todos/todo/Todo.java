package backend.likelion.todos.todo;

import backend.likelion.todos.common.ForbiddenException;
import backend.likelion.todos.goal.Goal;
import backend.likelion.todos.member.Member;
import java.time.LocalDate;
import lombok.Getter;

@Getter
public class Todo {

    private Long id;
    private String content;
    private LocalDate date;
    private Goal goal;
    private boolean isCompleted;

    public Todo(String content, LocalDate date, Goal goal) {
        this.content = content;
        this.date = date;
        this.goal = goal;
        this.isCompleted = false;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void validateMember(Member member) {
        if (!this.goal.getMember().equals(member)) {
            throw new ForbiddenException("해당 투두에 대한 권한이 없습니다.");
        }
    }

    public void update(String content, LocalDate date) {
        this.content = content;
        this.date = date;
    }

    public void check() {
        this.isCompleted = true;
    }

    public void uncheck() {
        this.isCompleted = false;
    }
}
