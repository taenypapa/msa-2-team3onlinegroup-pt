package hw.systems.msa2.team3.group.pt.reservation.kafka.consumer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import hw.systems.msa2.team3.group.pt.reservation.main.ReservationEntity;
import hw.systems.msa2.team3.group.pt.reservation.main.ReservationService;
import hw.systems.msa2.team3.group.pt.reservation.main.read.ReservationReadService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@Slf4j
@AllArgsConstructor
public class CallBackUpdatedClassListener {

    private ReservationService reservationService;
    private ReservationReadService reservationReadService;
    @KafkaListener(id = "callBackUpdatedClassListener", topics = "CALL-BACK-UPDATED-CLASS", containerFactory = "kafkaListenerContainerFactory")
    public void callBackUpdatedClassListener(String in, @Header(KafkaHeaders.RECEIVED_TOPIC) String topic,
                                     @Header(KafkaHeaders.OFFSET) long offset) {

        ObjectMapper objectMapper = new ObjectMapper();
        ModelMapper modelMapper = new ModelMapper();

        try {
            ReservationEntity result = objectMapper.readValue(in, ReservationEntity.class);

            if(result != null) {
                Optional<ReservationEntity> optionalReservationEntity = reservationReadService.findReservation(result.getId());
                if(optionalReservationEntity.isPresent()) {
                    ReservationEntity reservationEntity = optionalReservationEntity.get();
                    reservationEntity.setStatus(result.getStatus());

                    reservationService.update(reservationEntity);
                }
            }

        } catch (Exception e) {
            log.error("callBackUpdatedClassListener error: {}", e);
        }
        log.info(" #### Received: {} from {} @ {} ",in, topic, offset);
    }

}
