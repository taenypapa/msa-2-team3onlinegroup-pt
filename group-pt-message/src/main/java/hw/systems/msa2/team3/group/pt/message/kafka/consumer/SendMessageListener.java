package hw.systems.msa2.team3.group.pt.message.kafka.consumer;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@AllArgsConstructor
public class SendMessageListener {

    @KafkaListener(id = "sendMessageListener", topics = "SEND-MESSAGE", containerFactory = "kafkaListenerContainerFactory")
    public void sendMessageListener(String in, @Header(KafkaHeaders.RECEIVED_TOPIC) String topic,
                                     @Header(KafkaHeaders.OFFSET) long offset) {

        log.info(" #### Received: {} from {} @ {} ",in, topic, offset);
    }

}
