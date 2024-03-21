package com.play.java.bbgeducation.api.programs;

import an.awesome.pipelinr.Pipeline;
import com.play.java.bbgeducation.api.common.NoDataResponse;
import com.play.java.bbgeducation.api.common.RestResource;
import com.play.java.bbgeducation.api.endpoints.HasApiEndpoints;
import com.play.java.bbgeducation.api.programs.links.ProgramLinkProvider;
import com.play.java.bbgeducation.api.sessions.links.ProgramSessionLinkProvider;
import com.play.java.bbgeducation.application.common.oneof.OneOf2;
import com.play.java.bbgeducation.application.common.oneof.OneOf3;
import com.play.java.bbgeducation.application.common.oneof.oneoftypes.NotFound;
import com.play.java.bbgeducation.application.common.oneof.oneoftypes.Success;
import com.play.java.bbgeducation.application.common.validation.ValidationFailed;
import com.play.java.bbgeducation.application.programs.create.ProgramCreateCommand;
import com.play.java.bbgeducation.application.programs.delete.ProgramDeleteByIdCommand;
import com.play.java.bbgeducation.application.programs.getAll.ProgramGetAllCommand;
import com.play.java.bbgeducation.application.programs.getById.ProgramGetByIdCommand;
import com.play.java.bbgeducation.application.programs.result.ProgramResult;
import com.play.java.bbgeducation.application.programs.update.ProgramUpdateCommand;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestResource
@RequestMapping("api/programs")
@HasApiEndpoints
public class ProgramResource {

    private final Pipeline pipeline;
    private final ProgramLinkProvider linkProvider;
    private final ProgramSessionLinkProvider sessionLinkProvider;

    public ProgramResource(Pipeline pipeline, ProgramLinkProvider linkProvider, ProgramSessionLinkProvider sessionLinkProvider) {
        this.pipeline = pipeline;
        this.linkProvider = linkProvider;
        this.sessionLinkProvider = sessionLinkProvider;
    }

    @PostMapping(path="")
    public ResponseEntity createProgram(
            @RequestBody ProgramRequest programRequest,
            HttpServletRequest httpRequest)  {

        ProgramCreateCommand command = ProgramCreateCommand.builder()
                .name(programRequest.getName())
                .description(programRequest.getDescription())
                .build();

        OneOf2<ProgramResult, ValidationFailed> createdProgram = pipeline.send(command);


        return createdProgram.match(
                program -> {
                    EntityModel<ProgramResult> representation = EntityModel.of(program)
                            .add(Link.of(httpRequest.getRequestURI()).withSelfRel())
                            .add(linkProvider.getByIdLink(program.getId(),false))
                            .add(linkProvider.getAllLink());

                    return new ResponseEntity<>(representation, HttpStatus.CREATED);
                },
                fail -> ResponseEntity.of(fail.toProblemDetail("Error creating program"))
                        .build()
        );


    }
    @PutMapping(path="{pid}")
    public ResponseEntity updateProgram(
            @PathVariable("pid") Long id,
            @RequestBody ProgramRequest programRequest,
            HttpServletRequest httpRequest) {

        ProgramUpdateCommand command = ProgramUpdateCommand.builder()
                .id(id)
                .name(programRequest.getName())
                .description(programRequest.getDescription())
                .build();

        OneOf3<Success, NotFound, ValidationFailed> updated = pipeline.send(command);

        return updated.match(
                success -> ResponseEntity.ok(new NoDataResponse()
                        .add(linkProvider.getSelfLink(httpRequest))
                        .add(linkProvider.getByIdLink(id, false))
                        .add(linkProvider.getAllLink())
                ),
                notfound -> ResponseEntity.notFound().build(),
                fail -> ResponseEntity.of(fail.toProblemDetail("Error updating program"))
                        .build()

        );

    }

    @GetMapping(path="{pid}")
    public ResponseEntity getById (
            @PathVariable("pid") Long id,
            HttpServletRequest request) {

        OneOf2<ProgramResult, NotFound> result = pipeline.send(ProgramGetByIdCommand.builder()
                .id(id)
                .build());

        return result.match(
                program -> ResponseEntity.ok(EntityModel.of(program)
                        .add(linkProvider.getSelfLink(request))
                        .add(linkProvider.getUpdateLink(id))
                        .add(linkProvider.getDeleteLink(id))
                        .add(sessionLinkProvider.getByProgramLink(id))),
                notFound -> ResponseEntity.notFound().build()
        );
    }

    @GetMapping(path="")
    public ResponseEntity getAll(
            HttpServletRequest httpRequest) {

        List<ProgramResult> programs = pipeline.send(ProgramGetAllCommand.builder().build());

        List<EntityModel<ProgramResult>> items = programs.stream()
                .map(p -> {
                    return EntityModel.of(p)
                            .add(linkProvider.getByIdLink(p.getId(),true))
                            .add(linkProvider.getDeleteLink(p.getId()));
                }).toList();

        CollectionModel<EntityModel<ProgramResult>> programRep = CollectionModel.of(items)
                .add(linkProvider.getSelfLink(httpRequest))
                .add(linkProvider.getCreateLink());

        return ResponseEntity.ok(programRep);
    }

    @DeleteMapping(path="{pid}")
    public ResponseEntity deleteProgramById(
            @PathVariable("pid") Long id,
            HttpServletRequest httpRequest) {

        OneOf2<Success, NotFound> deleted = pipeline.send(
                ProgramDeleteByIdCommand.builder()
                        .id(id)
                        .build());

        return deleted.match(
                success -> ResponseEntity.ok(EntityModel.of(new NoDataResponse())
                        .add(linkProvider.getAllLink())),
                notFound -> ResponseEntity.notFound().build()
        );

    }

}
