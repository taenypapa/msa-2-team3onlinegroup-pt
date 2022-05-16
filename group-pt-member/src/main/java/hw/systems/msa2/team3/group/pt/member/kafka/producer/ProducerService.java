package hw.systems.msa2.team3.group.pt.member.kafka.producer;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@AllArgsConstructor
public class ProducerService {

    private final KafkaTemplate<String, Object> objectKafkaTemplate;
}
