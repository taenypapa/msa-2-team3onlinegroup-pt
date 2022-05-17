package hw.systems.msa2.team3.group.pt.myclass.main;

import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

import javax.persistence.*;

@Entity
@Getter @Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MyclassEntity {
	
	@Id
    @GeneratedValue
    private Long id;
    private String className;
    private int maxCnt;
    private int reserveCnt;
}
