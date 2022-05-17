package hw.systems.msa2.team3.group.pt.reservation.main;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Getter @Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReservationEntity {
    @Id
    @GeneratedValue
    private Long id;

    private Long memberId;
    private Long myclassId;

    @Enumerated(EnumType.STRING)
    private ReservationStatus status;

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
