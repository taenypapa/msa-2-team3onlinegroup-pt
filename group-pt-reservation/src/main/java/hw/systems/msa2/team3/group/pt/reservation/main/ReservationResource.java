package hw.systems.msa2.team3.group.pt.reservation.main;


import lombok.Getter;
import lombok.Setter;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;

@Getter
@Setter
public class ReservationResource extends RepresentationModel<ReservationResource> {

    private ReservationEntity reservationEntity;
    public ReservationResource(ReservationEntity reservationEntity) {
        this.reservationEntity = reservationEntity;
        add(WebMvcLinkBuilder.linkTo(ReservationController.class).slash(String.valueOf(reservationEntity.getId())).withSelfRel());
    }
}
