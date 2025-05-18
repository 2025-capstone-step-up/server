package docherri.dto;

import docherri.domain.Schedule;

import java.util.Set;

public class UserDTO {
    private Long id;
    private String name;
    private Set<Schedule> starredSchdules;
}
