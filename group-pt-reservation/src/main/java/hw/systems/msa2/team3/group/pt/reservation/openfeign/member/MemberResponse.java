package hw.systems.msa2.team3.group.pt.reservation.openfeign.member;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Builder @AllArgsConstructor
@NoArgsConstructor
public class MemberResponse implements Serializable {
    private Long id;
    private String name;
    private LocalDate startDate;
    private LocalDate endDate;
    private LocalDateTime registeredAt;
    private LocalDateTime lastUpdatedAt;
}
