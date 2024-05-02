package backend.likelion.todos.todo;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface TodoRepository extends JpaRepository<Todo, Long> {

    @Query("SELECT t FROM Todo t JOIN t.goal g WHERE g.member.id = :memberId AND t.date = :localDate")
    List<Todo> findAllByMemberIdAndDate(Long memberId, LocalDate localDate);
}
