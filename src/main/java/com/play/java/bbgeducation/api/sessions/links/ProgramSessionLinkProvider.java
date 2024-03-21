package com.play.java.bbgeducation.api.sessions.links;

import com.play.java.bbgeducation.api.links.LinkProviderBase;
import com.play.java.bbgeducation.api.sessions.ProgramSessionResource;
import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.hateoas.Link;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class ProgramSessionLinkProvider extends LinkProviderBase<Class<ProgramSessionResource>> {
    public ProgramSessionLinkProvider() {
        super(ProgramSessionResource.class);
    }

    public Link getCreateLink(){
        return linkTo(methodOn(getController())
                .createSession(null,null, null))
                .withRel(SessionLinkRelations.CREATE.value);

    }

    public Link getByIdLink(Long programId, Long sessionId, boolean asSelf) {
        return linkTo(methodOn(getController())
                .getById(programId, sessionId, null)).withRel(asSelf ? IanaLinkRelations.SELF_VALUE :
                SessionLinkRelations.GET_BY_ID.value);
    }

    public Link getByProgramLink(Long programId) {
        return linkTo(methodOn(getController())
                .getByProgram(programId,null)).withRel(SessionLinkRelations.GET_BY_PROGRAM.value);
    }

    public Link getDeleteLink(Long programId, Long sessionId){
        return linkTo(methodOn(getController())
                .deleteSession(programId, sessionId, null)).withRel(SessionLinkRelations.DELETE.value);
    }

    public Link getUpdateLink(Long programId, Long sessionId){
        return linkTo(methodOn(getController())
                .updateSession(programId, sessionId, null, null))
                .withRel(SessionLinkRelations.UPDATE.value);
    }
}
