package com.play.java.bbgeducation.api.sessioncourses.links;

import com.play.java.bbgeducation.api.links.LinkProviderBase;
import com.play.java.bbgeducation.api.sessioncourses.SessionCourseResource;
import org.springframework.hateoas.Link;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class SessionCourseLinkProvider extends LinkProviderBase<Class<SessionCourseResource>> {
    protected SessionCourseLinkProvider() {
        super(SessionCourseResource.class);
    }

    public Link getAddCourseToSessionLink(Long programId, Long sessionId, Long courseId) {
        return linkTo(methodOn(getController())
                .addCourseToSession(programId,sessionId, courseId, null))
                .withRel(SessionCourseLinkRelations.ADD_COURSE.value);
    }
}
