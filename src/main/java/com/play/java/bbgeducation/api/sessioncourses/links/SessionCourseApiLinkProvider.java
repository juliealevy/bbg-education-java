package com.play.java.bbgeducation.api.sessioncourses.links;

import com.play.java.bbgeducation.api.links.ApiLinkProviderBase;
import com.play.java.bbgeducation.api.links.ApiLinkService;
import com.play.java.bbgeducation.api.sessioncourses.SessionCourseResource;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.hateoas.Link;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class SessionCourseApiLinkProvider extends ApiLinkProviderBase<Class<SessionCourseResource>> {

    protected SessionCourseApiLinkProvider(ApiLinkService apiLinkService) {
        super(apiLinkService, SessionCourseResource.class);
    }

    public Link getAddCourseApiLink(){
        return getApiLink(SessionCourseLinkRelations.ADD_COURSE.value, "addCourseToSession",
                Long.class,Long.class,Long.class, HttpServletRequest.class);
    }

    @Override
    public List<Link> getAllLinks() {
        List<Link> links = new ArrayList<>();
        links.add(getAddCourseApiLink());
        return links;
    }
}
