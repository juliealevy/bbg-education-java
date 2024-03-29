package com.play.java.bbgeducation.api.sessions;

import an.awesome.pipelinr.Pipeline;
import com.play.java.bbgeducation.api.common.NoDataResponse;
import com.play.java.bbgeducation.api.common.RestResource;
import com.play.java.bbgeducation.api.endpoints.HasApiEndpoints;
import com.play.java.bbgeducation.api.programs.links.ProgramLinkProvider;
import com.play.java.bbgeducation.api.sessioncourses.SessionCourseResource;
import com.play.java.bbgeducation.api.sessioncourses.links.SessionCourseLinkProvider;
import com.play.java.bbgeducation.api.sessions.links.ProgramSessionLinkProvider;
import com.play.java.bbgeducation.application.common.oneof.OneOf2;
import com.play.java.bbgeducation.application.common.oneof.OneOf3;
import com.play.java.bbgeducation.application.common.oneof.oneoftypes.NotFound;
import com.play.java.bbgeducation.application.common.oneof.oneoftypes.Success;
import com.play.java.bbgeducation.application.common.validation.ValidationFailed;
import com.play.java.bbgeducation.application.sessions.create.SessionCreateCommand;
import com.play.java.bbgeducation.application.sessions.delete.SessionDeleteCommand;
import com.play.java.bbgeducation.application.sessions.getById.ProgramSessionGetByIdCommand;
import com.play.java.bbgeducation.application.sessions.getByProgram.ProgramSessionGetByProgramCommand;
import com.play.java.bbgeducation.application.sessions.result.SessionResult;
import com.play.java.bbgeducation.application.sessions.update.SessionUpdateCommand;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestResource
@RequestMapping("api/programs/{pid}/sessions")
@HasApiEndpoints
public class ProgramSessionResource {

    private final Pipeline pipeline;
    private final ProgramSessionLinkProvider programSessionLinkProvider;
    private final SessionCourseLinkProvider sessionCourseLinkProvider;
    private final ProgramLinkProvider programLinkProvider;

    public ProgramSessionResource(Pipeline pipeline, ProgramSessionLinkProvider programSessionLinkProvider, SessionCourseLinkProvider sessionCourseLinkProvider, ProgramLinkProvider programLinkProvider) {
        this.pipeline = pipeline;
        this.programSessionLinkProvider = programSessionLinkProvider;
        this.sessionCourseLinkProvider = sessionCourseLinkProvider;
        this.programLinkProvider = programLinkProvider;
    }

    @PostMapping(path="")
    public ResponseEntity createSession(
            @PathVariable("pid") Long programid,
            @RequestBody SessionRequest sessionRequest,
            HttpServletRequest httpRequest
    ) {

        SessionCreateCommand cmd = SessionCreateCommand.builder()
                .programId(programid)
                .name(sessionRequest.getName())
                .description(sessionRequest.getDescription())
                .startDate(sessionRequest.getStartDate())
                .endDate(sessionRequest.getEndDate())
                .practicumHours(sessionRequest.getPracticumHours())
                .build();

        OneOf3<SessionResult, NotFound, ValidationFailed> result = pipeline.send(cmd);

        return result.match(
                session -> new ResponseEntity<>(EntityModel.of(session)
                        .add(programSessionLinkProvider.getSelfLink(httpRequest))
                        .add(programSessionLinkProvider.getByIdLink(programid, session.getId(), false))
                        .add(programSessionLinkProvider.getByProgramLink(programid))
                        .add(programLinkProvider.getByIdLink(programid, false))
                        .add(programLinkProvider.getAllLink()),
                        HttpStatus.CREATED),
                notFound -> ResponseEntity.notFound().build(),
                fail -> ResponseEntity.of(fail.toProblemDetail("Error creating session"))
                        .build()
        );
    }

