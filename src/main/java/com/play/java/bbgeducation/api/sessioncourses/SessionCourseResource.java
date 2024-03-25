package com.play.java.bbgeducation.api.sessioncourses;

import an.awesome.pipelinr.Pipeline;
import com.play.java.bbgeducation.api.common.NoDataResponse;
import com.play.java.bbgeducation.api.common.RestResource;
import com.play.java.bbgeducation.api.endpoints.HasApiEndpoints;
import com.play.java.bbgeducation.api.sessioncourses.links.SessionCourseLinkProvider;
import com.play.java.bbgeducation.api.sessions.links.ProgramSessionLinkProvider;
import com.play.java.bbgeducation.application.common.oneof.OneOf2;
import com.play.java.bbgeducation.application.common.oneof.oneoftypes.NotFound;
import com.play.java.bbgeducation.application.common.oneof.oneoftypes.Success;
import com.play.java.bbgeducation.application.sessions.addCourse.AddCourseCommand;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestResource
@RequestMapping("api/programs/{pid}/sessions/{sid}/courses")
@HasApiEndpoints
public class SessionCourseResource {

    private final Pipeline pipeline;
    private final SessionCourseLinkProvider sessionCourseLinkProvider;
    private final ProgramSessionLinkProvider sessionLinkProvider;

    public SessionCourseResource(Pipeline pipeline, SessionCourseLinkProvider sessionCourseLinkProvider, ProgramSessionLinkProvider sessionLinkProvider) {
        this.pipeline = pipeline;
        this.sessionCourseLinkProvider = sessionCourseLinkProvider;
        this.sessionLinkProvider = sessionLinkProvider;
    }

    //get all courses for a session
    @GetMapping(path="")
    public ResponseEntity<?> getAllCourses(){
        return ResponseEntity.ok().build();
    }
    //get a specific course for a session
    @GetMapping(path="{cid}")
    public ResponseEntity<?> getById(){
        return ResponseEntity.ok().build();
    }

    //add a course to a session
    @PostMapping(path="{cid}")
    public ResponseEntity<?> addCourseToSession(
            @PathVariable("pid") Long programId,
            @PathVariable("sid") Long sessionId,
            @PathVariable("cid") Long courseId,
            HttpServletRequest httpRequest
    ){

        OneOf2<Success, NotFound> result = pipeline.send(new AddCourseCommand(programId, sessionId, courseId));

        //TODO:  add links:
        //get session courses?
        //remove course
        return result.match(
                success -> ResponseEntity.ok(EntityModel.of(new NoDataResponse())
                        .add(sessionCourseLinkProvider.getSelfLink(httpRequest))
                        .add(sessionLinkProvider.getByIdLink(programId, sessionId, false))
                ),
                notFound -> ResponseEntity.notFound().build()
        );
    }



    //remove a course from a session

    //add classes to a session course

    //remove classes from a session course

    //update a class from a session course
}
