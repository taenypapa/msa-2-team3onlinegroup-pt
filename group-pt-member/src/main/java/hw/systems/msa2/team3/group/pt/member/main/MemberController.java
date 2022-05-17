package hw.systems.msa2.team3.group.pt.member.main;

import hw.systems.msa2.team3.group.pt.member.main.read.MemberReadService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/members")
@AllArgsConstructor
@Slf4j
public class MemberController {
    private MemberReadService memberReadService;
    private MemberService memberService;

    @GetMapping
    public ResponseEntity<?> readAll() {
        List<MemberEntity> memberEntities = memberReadService.findAvailableMember();
        List<MemberResource> memberResources = new ArrayList<>();
        if(memberEntities != null && memberEntities.size() > 0) {

            for(MemberEntity memberEntity : memberEntities){
                memberResources.add(new MemberResource(memberEntity));
            }

            return ResponseEntity.ok().body(memberResources);
        } else {
            return ResponseEntity.noContent().build();
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> readOne(@PathVariable Long id) {
        Optional<MemberEntity> optionalMemberEntity = memberReadService.findMember(id);

        if(optionalMemberEntity.isPresent()) {
            return ResponseEntity.ok().body(new MemberResource(optionalMemberEntity.get()));
        } else {
            return ResponseEntity.noContent().build();
        }
    }

    @PostMapping
    public ResponseEntity<?> add(@RequestBody MemberDto dto) {

        MemberEntity memberEntity = MemberEntity.builder()
                .name(dto.getName())
                .startDate(dto.getStartDate())
                .endDate(dto.getEndDate())
                .build();

        if(memberEntity != null && memberEntity.getName() != null) {
            return ResponseEntity.ok().body(new MemberResource(memberService.save(memberEntity)));
        } else {
            return ResponseEntity.noContent().build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable Long id, @RequestBody MemberDto dto) {

        Optional<MemberEntity> optionalMemberEntity = memberReadService.findMember(id);

        if(optionalMemberEntity.isPresent()) {

            MemberEntity memberEntity = optionalMemberEntity.get();
            memberEntity.setStartDate(dto.getStartDate());
            memberEntity.setEndDate(dto.getEndDate());

            return ResponseEntity.ok().body(new MemberResource(memberService.save(memberEntity)));
        } else {
            return ResponseEntity.noContent().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {

        try {
            memberService.delete(id);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
}
