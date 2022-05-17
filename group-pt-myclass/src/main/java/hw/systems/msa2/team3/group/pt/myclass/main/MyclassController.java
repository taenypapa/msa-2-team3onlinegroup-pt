package hw.systems.msa2.team3.group.pt.myclass.main;

import hw.systems.msa2.team3.group.pt.myclass.main.read.MyclassReadService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/myclasses")
@AllArgsConstructor
@Slf4j
public class MyclassController {
    private MyclassReadService myclassReadService;
    private MyclassService myclassService;

    @GetMapping
    public ResponseEntity<?> readAll() {
        List<MyclassEntity> myclassEntities = myclassReadService.findAvailableMyclass();
        List<MyclassResource> myclassResources = new ArrayList<>();
        if(myclassEntities != null && myclassEntities.size() > 0) {

            for(MyclassEntity myclassEntity : myclassEntities){
            	myclassResources.add(new MyclassResource(myclassEntity));
            }

            return ResponseEntity.ok().body(myclassResources);
        } else {
            return ResponseEntity.noContent().build();
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> readOne(@PathVariable Long id) {
        Optional<MyclassEntity> optionalMyclassEntity = myclassReadService.findMyclass(id);

        if(optionalMyclassEntity.isPresent()) {
            return ResponseEntity.ok().body(new MyclassResource(optionalMyclassEntity.get()));
        } else {
            return ResponseEntity.noContent().build();
        }
    }

    @PostMapping
    public ResponseEntity<?> add(@RequestBody MyclassDto dto) {

    	MyclassEntity myclassEntity = MyclassEntity.builder()
                .className(dto.getClassName())
                .maxCnt(dto.getMaxCnt())
                .reserveCnt(dto.getReserveCnt())
                .build();

        if(myclassEntity != null && myclassEntity.getClassName() != null) {
            return ResponseEntity.ok().body(new MyclassResource(myclassService.save(myclassEntity)));
        } else {
            return ResponseEntity.noContent().build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable Long id, @RequestBody MyclassDto dto) {

        Optional<MyclassEntity> optionalMyclassEntity = myclassReadService.findMyclass(id);

        if(optionalMyclassEntity.isPresent()) {

        	MyclassEntity myclassEntity = optionalMyclassEntity.get();
        	myclassEntity.setReserveCnt(dto.getReserveCnt());

            return ResponseEntity.ok().body(new MyclassResource(myclassService.save(myclassEntity)));
        } else {
            return ResponseEntity.noContent().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {

        try {
            myclassService.delete(id);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
}