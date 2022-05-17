package hw.systems.msa2.team3.group.pt.reservation.main;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Getter @Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReservationEntity implements Serializable {
    @Id
    @GeneratedValue
    private Long id;

    private Long memberId;
    private Long myclassId;

    @Enumerated(EnumType.STRING)
    private ReservationStatus status;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss", timezone = "Asia/Seoul")
    private LocalDateTime registeredAt;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss", timezone = "Asia/Seoul")
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
