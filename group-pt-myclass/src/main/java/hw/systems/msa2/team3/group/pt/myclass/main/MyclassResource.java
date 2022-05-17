package hw.systems.msa2.team3.group.pt.myclass.main;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;

@Getter
@Setter
public class MyclassResource extends RepresentationModel<MyclassResource> {

    private MyclassEntity myclassEntity;
    public MyclassResource(MyclassEntity myclassEntity) {
        this.myclassEntity = myclassEntity;
        add(WebMvcLinkBuilder.linkTo(MyclassController.class).slash(String.valueOf(myclassEntity.getId())).withSelfRel());
    }
}