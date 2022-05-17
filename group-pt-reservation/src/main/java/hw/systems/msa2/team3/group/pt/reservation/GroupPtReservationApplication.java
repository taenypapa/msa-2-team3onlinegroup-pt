package hw.systems.msa2.team3.group.pt.reservation;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableCaching
@EnableFeignClients
public class GroupPtReservationApplication {

	public static void main(String[] args) {
		SpringApplication.run(GroupPtReservationApplication.class, args);
	}

}
