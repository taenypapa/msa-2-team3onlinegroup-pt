package hw.systems.msa2.team3.group.pt.myclass.kafka.consumer;

import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import hw.systems.msa2.team3.group.pt.myclass.kafka.producer.ProducerService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.Optional;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;

import hw.systems.msa2.team3.group.pt.myclass.kafka.ReservationEntity;
import hw.systems.msa2.team3.group.pt.myclass.kafka.ReservationStatus;
import hw.systems.msa2.team3.group.pt.myclass.main.MyclassEntity;
import hw.systems.msa2.team3.group.pt.myclass.main.MyclassService;
import hw.systems.msa2.team3.group.pt.myclass.main.read.MyclassReadService;


@Component
@Slf4j
@AllArgsConstructor
public class UpdatedReservationListener {
	
	private MyclassService myclassService;
    private MyclassReadService myclassReadService;

    private ProducerService producerService;

    @KafkaListener(id = "updatedReservationListener", topics = "UPDATED-RESERVATION", containerFactory = "kafkaListenerContainerFactory")
    public void updatedReservationListener(String in, @Header(KafkaHeaders.RECEIVED_TOPIC) String topic,
                                     @Header(KafkaHeaders.OFFSET) long offset) {
    	
    	ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
    	
    	try {
            ReservationEntity result = objectMapper.readValue(in, ReservationEntity.class);

            if(result != null) {
                Optional<MyclassEntity> optionalMyclassEntity = myclassReadService.findMyclass(result.getMyclassId());
                if(optionalMyclassEntity.isPresent()) {
                	MyclassEntity myclassEntity = optionalMyclassEntity.get();
                	int maxCnt = myclassEntity.getMaxCnt();
                    int reserveCnt = myclassEntity.getReserveCnt();
                    
                    if(result.getStatus().equals(ReservationStatus.REQUESTED) || result.getStatus().equals(ReservationStatus.WAITED)) {
                    	if(reserveCnt < maxCnt) {
                        	myclassEntity.setReserveCnt(reserveCnt+1);
                        	result.setStatus(ReservationStatus.DONE);
                        }else {
                        	result.setStatus(ReservationStatus.WAITED);
                        }
                	}
                	else if(result.getStatus().equals(ReservationStatus.CANCELED)){
                		myclassEntity.setReserveCnt(reserveCnt-1);
                	} else {
                        result.setStatus(ReservationStatus.WARN);
                    }
                    
                    myclassService.save(myclassEntity);

                    producerService.sendCallbackReservation(result);
                }

            }

        } catch (Exception e) {
            log.error("callBackUpdatedClassListener error: {}", e);
        }
        

        log.info(" #### Received: {} from {} @ {} ",in, topic, offset);
    }

}
