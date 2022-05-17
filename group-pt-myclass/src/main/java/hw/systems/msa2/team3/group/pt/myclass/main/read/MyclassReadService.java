package hw.systems.msa2.team3.group.pt.myclass.main.read;

import hw.systems.msa2.team3.group.pt.myclass.main.MyclassEntity;
import hw.systems.msa2.team3.group.pt.myclass.main.MyclassRepository;
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
public class MyclassReadService {
    private MyclassRepository myclassRepository;

    //@Cacheable("myclass")
    public List<MyclassEntity> findAvailableMyclass(){
        return myclassRepository.findAll();
    }

    //@Cacheable(value = "myclass", key = "#id")
    public Optional<MyclassEntity> findMyclass(Long id){
        return myclassRepository.findById(id);
    }
}