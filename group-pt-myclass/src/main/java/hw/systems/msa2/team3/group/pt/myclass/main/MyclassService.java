package hw.systems.msa2.team3.group.pt.myclass.main;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
@Slf4j
public class MyclassService {
    private MyclassRepository myclassRepository;

    public MyclassEntity save(MyclassEntity myclassEntity) {
        return myclassRepository.save(myclassEntity);
    }

    public void delete(Long id) {
    	myclassRepository.deleteById(id);
    }

}

