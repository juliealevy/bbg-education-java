package com.play.java.bbgeducation.api.courses.links;

import com.play.java.bbgeducation.api.courses.CourseController;
import com.play.java.bbgeducation.api.courses.CourseRequest;
import com.play.java.bbgeducation.api.endpoints.InvalidApiEndpointLinkException;
import com.play.java.bbgeducation.api.links.ApiLink;
import com.play.java.bbgeducation.api.links.ApiLinkProviderBase;
import com.play.java.bbgeducation.api.links.ApiLinkService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.SneakyThrows;
import org.springframework.hateoas.Link;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
public class CourseApiLinkProvider extends ApiLinkProviderBase<Class<CourseController>> {
    protected CourseApiLinkProvider(ApiLinkService apiLinkService) {
        super(apiLinkService, CourseController.class);
    }

    @SneakyThrows
    public Link getCreateApiLink() {
        Optional<ApiLink> link = apiLinkService.get(CourseLinkRelations.CREATE.value, getController(),
                getController().getMethod("createCourse", CourseRequest.class, HttpServletRequest.class),
                ApiCourseRequest.getApiBody());

        if (link.isEmpty()){
            throw new InvalidApiEndpointLinkException(CourseLinkRelations.CREATE.value);
        }
        return link.get();
    }

    @SneakyThrows
    public Link getByIdApiLink() {
        Optional<ApiLink> link = apiLinkService.get(CourseLinkRelations.GET_BY_ID.value, getController(),
                getController().getMethod("getById", Long.class, HttpServletRequest.class));

        if (link.isEmpty()){
            throw new InvalidApiEndpointLinkException(CourseLinkRelations.GET_BY_ID.value);
        }
        return link.get();
    }
    @Override
    public List<Link> getAllLinks() {
        List<Link> links = new ArrayList<>();
        links.add(getCreateApiLink());
//        links.add(getUpdateApiLink());
//        links.add(getDeleteApiLink());
        links.add(getByIdApiLink());
//        links.add(getAllApiLink());

        return links;
    }
}
