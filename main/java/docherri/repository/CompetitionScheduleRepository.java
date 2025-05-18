package docherri.repository;

import docherri.domain.CompetitionSchedule;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface CompetitionScheduleRepository extends JpaRepository<CompetitionSchedule, Long> {
    List<CompetitionSchedule> findByNameContainingIgnoreCase(String keyword);
}
