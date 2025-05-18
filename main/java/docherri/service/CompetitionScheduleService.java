package docherri.service;

import docherri.domain.CompetitionSchedule;
import docherri.dto.CompetitionScheduleDTO;
import docherri.repository.CompetitionScheduleRepository;
import jakarta.persistence.EntityNotFoundException;
import org.apache.poi.ss.usermodel.*;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
public class CompetitionScheduleService {
    private final CompetitionScheduleRepository competitionScheduleRepository;
    private final DateTimeFormatter excelDateFormatter = DateTimeFormatter.ofPattern("yyyy.MM.dd");

    public CompetitionScheduleService(CompetitionScheduleRepository competitionScheduleRepository) {
        this.competitionScheduleRepository = competitionScheduleRepository;
    }

    // 전체 일정 조회
    public List<CompetitionSchedule> getAllSchedules() {
        return competitionScheduleRepository.findAll();
    }

    // 일정 상세 조회
    public CompetitionSchedule getSchedule(Long id) {
        return competitionScheduleRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("공모전 일정을 찾을 수 없습니다."));
    }

    // 일정 추가
    public CompetitionSchedule addSchedule(CompetitionScheduleDTO dto) {
        return competitionScheduleRepository.save(convertFromDTO(dto));
    }

    // 일정 수정
    public CompetitionSchedule updateSchedule(Long id, CompetitionScheduleDTO dto) {
        CompetitionSchedule existing = getSchedule(id);
        CompetitionSchedule updated = convertFromDTO(dto);
        updated.setId(existing.getId());
        return competitionScheduleRepository.save(updated);
    }

    // 일정 삭제
    public void deleteSchedule(Long id) {
        if (!competitionScheduleRepository.existsById(id)) {
            throw new EntityNotFoundException("공모전 일정을 찾을 수 없습니다.");
        }
        competitionScheduleRepository.deleteById(id);
    }

    // 일정 검색
    public List<CompetitionSchedule> searchSchedules(String keyword) {
        return competitionScheduleRepository.findByNameContainingIgnoreCase(keyword);
    }

    // 엑셀 업로드
    public void uploadFromExcel(MultipartFile file) throws IOException {
        try (InputStream is = file.getInputStream()) {
            Workbook workbook = WorkbookFactory.create(is);
            Sheet sheet = workbook.getSheetAt(0);

            for (int i = 1; i <= sheet.getLastRowNum(); i++) {
                Row row = sheet.getRow(i);
                if (row == null) continue;

                CompetitionSchedule schedule = new CompetitionSchedule();
                schedule.setName(getString(row, 0));
                schedule.setStartDate(getDate(row, 1));
                schedule.setEndDate(getDate(row, 2));
                schedule.setUrl(getString(row, 3));

                competitionScheduleRepository.save(schedule);
            }
        }
    }

    private CompetitionSchedule convertFromDTO(CompetitionScheduleDTO dto) {
        CompetitionSchedule schedule = new CompetitionSchedule();
        schedule.setName(dto.getName());
        schedule.setStartDate(dto.getStartDate());
        schedule.setEndDate(dto.getEndDate());
        schedule.setUrl(dto.getUrl());
        return schedule;
    }

    private String getString(Row row, int idx) {
        Cell cell = row.getCell(idx);
        return (cell == null) ? null : cell.toString().trim();
    }

    private LocalDate getDate(Row row, int idx) {
        Cell cell = row.getCell(idx);
        if (cell == null) return null;

        if (cell.getCellType() == CellType.NUMERIC && DateUtil.isCellDateFormatted(cell)) {
            return cell.getLocalDateTimeCellValue().toLocalDate();
        } else if (cell.getCellType() == CellType.STRING) {
            try {
                return LocalDate.parse(cell.getStringCellValue().trim(), excelDateFormatter);
            } catch (Exception e) {
                return null;
            }
        }
        return null;
    }
}
