package docherri.service;

import docherri.domain.Schedule;
import docherri.dto.ScheduleDTO;
import org.apache.poi.ss.usermodel.*;
import org.springframework.stereotype.Service;
import docherri.repository.ScheduleRepository;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
public class ScheduleService {
    private final ScheduleRepository scheduleRepository;

    public ScheduleService(ScheduleRepository scheduleRepository) {
        this.scheduleRepository = scheduleRepository;
    }

    // 전체 일정 조회
    public List<Schedule> getAllSchedules() {
        return scheduleRepository.findAll();
    }

    // 일정 상세 조회
    public Schedule getSchedule(Long scheduleId) {
        return scheduleRepository.findById(scheduleId)
                .orElseThrow(() -> new EntityNotFoundException("일정을 찾을 수 없습니다."));
    }

    // 일정 추가
    public Schedule addSchedule(ScheduleDTO addedSchedule) {
        return scheduleRepository.save(convertFromDTO(addedSchedule));
    }


    // 일정 수정
    public Schedule updateSchedule(Long scheduleId, ScheduleDTO updated) {
        Schedule existing = scheduleRepository.findById(scheduleId)
                .orElseThrow(() -> new EntityNotFoundException("일정을 찾을 수 없습니다."));

        Schedule updatedEntity = convertFromDTO(updated);
        updatedEntity.setId(existing.getId());  // 기존 ID 유지

        return scheduleRepository.save(updatedEntity);
    }


    // 일정 삭제
    public void deleteSchedule(Long scheduleId) {
        if (!scheduleRepository.existsById(scheduleId)) {
            throw new EntityNotFoundException("일정을 찾을 수 없습니다.");
        }
        scheduleRepository.deleteById(scheduleId);
    }

    // 스케쥴검색
    public List<Schedule> searchSchedules(String keyword) {
        return scheduleRepository.findByTitleContainingIgnoreCase(keyword);
    }


    private Schedule convertFromDTO(ScheduleDTO dto) {
        Schedule schedule = new Schedule();

        schedule.setTitle(dto.getTitle());
        /*schedule.setDescription(dto.getDescription());
        schedule.setStartDate(dto.getStartDate());
        schedule.setEndDate(dto.getEndDate()); 테스트코드*/

        schedule.setRegistrationStart(dto.getRegistrationStart());
        schedule.setRegistrationEnd(dto.getRegistrationEnd());
        schedule.setExamDate(dto.getExamDate());
        schedule.setResultDate(dto.getResultDate());
        schedule.setFee1(dto.getFee1());
        schedule.setFee2(dto.getFee2());

        schedule.setWrittenApplyStart(dto.getWrittenApplyStart());
        schedule.setWrittenApplyEnd(dto.getWrittenApplyEnd());
        schedule.setWrittenExamStart(dto.getWrittenExamStart());
        schedule.setWrittenExamEnd(dto.getWrittenExamEnd());
        schedule.setWrittenResultDate(dto.getWrittenResultDate());

        schedule.setPracticalApplyStart(dto.getPracticalApplyStart());
        schedule.setPracticalApplyEnd(dto.getPracticalApplyEnd());
        schedule.setPracticalExamStart(dto.getPracticalExamStart());
        schedule.setPracticalExamEnd(dto.getPracticalExamEnd());
        schedule.setFinalResultDate(dto.getFinalResultDate());

        schedule.setWrittenFee(dto.getWrittenFee());
        schedule.setPracticalFee(dto.getPracticalFee());

        return schedule;
    }

    // 엑셀 업로드
    public void uploadFromExcel(MultipartFile file) throws IOException {
        try (InputStream is = file.getInputStream()) {
            Workbook workbook = WorkbookFactory.create(is);
            Sheet sheet = workbook.getSheetAt(0);

            for (int i = 1; i <= sheet.getLastRowNum(); i++) {
                Row row = sheet.getRow(i);
                if (row == null) continue;

                Schedule schedule = new Schedule();
                schedule.setTitle(getString(row, 0));
                schedule.setRegistrationStart(getDate(row, 1));
                schedule.setRegistrationEnd(getDate(row, 2));
                schedule.setExamDate(getDate(row, 3));
                schedule.setResultDate(getDate(row, 4));
                schedule.setFee1(getString(row, 5));
                schedule.setFee2(getString(row, 6));
                schedule.setWrittenApplyStart(getDate(row, 7));
                schedule.setWrittenApplyEnd(getDate(row, 8));
                schedule.setWrittenExamStart(getDate(row, 9));
                schedule.setWrittenExamEnd(getDate(row, 10));
                schedule.setWrittenResultDate(getDate(row, 11));
                schedule.setPracticalApplyStart(getDate(row, 12));
                schedule.setPracticalApplyEnd(getDate(row, 13));
                schedule.setPracticalExamStart(getDate(row, 14));
                schedule.setPracticalExamEnd(getDate(row, 15));
                schedule.setFinalResultDate(getDate(row, 16));
                schedule.setWrittenFee(getString(row, 17));
                schedule.setPracticalFee(getString(row, 18));

                scheduleRepository.save(schedule);
            }
        }
    }

    private String getString(Row row, int idx) {
        Cell cell = row.getCell(idx);
        return (cell == null) ? null : cell.toString().trim();
    }

    private final DateTimeFormatter excelDateFormatter = DateTimeFormatter.ofPattern("yyyy.MM.dd");

    private LocalDate getDate(Row row, int idx) {
        Cell cell = row.getCell(idx);
        if (cell == null) return null;

        if (cell.getCellType() == CellType.NUMERIC && DateUtil.isCellDateFormatted(cell)) {
            return cell.getLocalDateTimeCellValue().toLocalDate();
        } else if (cell.getCellType() == CellType.STRING) {
            try {
                return LocalDate.parse(cell.getStringCellValue().trim(), excelDateFormatter);
            } catch (Exception e) {
                return null; // 혹은 로깅
            }
        }
        return null;
    }

}
