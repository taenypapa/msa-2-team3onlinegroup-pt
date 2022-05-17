package hw.systems.msa2.team3.group.pt.reservation.main;

import hw.systems.msa2.team3.group.pt.reservation.kafka.producer.ProducerService;
import hw.systems.msa2.team3.group.pt.reservation.main.read.ReservationReadService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
@Slf4j
public class ReservationService {
    private ReservationReadService reservationReadService;
    private ReservationRepository reservationRepository;
    private ProducerService producerService;

    public ReservationEntity insert(ReservationEntity reservationEntity) {
        ReservationEntity savedReservationEntity = reservationRepository.save(reservationEntity);
        producerService.sendAddReservation(savedReservationEntity);
        return savedReservationEntity;
    }

    public ReservationEntity update(ReservationEntity reservationEntity) {
        return reservationRepository.save(reservationEntity);
    }

    public void delete(Long id) {

        Optional<ReservationEntity> optionalReservationEntity = reservationReadService.findReservation(id);
        if(optionalReservationEntity.isPresent()) {
            ReservationEntity deletedReservationEntity = optionalReservationEntity.get();
            deletedReservationEntity.setStatus(ReservationStatus.CANCELED);
            producerService.sendAddReservation(deletedReservationEntity);

            reservationRepository.deleteById(id);
        }
    }
}
