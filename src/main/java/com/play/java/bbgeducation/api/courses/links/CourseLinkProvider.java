package com.play.java.bbgeducation.api.courses.links;

import com.play.java.bbgeducation.api.courses.CourseController;
import com.play.java.bbgeducation.api.links.LinkProviderBase;
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
}
