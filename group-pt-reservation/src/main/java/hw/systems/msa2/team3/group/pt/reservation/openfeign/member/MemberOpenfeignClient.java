package hw.systems.msa2.team3.group.pt.reservation.openfeign.member;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@FeignClient(name = "memberOpenfeignClient", url = "http://localhost:8080", configuration = MemberOpenfeignConfig.class)
public interface MemberOpenfeignClient {

    @RequestMapping(method = RequestMethod.GET, value = "/members/{id}")
    String getMember(@PathVariable("id") Long id);
}
