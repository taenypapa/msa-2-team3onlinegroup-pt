package hw.systems.msa2.team3.group.pt.member.main;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Getter @Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MemberEntity {
    @Id
    @GeneratedValue
    private Long id;

    private String name;
    private LocalDate startDate;
    private LocalDate endDate;
    private LocalDateTime registeredAt;
    private LocalDateTime lastUpdatedAt;

    @PrePersist
    public void setDate() {
        this.setRegisteredAt(LocalDateTime.now());
        this.setLastUpdatedAt(LocalDateTime.now());
    }

    @PreUpdate
    public void modifiedDate() {
        this.setLastUpdatedAt(LocalDateTime.now());
    }
}
