package hw.systems.msa2.team3.group.pt.member.main;


import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
public class MemberResource extends RepresentationModel<MemberResource> {

    private MemberEntity memberEntity;
    public MemberResource(MemberEntity memberEntity) {
        this.memberEntity = memberEntity;
        add(WebMvcLinkBuilder.linkTo(MemberController.class).slash(String.valueOf(memberEntity.getId())).withSelfRel());
    }
}
