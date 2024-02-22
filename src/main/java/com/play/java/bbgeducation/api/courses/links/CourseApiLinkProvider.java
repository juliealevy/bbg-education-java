package com.play.java.bbgeducation.api.courses.links;

import com.play.java.bbgeducation.api.courses.CourseController;
import com.play.java.bbgeducation.api.courses.CourseRequest;
import com.play.java.bbgeducation.api.links.ApiLinkProviderBase;
import com.play.java.bbgeducation.api.links.ApiLinkService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.hateoas.Link;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class CourseApiLinkProvider extends ApiLinkProviderBase<Class<CourseController>> {
    protected CourseApiLinkProvider(ApiLinkService apiLinkService) {
        super(apiLinkService, CourseController.class);
    }

    public Link getCreateApiLink() {
        return getApiLink(CourseLinkRelations.CREATE.value, ApiCourseRequest.getApiBody(),
                "createCourse", CourseRequest.class, HttpServletRequest.class);
    }

    public Link getUpdateApiLink(){
        return getApiLink(CourseLinkRelations.UPDATE.value, ApiCourseRequest.getApiBody(),
                "updateCourse", Long.class, CourseRequest.class, HttpServletRequest.class);
    }

    public Link getByIdApiLink() {
        return getApiLink(CourseLinkRelations.GET_BY_ID.value,
                "getById", Long.class, HttpServletRequest.class);
    }

    public Link getAllApiLink() {
        return getApiLink(CourseLinkRelations.GET_ALL.value,
                "getAll", HttpServletRequest.class);
    }

    public Link getDeleteApiLink(){
        return getApiLink(CourseLinkRelations.DELETE.value,
                "deleteCourse",  Long.class, HttpServletRequest.class);
    }


    @Override
    public List<Link> getAllLinks() {
        return List.of(
                getCreateApiLink(),
                getUpdateApiLink(),
                getDeleteApiLink(),
                getByIdApiLink(),
                getAllApiLink()
        );

    }

}
