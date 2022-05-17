package hw.systems.msa2.team3.group.pt.member.main;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;

@Getter
@Setter
public class MemberResource extends RepresentationModel<MemberResource> {

    private MemberEntity memberEntity;
    public MemberResource(MemberEntity memberEntity) {
        this.memberEntity = memberEntity;
        add(WebMvcLinkBuilder.linkTo(MemberController.class).slash(String.valueOf(memberEntity.getId())).withSelfRel());
    }
}
