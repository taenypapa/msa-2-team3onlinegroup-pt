package hw.systems.msa2.team3.group.pt.reservation.kafka.producer;

import hw.systems.msa2.team3.group.pt.reservation.main.ReservationEntity;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@AllArgsConstructor
public class ProducerService {

    private final KafkaTemplate<String, Object> objectKafkaTemplate;

    public void sendAddReservation(ReservationEntity reservationEntity) {
        log.info("produced: {}", reservationEntity);
        this.objectKafkaTemplate.send("UPDATED-RESERVATION", reservationEntity);
    }
}
