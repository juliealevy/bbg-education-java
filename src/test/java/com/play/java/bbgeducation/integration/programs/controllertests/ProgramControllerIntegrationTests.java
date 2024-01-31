package com.play.java.bbgeducation.integration.programs.controllertests;

import an.awesome.pipelinr.Pipeline;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.play.java.bbgeducation.api.programs.ProgramRequest;
import com.play.java.bbgeducation.application.programs.ProgramResult;
import com.play.java.bbgeducation.application.programs.commands.ProgramCreateCommand;
import com.play.java.bbgeducation.application.programs.commands.ProgramUpdateCommand;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static com.play.java.bbgeducation.integration.programs.DataUtils.*;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@AutoConfigureMockMvc
public class ProgramControllerIntegrationTests {
    private MockMvc mockMvc;
    private ObjectMapper objectMapper;
    private final Pipeline pipeline;

    private static final String PROGRAMS_PATH = "/api/programs";
    private static final String PROBLEM_JSON_TYPE = "application/problem+json";

    @Autowired
    public ProgramControllerIntegrationTests(MockMvc mockMvc, Pipeline pipeline) {
        this.mockMvc = mockMvc;
        this.pipeline = pipeline;
        this.objectMapper = new ObjectMapper();
    }

    @Test
    public void ProgramCreate_Returns201_WhenSuccess() throws Exception {
        ProgramRequest testRequest = buildRequestI();
        String programJson = objectMapper.writeValueAsString(testRequest);

        mockMvc.perform(MockMvcRequestBuilders.post(PROGRAMS_PATH)
                .contentType(MediaType.APPLICATION_JSON)
                .content(programJson)
        ).andExpect(
                MockMvcResultMatchers.status().isCreated()
        );
    }

    @Test
    public void ProgramCreate_ReturnsSavedProgram_WhenSuccess() throws Exception {
        ProgramRequest testRequest = buildRequestI();
        String programJson = objectMapper.writeValueAsString(testRequest);

        mockMvc.perform(MockMvcRequestBuilders.post(PROGRAMS_PATH)
                .contentType(MediaType.APPLICATION_JSON)
                .content(programJson)
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.id").isNumber()
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.name").value(testRequest.getName())
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.description").value(testRequest.getDescription())
        );
    }

