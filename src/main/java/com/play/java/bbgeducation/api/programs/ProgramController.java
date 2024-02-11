package com.play.java.bbgeducation.api.programs;

import an.awesome.pipelinr.Pipeline;
import com.play.java.bbgeducation.api.common.NoDataResponse;
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
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/programs")
public class ProgramController {

    private final Pipeline pipeline;
    private final ProgramsLinkProvider linkProvider;

    public ProgramController(Pipeline pipeline, ProgramsLinkProvider linkProvider) {
        this.pipeline = pipeline;
        this.linkProvider = linkProvider;
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
                fail -> new ResponseEntity<>(ProblemDetail.forStatusAndDetail(HttpStatus.CONFLICT, fail.getErrorMessage()),
                        HttpStatus.CONFLICT)
        );


    }
    @PutMapping(path="{id}")
    public ResponseEntity updateProgram(
            @PathVariable("id") Long id,
            @RequestBody ProgramRequest programRequest,
            HttpServletRequest httpRequest)  {

        ProgramUpdateCommand command = ProgramUpdateCommand.builder()
                .id(id)
                .name(programRequest.getName())
                .description(programRequest.getDescription())
                .build();

        OneOf3<Success, NotFound, ValidationFailed> updated = pipeline.send(command);

        return updated.match(
                success -> {
                    return new ResponseEntity<>(new NoDataResponse()
                            .add(linkProvider.getSelfLink(httpRequest))
                            .add(linkProvider.getByIdLink(id,false))
                            .add(linkProvider.getAllLink()),
                            HttpStatus.OK);
                },
                notfound -> new ResponseEntity<>(HttpStatus.NOT_FOUND),
                fail -> new ResponseEntity<>(ProblemDetail.forStatusAndDetail(HttpStatus.CONFLICT, fail.getErrorMessage()),
                        HttpStatus.CONFLICT)
        );

    }

    @GetMapping(path="{id}")
    ResponseEntity getById (
            @PathVariable("id") Long id,
            HttpServletRequest request) {

        OneOf2<ProgramResult, NotFound> result = pipeline.send(ProgramGetByIdCommand.builder()
                .id(id)
                .build());

        return result.match(
                program -> {
                    return new ResponseEntity<>(EntityModel.of(program)
                            .add(Link.of(request.getRequestURI()).withSelfRel())
                            .add(linkProvider.getUpdateLink(id))
                            .add(linkProvider.getDeleteLink(id)),
                            HttpStatus.OK);
                },
                notFound -> new ResponseEntity<>(HttpStatus.NOT_FOUND)
        );
    }

    @GetMapping(path="")
    ResponseEntity getAll(
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

        return new ResponseEntity<>(programRep, HttpStatus.OK);
    }

    @DeleteMapping(path="{id}")
    ResponseEntity deleteProgramById(
            @PathVariable("id") Long id,
            HttpServletRequest httpRequest) {

        OneOf2<Success, NotFound> deleted = pipeline.send(
                ProgramDeleteByIdCommand.builder()
                .id(id)
                .build());

        //Does this need any links returned (like getall)?  or keep at no content
        return deleted.match(
                success -> new ResponseEntity<>(HttpStatus.NO_CONTENT),
                notFound -> new ResponseEntity<>(HttpStatus.NOT_FOUND)
        );

    }



}
