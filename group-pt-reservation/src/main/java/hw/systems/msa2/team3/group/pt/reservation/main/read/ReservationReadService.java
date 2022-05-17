package hw.systems.msa2.team3.group.pt.reservation.main.read;

import hw.systems.msa2.team3.group.pt.reservation.main.ReservationEntity;
import hw.systems.msa2.team3.group.pt.reservation.main.ReservationRepository;
import hw.systems.msa2.team3.group.pt.reservation.main.ReservationStatus;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
@Slf4j
public class ReservationReadService {
    private ReservationRepository reservationRepository;

    public Optional<ReservationEntity> findByMyclassIdAndStatus(Long myclassId, ReservationStatus status){
        return reservationRepository.findFirstByMyclassIdAndStatusOrderByRegisteredAt(myclassId, status);
    }

    public Optional<ReservationEntity> findByMemberIdAndMyclassId(Long memberId, Long myclassId){
        return reservationRepository.findByMemberIdAndMyclassId(memberId, myclassId);
    }


    //@Cacheable("reservation")
    public List<ReservationEntity> findAll(){
        return reservationRepository.findAll();
    }

    //@Cacheable(value = "reservation", key = "#myclassId")
    public List<ReservationEntity> findByMyclassId(Long myclassId){
        return reservationRepository.findByMyclassId(myclassId);
    }

    //@Cacheable(value = "reservation", key = "#memberId")
    public List<ReservationEntity> findByMemberId(Long memberId){
        return reservationRepository.findByMemberId(memberId);
    }

    //@Cacheable(value = "reservation", key = "#id")
    public Optional<ReservationEntity> findReservation(Long id){
        return reservationRepository.findById(id);
    }
}
