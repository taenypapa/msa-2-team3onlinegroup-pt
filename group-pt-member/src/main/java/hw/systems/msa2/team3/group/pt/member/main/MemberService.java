package hw.systems.msa2.team3.group.pt.member.main;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
@Slf4j
public class MemberService {
    private MemberRepository memberRepository;

    public MemberEntity save(MemberEntity memberEntity) {
        return memberRepository.save(memberEntity);
    }

    public void delete(Long id) {
        memberRepository.deleteById(id);
    }
}
