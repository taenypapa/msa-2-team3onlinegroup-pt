package hw.systems.msa2.team3.group.pt.member;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class GroupPtMemberApplication {

	public static void main(String[] args) {
		SpringApplication.run(GroupPtMemberApplication.class, args);
	}

}
