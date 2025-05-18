package docherri.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Getter @Setter
@NoArgsConstructor
public class Schedule {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    private LocalDate registrationStart; // 접수 시작
    private LocalDate registrationEnd;   // 접수 끝

    private LocalDate examDate;          // 단일 시험일
    private LocalDate resultDate;        // 단일 결과 발표

    private String fee1;                 // 1차 수수료
    private String fee2;                 // 2차 수수료

    // 필기 관련
    private LocalDate writtenApplyStart;
    private LocalDate writtenApplyEnd;
    private LocalDate writtenExamStart;
    private LocalDate writtenExamEnd;
    private LocalDate writtenResultDate;

    // 실기 관련
    private LocalDate practicalApplyStart;
    private LocalDate practicalApplyEnd;
    private LocalDate practicalExamStart;
    private LocalDate practicalExamEnd;
    private LocalDate finalResultDate;

    private String writtenFee;    // 필기 수수료
    private String practicalFee;  // 실기 수수료
}
