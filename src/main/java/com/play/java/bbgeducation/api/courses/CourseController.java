package com.play.java.bbgeducation.api.courses;

import an.awesome.pipelinr.Pipeline;
import com.play.java.bbgeducation.api.common.NoDataResponse;
import com.play.java.bbgeducation.api.courses.links.CourseLinkProvider;
import com.play.java.bbgeducation.api.endpoints.HasApiEndpoints;
import com.play.java.bbgeducation.application.common.oneof.OneOf2;
import com.play.java.bbgeducation.application.common.oneof.OneOf3;
import com.play.java.bbgeducation.application.common.oneof.oneoftypes.NotFound;
import com.play.java.bbgeducation.application.common.oneof.oneoftypes.Success;
import com.play.java.bbgeducation.application.common.validation.ValidationFailed;
import com.play.java.bbgeducation.application.courses.create.CourseCreateCommand;
import com.play.java.bbgeducation.application.courses.delete.CourseDeleteCommand;
import com.play.java.bbgeducation.application.courses.getById.CourseGetByIdCommand;
import com.play.java.bbgeducation.application.courses.getall.CourseGetAllCommand;
import com.play.java.bbgeducation.application.courses.results.CourseResult;
import com.play.java.bbgeducation.application.courses.update.CourseUpdateCommand;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
                        .add(courseLinkProvider.getSelfLink(httpRequest))
                        .add(courseLinkProvider.getByIdLink(course.getId(), false))
                        .add(courseLinkProvider.getAllLink()),
                        HttpStatus.CREATED),
                fail -> ResponseEntity.of(fail.toProblemDetail("Error creating course"))
                        .build()
        );

    }

    @PutMapping(path="{cid}")
    public ResponseEntity updateCourse(
            @PathVariable("cid") Long courseID,
            @RequestBody CourseRequest updateRequest,
            HttpServletRequest httpRequest
    ){
        CourseUpdateCommand command = CourseUpdateCommand.builder()
                .id(courseID)
                .name(updateRequest.getName())
                .description(updateRequest.getDescription())
                .isOnline(updateRequest.getIsOnline())
                .isPublic(updateRequest.getIsPublic())
                .build();

        OneOf3<Success, NotFound, ValidationFailed> result = pipeline.send(command);

        return result.match(
                success -> ResponseEntity.ok(EntityModel.of(new NoDataResponse())
                        .add(courseLinkProvider.getSelfLink(httpRequest))
                        .add(courseLinkProvider.getAllLink())
                        .add(courseLinkProvider.getByIdLink(courseID, false))
                ),
                notFound -> ResponseEntity.notFound().build(),
                fail -> ResponseEntity.of(fail.toProblemDetail("Error updating course"))
                        .build()
        );
    }

    @GetMapping(path="{cid}")
    public ResponseEntity getById(
            @PathVariable("cid") Long id,
            HttpServletRequest request
    ) {
        CourseGetByIdCommand command = new CourseGetByIdCommand(id);
        OneOf2<CourseResult, NotFound> result = pipeline.send(command);

        return result.match(
                course -> ResponseEntity.ok(buildEntityModelCourseItem(course)),
                notfound -> ResponseEntity.notFound().build()
        );
    }

    @GetMapping(path="")
    public ResponseEntity getAll(
            HttpServletRequest httpRequest
    ){
        CourseGetAllCommand command = CourseGetAllCommand.builder().build();
        List<CourseResult> results = pipeline.send(command);
        return ResponseEntity.ok(buildCollectionModel(results, httpRequest));
    }

    @DeleteMapping(path="{cid}")
    public ResponseEntity deleteCourse(
            @PathVariable("cid") Long id,
            HttpServletRequest request
    ) {
        CourseDeleteCommand command = new CourseDeleteCommand(id);
        OneOf2<Success, NotFound> result = pipeline.send(command);

        return result.match(
                success -> ResponseEntity.ok(EntityModel.of(new NoDataResponse())
                        .add(courseLinkProvider.getSelfLink(request))
                        .add(courseLinkProvider.getAllLink())
                ),
                notFound -> ResponseEntity.notFound().build()
        );
    }

    CollectionModel<EntityModel<CourseResult>> buildCollectionModel(List<CourseResult> courseList, HttpServletRequest httpRequest){
        return CollectionModel.of(buildEntityModelItemList( courseList))
                .add(courseLinkProvider.getSelfLink(httpRequest))
                .add(courseLinkProvider.getCreateLink());
    }

    List<EntityModel<CourseResult>> buildEntityModelItemList(List<CourseResult> courseList){
        return courseList.stream()
                .map(this::buildEntityModelCourseItem)
                .toList();
    }

    EntityModel<CourseResult> buildEntityModelCourseItem(CourseResult result){
        return  EntityModel.of(result)
                .add(courseLinkProvider.getByIdLink(result.getId(), true))
                .add(courseLinkProvider.getUpdateLink(result.getId()))
                .add(courseLinkProvider.getDeleteLink(result.getId()))
                .add(courseLinkProvider.getAllLink());
    }
}
