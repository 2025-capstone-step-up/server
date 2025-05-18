package docherri.controller;

import docherri.domain.Schedule;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import docherri.service.UserService;

import java.util.Set;
import jakarta.persistence.EntityNotFoundException;

//이거 별표 관련 코드들이라 (유저-> 별표한 거 보여주기) 신경 x ---- 사용안함
@RestController
@RequestMapping("/users")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    // 별표 추가 (즐겨찾기)
    @PostMapping("/{userId}/stars/{scheduleId}")
    public ResponseEntity<String> addStar(@PathVariable Long userId, @PathVariable Long scheduleId) {
        try {
            userService.addStar(userId, scheduleId);
            return ResponseEntity.ok("별표 추가 성공!");
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build(); // 404
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage()); // 400
        } catch (Exception e) {
            return ResponseEntity.status(500).body("서버 오류 발생!");
        }
    }

    // 별표 삭제 (즐겨찾기 해제)
    @DeleteMapping("/{userId}/stars/{scheduleId}")
    public ResponseEntity<String> removeStar(@PathVariable Long userId, @PathVariable Long scheduleId) {
        try {
            userService.removeStar(userId, scheduleId);
            return ResponseEntity.ok("별표 해제 성공!");
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build(); // 404
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage()); // 400
        } catch (Exception e) {
            return ResponseEntity.status(500).body("서버 오류 발생!");
        }
    }

    // 내가 별표한 일정 목록 조회
    @GetMapping("/{userId}/stars")
    public ResponseEntity<?> getStarredSchedules(@PathVariable Long userId) {
        try {
            Set<Schedule> starredSchedules = userService.getStarredSchedules(userId);
            return ResponseEntity.ok(starredSchedules);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build(); // 404
        } catch (Exception e) {
            return ResponseEntity.status(500).body("서버 오류 발생!");
        }
    }
}
