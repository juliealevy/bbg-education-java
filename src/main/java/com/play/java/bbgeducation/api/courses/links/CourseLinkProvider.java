package com.play.java.bbgeducation.api.courses.links;

import com.play.java.bbgeducation.api.courses.CourseController;
import com.play.java.bbgeducation.api.links.LinkProviderBase;
import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.hateoas.Link;
import org.springframework.stereotype.Controller;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Controller
public class CourseLinkProvider extends LinkProviderBase<Class<CourseController>> {

    public CourseLinkProvider() {
        super(CourseController.class);
    }

    public Link getCreateLink(){
        return linkTo(methodOn(getController())
                .createCourse(null,null)).withRel(CourseLinkRelations.CREATE.value);

    }

    public Link getByIdLink(Long id, boolean asSelf) {
        return linkTo(methodOn(getController())
                .getById(id, null))
                .withRel(asSelf ? IanaLinkRelations.SELF_VALUE : CourseLinkRelations.GET_BY_ID.value);

    }

    public Link getDeleteLink(Long id) {
        return linkTo(methodOn(getController())
                .deleteCourse(id, null))
                .withRel(CourseLinkRelations.DELETE.value);

    }
}
