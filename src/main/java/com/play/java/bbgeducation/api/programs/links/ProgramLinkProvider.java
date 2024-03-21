package com.play.java.bbgeducation.api.programs.links;

import com.play.java.bbgeducation.api.links.LinkProviderBase;
import com.play.java.bbgeducation.api.programs.ProgramResource;
import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.hateoas.Link;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class ProgramLinkProvider extends LinkProviderBase<Class<ProgramResource>> {

    public ProgramLinkProvider() {
        super(ProgramResource.class);
    }

    public Link getCreateLink(){
        return linkTo(methodOn(getController())
                .createProgram(null,null)).withRel(ProgramLinkRelations.CREATE.value);

    }

    public Link getUpdateLink(Long id){
        return linkTo(methodOn(getController())
                .updateProgram(id,null, null)).withRel(ProgramLinkRelations.UPDATE.value);
    }



    public Link getDeleteLink(Long id){
        return linkTo(methodOn(getController())
                .deleteProgramById(id,null)).withRel(ProgramLinkRelations.DELETE.value);

    }


    public Link getByIdLink(Long id, boolean asSelf) {
        return linkTo(methodOn(getController())
                .getById(id, null)).withRel(asSelf ? IanaLinkRelations.SELF_VALUE :
                ProgramLinkRelations.GET_BY_ID.value);
    }



    public Link getAllLink(){
        return linkTo(methodOn(getController())
                .getAll( null)).withRel(ProgramLinkRelations.GET_ALL.value);

    }



}

