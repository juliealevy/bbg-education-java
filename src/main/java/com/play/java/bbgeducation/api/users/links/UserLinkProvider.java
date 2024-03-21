package com.play.java.bbgeducation.api.users.links;

import com.play.java.bbgeducation.api.links.LinkProviderBase;
import com.play.java.bbgeducation.api.users.UserResource;
import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.hateoas.Link;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class UserLinkProvider extends LinkProviderBase<Class<UserResource>> {
    protected UserLinkProvider() {
        super(UserResource.class);
    }

    public Link getUpdateLink(Long userId){
        return linkTo(methodOn(getController())
                .updateUser(userId,null, null)).withRel(UserLinkRelations.UPDATE.value);

    }

    public Link getAllLink(){
        return linkTo(methodOn(getController())
                .getAll(null)).withRel(UserLinkRelations.GET_ALL.value);
    }

    public Link getByIdLink(Long userId, boolean asSelf){
        return linkTo(methodOn(getController())
                .getById(userId)).withRel(asSelf? IanaLinkRelations.SELF_VALUE: UserLinkRelations.GET_BY_ID.value);
    }

    public Link getByEmailLink(){
        return linkTo(methodOn(getController())
                .getByEmail(null, null)).withRel(UserLinkRelations.GET_BY_EMAIL.value);
    }

    public Link getDeleteLink(Long userId){
        return linkTo(methodOn(getController())
                .deleteById(userId, null)).withRel(UserLinkRelations.DELETE.value);
    }
}
