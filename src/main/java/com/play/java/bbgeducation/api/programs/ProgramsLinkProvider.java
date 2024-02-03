package com.play.java.bbgeducation.api.programs;

import com.play.java.bbgeducation.api.LinkProviderBase;
import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.hateoas.Link;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class ProgramsLinkProvider extends LinkProviderBase {

    private final Class<ProgramController> myController = ProgramController.class;
    public Link getCreateLink(){
        return linkTo(methodOn(myController)
                .createProgram(null,null)).withRel("program:create");

    }

    public Link getUpdateLink(Long id){
        return linkTo(methodOn(myController)
                .updateProgram(id,null, null)).withRel("program:update");

    }

    public Link getDeleteLink(Long id){
        return linkTo(methodOn(myController)
                .deleteProgramById(id,null)).withRel("program:delete");

    }


    public Link getByIdLink(Long id, boolean asSelf) {
        return linkTo(methodOn(myController)
                .getById(id, null)).withRel(asSelf ? IanaLinkRelations.SELF_VALUE :
                "program:get-by-id");

    }

    public Link getAllLink(){
        return linkTo(methodOn(myController)
                .getAll( null)).withRel("program:get-all");

    }

}
