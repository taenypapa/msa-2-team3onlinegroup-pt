package hw.systems.msa2.team3.group.pt.reservation.kafka.consumer;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@AllArgsConstructor
public class CallBackUpdatedClassListener {

    @KafkaListener(id = "callBackUpdatedClassListener", topics = "CALL-BACK-UPDATED-CLASS", containerFactory = "kafkaListenerContainerFactory")
    public void callBackUpdatedClassListener(String in, @Header(KafkaHeaders.RECEIVED_TOPIC) String topic,
                                     @Header(KafkaHeaders.OFFSET) long offset) {

        log.info(" #### Received: {} from {} @ {} ",in, topic, offset);
    }

}
