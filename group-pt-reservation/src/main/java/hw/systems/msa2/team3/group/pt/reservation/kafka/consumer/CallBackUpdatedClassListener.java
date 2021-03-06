package hw.systems.msa2.team3.group.pt.reservation.kafka.consumer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import hw.systems.msa2.team3.group.pt.reservation.kafka.producer.ProducerService;
import hw.systems.msa2.team3.group.pt.reservation.main.ReservationEntity;
import hw.systems.msa2.team3.group.pt.reservation.main.ReservationService;
import hw.systems.msa2.team3.group.pt.reservation.main.ReservationStatus;
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

    private ProducerService producerService;
    @KafkaListener(id = "callBackUpdatedClassListener", topics = "CALL-BACK-UPDATED-CLASS", containerFactory = "kafkaListenerContainerFactory")
    public void callBackUpdatedClassListener(String in, @Header(KafkaHeaders.RECEIVED_TOPIC) String topic,
                                     @Header(KafkaHeaders.OFFSET) long offset) {

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());

        try {
            ReservationEntity result = objectMapper.readValue(in, ReservationEntity.class);

            if(result != null) {
                Optional<ReservationEntity> optionalReservationEntity = reservationReadService.findReservation(result.getId());
                if(optionalReservationEntity.isPresent()) {
                    ReservationEntity reservationEntity = optionalReservationEntity.get();
                    reservationEntity.setStatus(result.getStatus());

                    if(!ReservationStatus.CANCELED.equals(result.getStatus())
                            && !ReservationStatus.REQUESTED.equals(result.getStatus())) {
                        reservationService.save(reservationEntity);
                    } else if(ReservationStatus.CANCELED.equals(result.getStatus())) {
                        Optional<ReservationEntity> addReservationEntity = reservationReadService.findByMyclassIdAndStatus(result.getMyclassId(), ReservationStatus.WAITED);

                        /** ???????????? ????????? ???????????? */
                        if(addReservationEntity.isPresent()) {
                            producerService.sendAddReservation(addReservationEntity.get());
                        }
                    }
                }
            }

        } catch (Exception e) {
            log.error("callBackUpdatedClassListener error: {}", e);
        }
        log.info(" #### Received: {} from {} @ {} ",in, topic, offset);
    }

}
