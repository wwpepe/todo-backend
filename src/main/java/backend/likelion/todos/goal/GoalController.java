package backend.likelion.todos.goal;

import backend.likelion.todos.auth.Auth;
import java.net.URI;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RequestMapping("/goals")
@RestController
public class GoalController {

    private final GoalService goalService;

    @PostMapping
    public ResponseEntity<Void> create(
            @Auth Long memberId,
            @RequestBody GoalCreateRequest request
    ) {
        Long goalId = goalService.save(request.name(), request.color(), memberId);
        return ResponseEntity.created(URI.create("/goals/" + goalId)).build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> update(
            @PathVariable("id") Long id,
            @Auth Long memberId,
            @RequestBody GoalUpdateRequest request
    ) {
        goalService.update(id, request.name(), request.color(), memberId);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(
            @PathVariable("id") Long id,
            @Auth Long memberId
    ) {
        goalService.delete(id, memberId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/my")
    public ResponseEntity<List<GoalResponse>> findMine(
            @Auth Long memberId
    ) {
        List<GoalResponse> result = goalService.findAllByMemberId(memberId);
        return ResponseEntity.ok(result);
    }
}
