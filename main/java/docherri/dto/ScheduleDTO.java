package docherri.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class ScheduleDTO {

    private String title;

    private LocalDate registrationStart;
    private LocalDate registrationEnd;

    private LocalDate examDate;
    private LocalDate resultDate;

    private String fee1;
    private String fee2;

    private LocalDate writtenApplyStart;
    private LocalDate writtenApplyEnd;
    private LocalDate writtenExamStart;
    private LocalDate writtenExamEnd;
    private LocalDate writtenResultDate;

    private LocalDate practicalApplyStart;
    private LocalDate practicalApplyEnd;
    private LocalDate practicalExamStart;
    private LocalDate practicalExamEnd;
    private LocalDate finalResultDate;

    private String writtenFee;
    private String practicalFee;
}
