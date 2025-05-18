package docherri.controller;

import docherri.domain.Schedule;
import docherri.dto.ScheduleDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import docherri.service.ScheduleService;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/schedules")
public class ScheduleController {
    private final ScheduleService scheduleService;

    public ScheduleController(ScheduleService scheduleService) {
        this.scheduleService = scheduleService;
    }

    // 전체 일정 조회
    @GetMapping
    public ResponseEntity<?> getAllSchedules() {
        try {
            List<Schedule> schedules = scheduleService.getAllSchedules();
            return ResponseEntity.ok(schedules);
        } catch (Exception e) {
            return ResponseEntity.status(500).body("서버 오류 발생");
        }
    }

    // 일정 상세 조회
    @GetMapping("/{id}")
    public ResponseEntity<?> getSchedule(@PathVariable Long id) {
        try {
            Schedule schedule = scheduleService.getSchedule(id);
            return ResponseEntity.ok(schedule);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.status(500).body("서버 오류 발생");
        }
    }

    // 일정 추가
    @PostMapping
    public ResponseEntity<?> addSchedule(@RequestBody ScheduleDTO dto) {
        try {
            Schedule created = scheduleService.addSchedule(dto);
            return ResponseEntity.ok(created);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(500).body("서버 오류 발생");
        }
    }

    // 일정 수정
    @PutMapping("/{id}")
    public ResponseEntity<?> updateSchedule(@PathVariable Long id, @RequestBody ScheduleDTO dto) {
        try {
            Schedule updated = scheduleService.updateSchedule(id, dto);
            return ResponseEntity.ok(updated);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(500).body("서버 오류 발생");
        }
    }

    // 일정 삭제
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteSchedule(@PathVariable Long id) {
        try {
            scheduleService.deleteSchedule(id);
            return ResponseEntity.ok().build();
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.status(500).body("서버 오류 발생");
        }
    }

    // 스케쥴 검색
    // 요청 형식 : /search?keyword=검색어
    @GetMapping("/search")
    public ResponseEntity<?> searchSchedules(@RequestParam("keyword") String keyword) {
        try {
            List<Schedule> results = scheduleService.searchSchedules(keyword);
            return ResponseEntity.ok(results);
        } catch (Exception e) {
            return ResponseEntity.status(500).body("검색 중 서버 오류 발생");
        }
    }

    // 엑셀 업로드
    @PostMapping("/upload")
    public ResponseEntity<?> uploadExcel(@RequestParam("file") MultipartFile file) {
        try {
            scheduleService.uploadFromExcel(file);
            return ResponseEntity.ok("업로드 성공");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("엑셀 업로드 중 오류 발생: " + e.getMessage());
        }
    }

}

