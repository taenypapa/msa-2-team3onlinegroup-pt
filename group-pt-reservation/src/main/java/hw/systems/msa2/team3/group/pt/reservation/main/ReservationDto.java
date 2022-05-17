package hw.systems.msa2.team3.group.pt.reservation.main;

import lombok.*;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import java.io.Serializable;
import java.time.LocalDate;

@Getter @Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReservationDto implements Serializable {

    private Long memberId;
    private Long myclassId;
    private ReservationStatus status;
}
