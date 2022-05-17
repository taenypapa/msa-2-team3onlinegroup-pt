package hw.systems.msa2.team3.group.pt.member.main.read;

import hw.systems.msa2.team3.group.pt.member.main.MemberEntity;
import hw.systems.msa2.team3.group.pt.member.main.MemberRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
@Slf4j
public class MemberReadService {
    private MemberRepository memberRepository;

    @Cacheable("member")
    public List<MemberEntity> findAvailableMember(){
        return memberRepository.findByStartDateLessThanEqualAndEndDateGreaterThanEqual(LocalDate.now(), LocalDate.now());
    }

    @Cacheable(value = "member", key = "#id")
    public Optional<MemberEntity> findMember(Long id){
        return memberRepository.findById(id);
    }
}
