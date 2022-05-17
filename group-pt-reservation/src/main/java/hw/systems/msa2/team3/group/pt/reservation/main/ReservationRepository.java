package hw.systems.msa2.team3.group.pt.reservation.main;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface ReservationRepository extends JpaRepository<ReservationEntity, Long> {
    Optional<ReservationEntity> findFirstByStatusOrderByRegisteredAt(ReservationStatus status);
    List<ReservationEntity> findByMyclassId(Long myclassId);
    List<ReservationEntity> findByMemberId(Long memberId);
}
