package docherri.controller;

import docherri.domain.CompetitionSchedule;
import docherri.dto.CompetitionScheduleDTO;
import docherri.service.CompetitionScheduleService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/competitions")
public class CompetitionScheduleController {

    private final CompetitionScheduleService competitionScheduleService;

    public CompetitionScheduleController(CompetitionScheduleService competitionScheduleService) {
        this.competitionScheduleService = competitionScheduleService;
    }

    // 전체 조회
    @GetMapping
    public ResponseEntity<?> getAllCompetitionSchedules() {
        try {
            List<CompetitionSchedule> list = competitionScheduleService.getAllSchedules();
            return ResponseEntity.ok(list);
        } catch (Exception e) {
            return ResponseEntity.status(500).body("서버 오류 발생");
        }
    }

    // 단건 조회
    @GetMapping("/{id}")
    public ResponseEntity<?> getCompetitionSchedule(@PathVariable Long id) {
        try {
            CompetitionSchedule schedule = competitionScheduleService.getSchedule(id);
            return ResponseEntity.ok(schedule);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.status(500).body("서버 오류 발생");
        }
    }

    // 추가
    @PostMapping
    public ResponseEntity<?> addCompetitionSchedule(@RequestBody CompetitionScheduleDTO dto) {
        try {
            CompetitionSchedule created = competitionScheduleService.addSchedule(dto);
            return ResponseEntity.ok(created);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(500).body("서버 오류 발생");
        }
    }

    // 수정
    @PutMapping("/{id}")
    public ResponseEntity<?> updateCompetitionSchedule(@PathVariable Long id, @RequestBody CompetitionScheduleDTO dto) {
        try {
            CompetitionSchedule updated = competitionScheduleService.updateSchedule(id, dto);
            return ResponseEntity.ok(updated);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(500).body("서버 오류 발생");
        }
    }

    // 삭제
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteCompetitionSchedule(@PathVariable Long id) {
        try {
            competitionScheduleService.deleteSchedule(id);
            return ResponseEntity.ok().build();
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.status(500).body("서버 오류 발생");
        }
    }

    // 검색
    // 요청 형식: /competitions/search?keyword=검색어
    @GetMapping("/search")
    public ResponseEntity<?> searchCompetitionSchedules(@RequestParam("keyword") String keyword) {
        try {
            List<CompetitionSchedule> results = competitionScheduleService.searchSchedules(keyword);
            return ResponseEntity.ok(results);
        } catch (Exception e) {
            return ResponseEntity.status(500).body("검색 중 오류 발생");
        }
    }

    // 엑셀 업로드
    @PostMapping("/upload")
    public ResponseEntity<?> uploadExcel(@RequestParam("file") MultipartFile file) {
        try {
            competitionScheduleService.uploadFromExcel(file);
            return ResponseEntity.ok("업로드 성공");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("엑셀 업로드 중 오류 발생: " + e.getMessage());
        }
    }
}
