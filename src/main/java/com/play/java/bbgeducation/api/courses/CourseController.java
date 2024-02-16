package com.play.java.bbgeducation.api.courses;

import an.awesome.pipelinr.Pipeline;
import com.play.java.bbgeducation.api.courses.links.CourseLinkProvider;
import com.play.java.bbgeducation.api.endpoints.HasApiEndpoints;
import com.play.java.bbgeducation.application.common.oneof.OneOf2;
import com.play.java.bbgeducation.application.common.validation.ValidationFailed;
import com.play.java.bbgeducation.application.courses.create.CourseCreateCommand;
import com.play.java.bbgeducation.application.courses.results.CourseResult;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/courses")
@HasApiEndpoints
public class CourseController {
    private final Pipeline pipeline;
    private final CourseLinkProvider courseLinkProvider;

    public CourseController(Pipeline pipeline, CourseLinkProvider courseLinkProvider) {
        this.pipeline = pipeline;
        this.courseLinkProvider = courseLinkProvider;
    }

    @PostMapping(path="")
    public ResponseEntity createCourse(
            @RequestBody CourseRequest createRequest,
            HttpServletRequest httpRequest
    ) {

        CourseCreateCommand command = CourseCreateCommand.builder()
                .name(createRequest.getName())
                .description(createRequest.getDescription())
                .isPublic(createRequest.getIsPublic())
                .isOnline(createRequest.getIsOnline())
                .build();

        OneOf2<CourseResult, ValidationFailed> result = pipeline.send(command);

        return result.match(
                course -> new ResponseEntity(EntityModel.of(course)
                        .add(courseLinkProvider.getSelfLink(httpRequest)),
                        HttpStatus.CREATED),
                fail -> ResponseEntity.of(ProblemDetail.forStatusAndDetail(HttpStatus.CONFLICT, fail.getErrorMessage()))
                        .build()
        );

    }
}
