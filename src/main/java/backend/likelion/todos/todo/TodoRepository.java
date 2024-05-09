package backend.likelion.todos.todo;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface TodoRepository extends JpaRepository<Todo, Long> {

    @Query("""
    SELECT t
    FROM Todo t
    JOIN t.goal g
    ON t.goal = g
    WHERE g.member.id = :memberId
    AND YEAR(t.date) = :year
    AND MONTH(t.date) = :month
    ORDER BY DAY(t.date) ASC
    """)
    List<Todo> findAllByMemberIdAndDateOrderByDayAsc(Long memberId, int year, int month);
}
