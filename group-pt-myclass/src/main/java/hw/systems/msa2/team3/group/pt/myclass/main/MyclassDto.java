package hw.systems.msa2.team3.group.pt.myclass.main;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.time.LocalDate;

@Getter @Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MyclassDto {

    private String classId;
    private String className;
    private int maxCnt;
    private int reserveCnt;
}