    @PutMapping(path="{sid}")
    public ResponseEntity updateSession(
            @PathVariable("pid") Long programid,
            @PathVariable("sid") Long sessionid,
            @RequestBody SessionRequest sessionRequest,
            HttpServletRequest httpRequest
    ) {
        SessionUpdateCommand command = SessionUpdateCommand.builder()
                .programId(programid)
                .id(sessionid)
                .name(sessionRequest.getName())
                .description(sessionRequest.getDescription())
                .startDate(sessionRequest.getStartDate())
                .endDate(sessionRequest.getEndDate())
                .practicumHours(sessionRequest.getPracticumHours())
                .build();

        OneOf3<Success, NotFound, ValidationFailed> result = pipeline.send(command);

        return result.match(
                success -> ResponseEntity.ok(new NoDataResponse()
                        .add(programSessionLinkProvider.getSelfLink(httpRequest))
                        .add(programSessionLinkProvider.getByIdLink(programid, sessionid,false))
                        .add(programSessionLinkProvider.getByProgramLink(programid))
                        ),
                notFound -> ResponseEntity.notFound().build(),
                fail -> ResponseEntity.of(fail.toProblemDetail("Error updating session"))
                        .build()
        );
    }

    @GetMapping(path="{sid}")
    public ResponseEntity getById(
            @PathVariable("pid") Long programid,
            @PathVariable("sid") Long sessionid,
            HttpServletRequest httpRequest
    ) {

        ProgramSessionGetByIdCommand command = ProgramSessionGetByIdCommand.builder()
                .programId(programid)
                .sessionId(sessionid)
                .build();

        OneOf2<SessionResult, NotFound> result = pipeline.send(command);

        return result.match(
                session -> ResponseEntity.ok(EntityModel.of(session)
                        .add(programSessionLinkProvider.getSelfLink(httpRequest))
                        .add(programSessionLinkProvider.getUpdateLink(programid, sessionid))
                        .add(programSessionLinkProvider.getDeleteLink(programid, sessionid))
                ),
                notfound -> ResponseEntity.notFound().build()
        );
    }

    @GetMapping(path="")
    public ResponseEntity getByProgram(
            @PathVariable("pid") Long programid,
            HttpServletRequest httpRequest
    ){

        ProgramSessionGetByProgramCommand command = ProgramSessionGetByProgramCommand.builder()
                .programId(programid)
                .build();

        OneOf2<List<SessionResult>, NotFound> sessionsList = pipeline.send(command);

        return sessionsList.match(
                sessions -> ResponseEntity.ok(buildCollectionModel(sessions, httpRequest)),
                notFound -> ResponseEntity.notFound().build()
        );
    }

    @DeleteMapping(path="{sid}")
    public ResponseEntity deleteSession(
            @PathVariable("pid") Long programid,
            @PathVariable("sid") Long sessionid,
            HttpServletRequest httpRequest
    ) {
        SessionDeleteCommand command = SessionDeleteCommand.builder()
                .programId(programid)
                .sessionId(sessionid)
                .build();
        OneOf2<Success, NotFound> result = pipeline.send(command);

        return result.match(
                success -> ResponseEntity.ok(EntityModel.of(new NoDataResponse())
                        .add(programSessionLinkProvider.getByProgramLink(programid))
                        .add(programLinkProvider.getByIdLink(programid, false))
                ),
                notFound -> ResponseEntity.notFound().build()
        );
    }

    CollectionModel<EntityModel<SessionResult>> buildCollectionModel(List<SessionResult> sessionList, HttpServletRequest httpRequest){
        return CollectionModel.of(buildEntityModelResultList( sessionList))
                .add(programSessionLinkProvider.getSelfLink(httpRequest))
                .add(programSessionLinkProvider.getCreateLink());


    }
    List<EntityModel<SessionResult>> buildEntityModelResultList(List<SessionResult> sessionList){
        return sessionList.stream()
                .map(s -> EntityModel.of(s)
                        .add(programSessionLinkProvider.getByIdLink(s.getProgram().getId(), s.getId(), true))
                        .add(programSessionLinkProvider.getDeleteLink(s.getProgram().getId(), s.getId()))
                ).toList();
    }
}
