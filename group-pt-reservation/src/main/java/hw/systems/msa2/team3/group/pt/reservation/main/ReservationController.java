package hw.systems.msa2.team3.group.pt.reservation.main;

import hw.systems.msa2.team3.group.pt.reservation.main.read.ReservationReadService;
import hw.systems.msa2.team3.group.pt.reservation.openfeign.member.MemberOpenfeignService;
import hw.systems.msa2.team3.group.pt.reservation.openfeign.member.MemberResponse;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/reservations")
@AllArgsConstructor
@Slf4j
public class ReservationController {
    private ReservationReadService reservationReadService;
    private ReservationService reservationService;

    private MemberOpenfeignService memberOpenfeignService;

    @GetMapping
    public ResponseEntity<?> readAll() {
        List<ReservationEntity> reservationEntities = reservationReadService.findAll();
        List<ReservationResource> reservationResources = new ArrayList<>();
        if(reservationEntities != null && reservationEntities.size() > 0) {

            for(ReservationEntity reservationEntity : reservationEntities){
                reservationResources.add(new ReservationResource(reservationEntity));
            }

            return ResponseEntity.ok().body(reservationResources);
        } else {
            return ResponseEntity.noContent().build();
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> readOne(@PathVariable Long id) {
        Optional<ReservationEntity> optionalMemberEntity = reservationReadService.findReservation(id);

        if(optionalMemberEntity.isPresent()) {
            return ResponseEntity.ok().body(new ReservationResource(optionalMemberEntity.get()));
        } else {
            return ResponseEntity.noContent().build();
        }
    }

    @PostMapping
    public ResponseEntity<?> add(@RequestBody ReservationDto dto) {

        MemberResponse memberResponse = memberOpenfeignService.getMember(dto.getMemberId());

        if(memberResponse == null) {
            return ResponseEntity.badRequest().build();
        }

        ReservationEntity reservationEntity = ReservationEntity.builder()
                .myclassId(dto.getMyclassId())
                .memberId(dto.getMemberId())
                .status(ReservationStatus.REQUESTED)
                .build();

        if(reservationEntity != null && reservationEntity.getMyclassId() != null
                && reservationEntity.getMemberId() != null) {
            return ResponseEntity.ok().body(new ReservationResource(reservationService.insert(reservationEntity)));
        } else {
            return ResponseEntity.noContent().build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable Long id, @RequestBody ReservationDto dto) {

        Optional<ReservationEntity> optionalMemberEntity = reservationReadService.findReservation(id);

        if(optionalMemberEntity.isPresent()) {

            ReservationEntity reservationEntity = optionalMemberEntity.get();
            reservationEntity.setStatus(dto.getStatus());

            return ResponseEntity.ok().body(new ReservationResource(reservationService.update(reservationEntity)));
        } else {
            return ResponseEntity.noContent().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {

        try {
            reservationService.delete(id);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
}
