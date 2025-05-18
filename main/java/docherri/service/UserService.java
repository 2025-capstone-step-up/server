package docherri.service;

import docherri.domain.Schedule;
import docherri.domain.User;
import org.springframework.stereotype.Service;
import docherri.repository.ScheduleRepository;
import docherri.repository.UserRepository;

import jakarta.persistence.EntityNotFoundException;
import java.util.Set;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final ScheduleRepository scheduleRepository;

    public UserService(UserRepository userRepository, ScheduleRepository scheduleRepository) {
        this.userRepository = userRepository;
        this.scheduleRepository = scheduleRepository;
    }

    // 별표 추가
    public void addStar(Long userId, Long scheduleId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("사용자를 찾을 수 없습니다."));
        Schedule schedule = scheduleRepository.findById(scheduleId)
                .orElseThrow(() -> new EntityNotFoundException("일정을 찾을 수 없습니다."));

        if (user.getStarredSchedules().contains(schedule)) {
            throw new IllegalArgumentException("이미 별표한 일정입니다.");
        }

        user.getStarredSchedules().add(schedule);
        userRepository.save(user);
    }

    // 별표 삭제
    public void removeStar(Long userId, Long scheduleId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("사용자를 찾을 수 없습니다."));
        Schedule schedule = scheduleRepository.findById(scheduleId)
                .orElseThrow(() -> new EntityNotFoundException("일정을 찾을 수 없습니다."));

        if (!user.getStarredSchedules().contains(schedule)) {
            throw new IllegalArgumentException("별표하지 않은 일정입니다.");
        }

        user.getStarredSchedules().remove(schedule);
        userRepository.save(user);
    }

    // 별표 목록 조회
    public Set<Schedule> getStarredSchedules(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("사용자를 찾을 수 없습니다."));
        return user.getStarredSchedules();
    }
}
