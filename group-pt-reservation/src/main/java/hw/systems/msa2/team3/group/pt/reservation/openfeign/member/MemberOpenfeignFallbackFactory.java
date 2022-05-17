package hw.systems.msa2.team3.group.pt.reservation.openfeign.member;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class MemberOpenfeignFallbackFactory implements FallbackFactory<MemberOpenfeignClient> {
    @Override
    public MemberOpenfeignClient create(Throwable cause) {
        log.error(cause.getLocalizedMessage(), cause);
        return id -> "";
    }
}
