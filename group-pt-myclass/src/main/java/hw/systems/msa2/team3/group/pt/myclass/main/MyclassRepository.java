package hw.systems.msa2.team3.group.pt.myclass.main;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface MyclassRepository extends JpaRepository<MyclassEntity, Long> {
    
}