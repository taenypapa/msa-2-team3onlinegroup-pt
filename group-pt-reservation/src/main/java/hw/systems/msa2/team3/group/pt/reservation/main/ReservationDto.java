package hw.systems.msa2.team3.group.pt.reservation.main;

import lombok.*;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import java.time.LocalDate;

@Getter @Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReservationDto {

    private Long memberId;
    private Long myclassId;
    private ReservationStatus status;
}
