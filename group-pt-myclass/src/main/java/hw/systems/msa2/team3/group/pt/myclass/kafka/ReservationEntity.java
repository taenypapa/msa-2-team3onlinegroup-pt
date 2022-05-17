package hw.systems.msa2.team3.group.pt.myclass.kafka;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter @Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReservationEntity {

    private Long id;
    private Long memberId;
    private Long myclassId;
    private ReservationStatus status;
    private LocalDateTime registeredAt;
    private LocalDateTime lastUpdatedAt;
}
