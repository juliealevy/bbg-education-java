package com.play.java.bbgeducation.api.programs;

import an.awesome.pipelinr.Pipeline;
import com.play.java.bbgeducation.application.oneof.OneOf2;
import com.play.java.bbgeducation.application.exceptions.ValidationFailed;
import com.play.java.bbgeducation.application.oneof.OneOf3;
import com.play.java.bbgeducation.application.oneof.OneOfTypes;
import com.play.java.bbgeducation.application.programs.commands.*;
import com.play.java.bbgeducation.application.programs.ProgramResult;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("api/programs")
public class ProgramController {

    private final Pipeline pipeline;

    public ProgramController(Pipeline pipeline) {
        this.pipeline = pipeline;
    }

    @PostMapping(path="")
    public ResponseEntity createProgram(
        @RequestBody ProgramRequest programRequest)  {

        ProgramCreateCommand command = ProgramCreateCommand.builder()
                .name(programRequest.getName())
                .description(programRequest.getDescription())
                .build();

        OneOf2<ProgramResult, ValidationFailed> createdProgram = pipeline.send(command);
        return createdProgram.match(
                program -> new ResponseEntity<>(program, HttpStatus.CREATED),
                fail -> new ResponseEntity<>(ProblemDetail.forStatusAndDetail(HttpStatus.CONFLICT, fail.getErrorMessage()),
                        HttpStatus.CONFLICT)
        );


    }

    @PutMapping(path="{id}")
    public ResponseEntity updateProgram(
            @PathVariable("id") Long id,
            @RequestBody ProgramRequest programRequest)  {

        ProgramUpdateCommand command = ProgramUpdateCommand.builder()
                .id(id)
                .name(programRequest.getName())
                .description(programRequest.getDescription())
                .build();

        OneOf3<OneOfTypes.Success, OneOfTypes.NotFound, ValidationFailed> updated = pipeline.send(command);

        return updated.match(
                success -> new ResponseEntity<>(HttpStatus.OK),
                notfound -> new ResponseEntity<>(HttpStatus.NOT_FOUND),
                fail -> new ResponseEntity<>(ProblemDetail.forStatusAndDetail(HttpStatus.CONFLICT, fail.getErrorMessage()),
                        HttpStatus.CONFLICT)
        );

    }

    @GetMapping(path="{id}")
    ResponseEntity<ProgramResult> getById (
            @PathVariable("id") Long id) {

        Optional<ProgramResult> results = pipeline.send(ProgramGetByIdCommand.builder()
                .id(id)
                .build());

        return results.map(programResult -> new ResponseEntity<>(programResult, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @GetMapping(path="")
    ResponseEntity<List<ProgramResult>> getAll(){
        List<ProgramResult> programs = pipeline.send(ProgramGetAllCommand.builder().build());
        return new ResponseEntity<>(programs, HttpStatus.OK);
    }

    @DeleteMapping(path="{id}")
    ResponseEntity<Object> deleteProgramById(
            @PathVariable("id") Long id) {

        Optional<ProgramResult> deleted = pipeline.send(ProgramDeleteByIdCommand.builder()
                .id(id)
                .build());

        return deleted.map(programResult -> new ResponseEntity<>(HttpStatus.NO_CONTENT))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));

    }


}
