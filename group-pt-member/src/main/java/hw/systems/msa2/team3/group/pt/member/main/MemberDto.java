package hw.systems.msa2.team3.group.pt.member.main;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.time.LocalDate;

@Getter @Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MemberDto {

    private String name;
    private LocalDate startDate;
    private LocalDate endDate;
}
