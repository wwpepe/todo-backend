package backend.likelion.todos.member;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import org.springframework.stereotype.Repository;

@Repository
public class MemberRepository {

    private final Map<Long, Member> members = new HashMap<>();
    private Long id = 1L;

    public Member save(Member member) {
        member.setId(id);
        members.put(id++, member);
        return member;
    }

    public Optional<Member> findById(Long id) {
        return Optional.ofNullable(members.get(id));
    }

    public Optional<Member> findByUsername(String username) {
        return members.values()
                .stream()
                .filter(it -> it.getUsername().equals(username))
                .findAny();
    }

    public void clear() {
        members.clear();
    }
}
