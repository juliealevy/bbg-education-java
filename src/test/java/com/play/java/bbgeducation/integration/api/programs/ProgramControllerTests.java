package com.play.java.bbgeducation.integration.api.programs;

import an.awesome.pipelinr.Pipeline;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.play.java.bbgeducation.api.programs.ProgramRequest;
import com.play.java.bbgeducation.application.common.oneof.OneOf2;
import com.play.java.bbgeducation.application.common.validation.ValidationFailed;
import com.play.java.bbgeducation.application.programs.result.ProgramResult;
import com.play.java.bbgeducation.application.programs.create.ProgramCreateCommand;
import com.play.java.bbgeducation.infrastructure.auth.Roles;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static com.play.java.bbgeducation.integration.api.programs.DataUtils.*;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@AutoConfigureMockMvc
public class ProgramControllerTests {
    private MockMvc mockMvc;
    private WebApplicationContext webApplicationContext;
    private ObjectMapper objectMapper;
    private final Pipeline pipeline;

    private static final String PROGRAMS_PATH = "/api/programs";
    private static final String PROBLEM_JSON_TYPE = "application/problem+json";

    @Autowired
    public ProgramControllerTests(WebApplicationContext webApplicationContext, Pipeline pipeline) {
        this.webApplicationContext = webApplicationContext;
        this.pipeline = pipeline;
        this.objectMapper = new ObjectMapper();
    }