    @Test
    public void ProgramCreate_ReturnsProblemJsonConflict_WhenNameExists() throws Exception {
        ProgramCreateCommand createCommand1 = buildCreateCommandI();
        ProgramResult savedProgram1 = pipeline.send(createCommand1);

        ProgramRequest createRequest = buildRequestI();

        String requestJson = objectMapper.writeValueAsString(createRequest);

        mockMvc.perform(MockMvcRequestBuilders.post(PROGRAMS_PATH)
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestJson)
        ).andExpect(
                MockMvcResultMatchers.status().isConflict()
        ).andExpect(
                MockMvcResultMatchers.content().contentType(PROBLEM_JSON_TYPE)
        );
    }
    @Test
    public void ProgramGetAll_Returns200_WhenSuccess() throws Exception {

        mockMvc.perform(MockMvcRequestBuilders.get(PROGRAMS_PATH)
                .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(
                MockMvcResultMatchers.status().isOk()
        );
    }

    @Test
    public void ProgramGetAll_ReturnsList_WhenSuccess() throws Exception {
        ProgramCreateCommand createCommand1 = buildCreateCommandI();
        ProgramResult saved1 = pipeline.send(createCommand1);
        ProgramCreateCommand createCommand2 = buildCreateCommandII();
        ProgramResult saved2 = pipeline.send(createCommand2);

        mockMvc.perform(MockMvcRequestBuilders.get(PROGRAMS_PATH)
                .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$[0].id").value(saved1.getId())
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$[0].name").value(saved1.getName())
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$[0].description").value(saved1.getDescription())
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$[1].id").value(saved2.getId())
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$[1].name").value(saved2.getName())
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$[1].description").value(saved2.getDescription())
        );
    }

    @Test
    public void ProgramGetById_Returns200_WhenIdExists() throws Exception {
        ProgramCreateCommand createCommand1 = buildCreateCommandI();
        ProgramResult program = pipeline.send(createCommand1);

        mockMvc.perform(MockMvcRequestBuilders.get(getProgramsPath(program.getId()))
                .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(
                MockMvcResultMatchers.status().isOk()
        );
    }

    @Test
    public void ProgramGetById_ReturnsProgram_WhenIdExists() throws Exception {
        ProgramCreateCommand createCommand1 = buildCreateCommandI();
        ProgramResult program = pipeline.send(createCommand1);

        mockMvc.perform(MockMvcRequestBuilders.get(getProgramsPath(program.getId()))
                .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.id").value(program.getId())
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.name").value(program.getName())
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.description").value(program.getDescription())
        );
    }

    @Test
    public void ProgramGetById_Returns404_WhenIdNotExist() throws Exception {
        ProgramCreateCommand createCommand1 = buildCreateCommandI();
        ProgramResult program = pipeline.send(createCommand1);

        mockMvc.perform(MockMvcRequestBuilders.get(getProgramsPath(program.getId() + 100L))
                .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(
                MockMvcResultMatchers.status().isNotFound()
        );
    }

    @Test
    public void ProgramUpdate_Returns200_WhenInputValid() throws Exception {
        ProgramCreateCommand createCommand1 = buildCreateCommandI();
        ProgramResult savedProgram = pipeline.send(createCommand1);

        ProgramRequest updateRequest = ProgramRequest.builder()
                .name(savedProgram.getName() + "updated")
                .description(savedProgram.getDescription() + " updated").build();

        String requestJson = objectMapper.writeValueAsString(updateRequest);

        mockMvc.perform(MockMvcRequestBuilders.put(getProgramsPath(savedProgram.getId()))
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestJson)
        ).andExpect(
                MockMvcResultMatchers.status().isOk()
        );
    }

    @Test
    public void ProgramUpdate_ReturnsUpdatedProgram_WhenInputValid() throws Exception {
        ProgramCreateCommand createCommand1 = buildCreateCommandI();
        ProgramResult savedProgram = pipeline.send(createCommand1);

        ProgramRequest updateRequest = ProgramRequest.builder()
                .name(savedProgram.getName() + "updated")
                .description(savedProgram.getDescription() + " updated").build();

        String requestJson = objectMapper.writeValueAsString(updateRequest);

        mockMvc.perform(MockMvcRequestBuilders.put(getProgramsPath(savedProgram.getId()))
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestJson)
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.id").value(savedProgram.getId())
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.name").value(updateRequest.getName())
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.description").value(updateRequest.getDescription())
        );
    }

    @Test
    public void ProgramUpdate_ReturnsNotFound_WhenIdNotExists() throws Exception {
        ProgramCreateCommand createCommand1 = buildCreateCommandI();
        ProgramResult savedProgram1 = pipeline.send(createCommand1);

        ProgramRequest updateRequest = ProgramRequest.builder()
                .name(savedProgram1.getName())
                .description(savedProgram1.getDescription())
                .build();

        String requestJson = objectMapper.writeValueAsString(updateRequest);

        mockMvc.perform(MockMvcRequestBuilders.put(getProgramsPath(savedProgram1.getId() + 100L))
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestJson)
        ).andExpect(
                MockMvcResultMatchers.status().isNotFound()
        );
    }

    @Test
    public void ProgramUpdate_ReturnsProblemJsonConflict_WhenNameExists() throws Exception {
        ProgramCreateCommand createCommand1 = buildCreateCommandI();
        ProgramResult savedProgram1 = pipeline.send(createCommand1);
        ProgramCreateCommand createCommand2 = buildCreateCommandII();
        ProgramResult savedProgram2 = pipeline.send(createCommand2);

        ProgramRequest updateRequest = ProgramRequest.builder()
                .name(savedProgram2.getName())
                .description(savedProgram2.getDescription())
                .build();

        String requestJson = objectMapper.writeValueAsString(updateRequest);

        mockMvc.perform(MockMvcRequestBuilders.put(getProgramsPath(savedProgram1.getId()))
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestJson)
        ).andExpect(
                MockMvcResultMatchers.status().isConflict()
        ).andExpect(
                MockMvcResultMatchers.content().contentType(PROBLEM_JSON_TYPE)
        );
    }

    @Test
    public void ProgramDelete_ShouldReturnNoContent_WhenIdExists() throws Exception {
        ProgramCreateCommand createCommand1 = buildCreateCommandI();
        ProgramResult savedProgram1 = pipeline.send(createCommand1);

        mockMvc.perform(MockMvcRequestBuilders.delete(getProgramsPath(savedProgram1.getId()))
                .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(
                MockMvcResultMatchers.status().isNoContent()
        );
    }

    @Test
    public void ProgramDelete_ShouldReturnNotFound_WhenIdNotExists() throws Exception {
        ProgramCreateCommand createCommand1 = buildCreateCommandI();
        ProgramResult savedProgram1 = pipeline.send(createCommand1);

        mockMvc.perform(MockMvcRequestBuilders.delete(getProgramsPath(savedProgram1.getId() + 100L))
                .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(
                MockMvcResultMatchers.status().isNotFound()
        );
    }


    private String getProgramsPath(Long programId){
        String path = PROGRAMS_PATH;
        if (programId != null){
            path += ("/" + programId);
        }
        return path;
    }
}
