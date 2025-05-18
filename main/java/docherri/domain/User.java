package docherri.domain;

import jakarta.persistence.*;
import jakarta.persistence.Id;
import lombok.Getter;


import java.util.HashSet;
import java.util.Set;
//사용안함 xxxxxxxxxxxx
@Getter
@Entity
@Table(name="users")
public class User {
    @Id
    @GeneratedValue
    private Long id;

    private String name; //필요없을지도

    @ManyToMany
    @JoinTable(
            name = "user_starred_schedule",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "schedule_id")
    )
    private Set<Schedule> starredSchedules = new HashSet<>();
}