    @BeforeEach
    public void init(){
        this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).apply(springSecurity()).build();
    }

    @Test
    @WithMockUser(username="test", roles = {Roles.ADMIN, Roles.USER})
    public void ProgramCreate_Returns201_WhenAdminAndValid() throws Exception {
        ProgramRequest testRequest = buildRequestI();
        String programJson = objectMapper.writeValueAsString(testRequest);

        mockMvc.perform(MockMvcRequestBuilders.post(PROGRAMS_PATH)
                .contentType(MediaType.APPLICATION_JSON)
                .content(programJson)
        ).andDo(MockMvcResultHandlers.print()
        ).andExpect(
                MockMvcResultMatchers.status().isCreated()
        );
    }

    @Test
    @WithMockUser(username="test", roles = {Roles.USER})
    public void ProgramCreate_Returns403_WhenNotAdmin() throws Exception {
        ProgramRequest testRequest = buildRequestI();
        String programJson = objectMapper.writeValueAsString(testRequest);

        mockMvc.perform(MockMvcRequestBuilders.post(PROGRAMS_PATH)
                .contentType(MediaType.APPLICATION_JSON)
                .content(programJson)
        ).andDo(MockMvcResultHandlers.print()
        ).andExpect(
                MockMvcResultMatchers.status().isForbidden()
        );
    }

    @Test
    @WithMockUser(username="test", roles = {Roles.ADMIN, Roles.USER})
    public void ProgramCreate_ReturnsSavedProgram_WhenSuccess() throws Exception {
        ProgramRequest testRequest = buildRequestI();
        String programJson = objectMapper.writeValueAsString(testRequest);

        mockMvc.perform(MockMvcRequestBuilders.post(PROGRAMS_PATH)
                .contentType(MediaType.APPLICATION_JSON)
                .content(programJson)
        ).andDo(MockMvcResultHandlers.print()
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.id").isNumber()
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.name").value(testRequest.getName())
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.description").value(testRequest.getDescription())
        );
    }

    @Test
    @WithMockUser(username="test", roles = {Roles.ADMIN, Roles.USER})
    public void ProgramCreate_ReturnsProblemJsonConflict_WhenNameExists() throws Exception {
        ProgramCreateCommand createCommand1 = buildCreateCommandI();
        OneOf2<ProgramResult, ValidationFailed> savedProgram1 = pipeline.send(createCommand1);

        ProgramRequest createRequest = buildRequestI();

        String requestJson = objectMapper.writeValueAsString(createRequest);

        mockMvc.perform(MockMvcRequestBuilders.post(PROGRAMS_PATH)
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestJson)
        ).andDo(MockMvcResultHandlers.print()
        ).andExpect(
                MockMvcResultMatchers.status().isConflict()
        ).andExpect(
                MockMvcResultMatchers.content().contentType(PROBLEM_JSON_TYPE)
        );
    }
    @Test
    @WithMockUser(username="test", roles = {Roles.USER})
    public void ProgramGetAll_Returns200_WhenSuccess() throws Exception {

        mockMvc.perform(MockMvcRequestBuilders.get(PROGRAMS_PATH)
                .contentType(MediaType.APPLICATION_JSON)
        ).andDo(MockMvcResultHandlers.print()
        ).andExpect(
                MockMvcResultMatchers.status().isOk()
        );
    }

    @Test
    @WithMockUser(username="test", roles = {Roles.USER})
    public void ProgramGetAll_ReturnsList_WhenSuccess() throws Exception {
        ProgramResult saved1 = createAndSaveProgramI();
        ProgramResult saved2 = createAndSaveProgramII();

        mockMvc.perform(MockMvcRequestBuilders.get(PROGRAMS_PATH)
                .contentType(MediaType.APPLICATION_JSON)
        ).andDo(MockMvcResultHandlers.print()
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$._embedded.programs.[0].id").value(saved1.getId())
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$._embedded.programs.[0].name").value(saved1.getName())
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$._embedded.programs.[0].description").value(saved1.getDescription())
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$._embedded.programs.[1].id").value(saved2.getId())
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$._embedded.programs.[1].name").value(saved2.getName())
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$._embedded.programs.[1].description").value(saved2.getDescription())
        );
    }

    @Test
    @WithMockUser(username="test", roles = {Roles.USER})
    public void ProgramGetById_Returns200_WhenIdExists() throws Exception {
        ProgramResult program = createAndSaveProgramI();

        mockMvc.perform(MockMvcRequestBuilders.get(getProgramsPath(program.getId()))
                .contentType(MediaType.APPLICATION_JSON)
        ).andDo(MockMvcResultHandlers.print()
        ).andExpect(
                MockMvcResultMatchers.status().isOk()
        );
    }

    @Test
    @WithMockUser(username="test", roles = {Roles.USER})
    public void ProgramGetById_ReturnsProgram_WhenIdExists() throws Exception {
        ProgramResult program = createAndSaveProgramI();

        mockMvc.perform(MockMvcRequestBuilders.get(getProgramsPath(program.getId()))
                .contentType(MediaType.APPLICATION_JSON)
        ).andDo(MockMvcResultHandlers.print()
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.id").value(program.getId())
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.name").value(program.getName())
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.description").value(program.getDescription())
        );
    }

    @Test
    @WithMockUser(username="test", roles = {Roles.USER})
    public void ProgramGetById_Returns404_WhenIdNotExist() throws Exception {
        ProgramResult program = createAndSaveProgramI();

        mockMvc.perform(MockMvcRequestBuilders.get(getProgramsPath(program.getId() + 100L))
                .contentType(MediaType.APPLICATION_JSON)
        ).andDo(MockMvcResultHandlers.print()
        ).andExpect(
                MockMvcResultMatchers.status().isNotFound()
        );
    }

    @Test
    @WithMockUser(username="test", roles = {Roles.ADMIN})
    public void ProgramUpdate_Returns200_WhenInputValid() throws Exception {
        ProgramResult savedProgram = createAndSaveProgramI();

        ProgramRequest updateRequest = ProgramRequest.builder()
                .name(savedProgram.getName() + "updated")
                .description(savedProgram.getDescription() + " updated").build();

        String requestJson = objectMapper.writeValueAsString(updateRequest);

        mockMvc.perform(MockMvcRequestBuilders.put(getProgramsPath(savedProgram.getId()))
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestJson)
        ).andDo(MockMvcResultHandlers.print()
        ).andExpect(
                MockMvcResultMatchers.status().isOk()
        );
    }

    @Test
    @WithMockUser(username="test", roles = {Roles.ADMIN})
    public void ProgramUpdate_ReturnsNotFound_WhenIdNotExists() throws Exception {
        ProgramResult savedProgram1 = createAndSaveProgramI();

        ProgramRequest updateRequest = ProgramRequest.builder()
                .name(savedProgram1.getName())
                .description(savedProgram1.getDescription())
                .build();

        String requestJson = objectMapper.writeValueAsString(updateRequest);

        mockMvc.perform(MockMvcRequestBuilders.put(getProgramsPath(savedProgram1.getId() + 100L))
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestJson)
        ).andDo(MockMvcResultHandlers.print()
        ).andExpect(
                MockMvcResultMatchers.status().isNotFound()
        );
    }

    @Test
    @WithMockUser(username="test", roles = {Roles.ADMIN})
    public void ProgramUpdate_ReturnsProblemJsonConflict_WhenNameExists() throws Exception {
        ProgramResult savedProgram1 = createAndSaveProgramI();
        ProgramResult savedProgram2 = createAndSaveProgramII();

        ProgramRequest updateRequest = ProgramRequest.builder()
                .name(savedProgram2.getName())
                .description(savedProgram2.getDescription())
                .build();

        String requestJson = objectMapper.writeValueAsString(updateRequest);

        mockMvc.perform(MockMvcRequestBuilders.put(getProgramsPath(savedProgram1.getId()))
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestJson)
        ).andDo(MockMvcResultHandlers.print()
        ).andExpect(
                MockMvcResultMatchers.status().isConflict()
        ).andExpect(
                MockMvcResultMatchers.content().contentType(PROBLEM_JSON_TYPE)
        );
    }

    @Test
    @WithMockUser(username="test", roles = {Roles.ADMIN})
    public void ProgramDelete_ShouldReturnNoContent_WhenIdExists() throws Exception {
        ProgramResult savedProgram1 = createAndSaveProgramI();

        mockMvc.perform(MockMvcRequestBuilders.delete(getProgramsPath(savedProgram1.getId()))
                .contentType(MediaType.APPLICATION_JSON)
        ).andDo(MockMvcResultHandlers.print()
        ).andExpect(
                MockMvcResultMatchers.status().isNoContent()
        );
    }

    @Test
    @WithMockUser(username="test", roles = {Roles.ADMIN})
    public void ProgramDelete_ShouldReturnNotFound_WhenIdNotExists() throws Exception {
        ProgramResult savedProgram1 = createAndSaveProgramI();

        mockMvc.perform(MockMvcRequestBuilders.delete(getProgramsPath(savedProgram1.getId() + 100L))
                .contentType(MediaType.APPLICATION_JSON)
        ).andDo(MockMvcResultHandlers.print()
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

    private ProgramResult createAndSaveProgramI(){
        ProgramCreateCommand createCommand1 = buildCreateCommandI();
        OneOf2<ProgramResult, ValidationFailed> savedProgram1 = pipeline.send(createCommand1);
        return savedProgram1.asOption1();
    }
    private ProgramResult createAndSaveProgramII(){
        ProgramCreateCommand createCommand1 = buildCreateCommandII();
        OneOf2<ProgramResult, ValidationFailed> savedProgram1 = pipeline.send(createCommand1);
        return savedProgram1.asOption1();
    }
}
