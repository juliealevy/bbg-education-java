package com.play.java.bbgeducation.integration.sessions;

import an.awesome.pipelinr.Pipeline;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.play.java.bbgeducation.api.sessions.SessionRequest;
import com.play.java.bbgeducation.application.programs.create.ProgramCreateCommand;
import com.play.java.bbgeducation.application.programs.result.ProgramResult;
import com.play.java.bbgeducation.application.sessions.create.SessionCreateCommand;
import com.play.java.bbgeducation.application.sessions.result.SessionResult;
import com.play.java.bbgeducation.infrastructure.auth.Roles;
import org.instancio.Instancio;
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
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.time.LocalDate;

import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@AutoConfigureMockMvc
public class ProgramSessionControllerIntegrationTests {
    private MockMvc mockMvc;
    private WebApplicationContext webApplicationContext;
    private ObjectMapper objectMapper;
    private final Pipeline pipeline;

    private static final String PROGRAM_SESSIONS_PATH = "/api/programs/%s/sessions";
    private static final String PROGRAM_SESSIONS_ID_PATH = "/api/programs/%s/sessions/%s";
    private static final String PROBLEM_JSON_TYPE = "application/problem+json";

    @Autowired
    public ProgramSessionControllerIntegrationTests(WebApplicationContext webApplicationContext, Pipeline pipeline) {
        this.webApplicationContext = webApplicationContext;
        this.pipeline = pipeline;
        this.objectMapper = JsonMapper.builder()
                .addModule(new JavaTimeModule())
                .build();
    }

    @BeforeEach
    public void init(){
        this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).apply(springSecurity()).build();
    }

    @Test
    @WithMockUser(username="test", roles = {Roles.ADMIN, Roles.USER})
    public void SessionCreate_Returns201_WhenAdminAndValid() throws Exception {
        ProgramResult savedProgram = createAndSaveProgram();

        SessionRequest request = createSessionRequest();
        String requestJson = objectMapper.writeValueAsString(request);

        mockMvc.perform(MockMvcRequestBuilders.post(String.format(PROGRAM_SESSIONS_PATH, savedProgram.getId()))
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestJson)
        ).andExpect(
                MockMvcResultMatchers.status().isCreated()
        );
    }

    @Test
    @WithMockUser(username="test", roles = {Roles.USER})
    public void SessionCreate_Returns403_WhenNotAdmin() throws Exception {
        ProgramResult savedProgram = createAndSaveProgram();

        SessionRequest request = createSessionRequest();
        String requestJson = objectMapper.writeValueAsString(request);

        mockMvc.perform(MockMvcRequestBuilders.post(String.format(PROGRAM_SESSIONS_PATH, savedProgram.getId()))
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestJson)
        ).andExpect(
                MockMvcResultMatchers.status().isForbidden()
        );
    }

    @Test
    @WithMockUser(username="test", roles = {Roles.ADMIN, Roles.USER})
    public void SessionCreate_ReturnsResult_WhenSuccess() throws Exception {
        ProgramResult savedProgram = createAndSaveProgram();

        SessionRequest request = createSessionRequest();
        String requestJson = objectMapper.writeValueAsString(request);

        mockMvc.perform(MockMvcRequestBuilders.post(String.format(PROGRAM_SESSIONS_PATH, savedProgram.getId()))
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestJson)
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.id").isNumber()
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.name").value(request.getName())
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.description").value(request.getDescription())
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.startDate").value(request.getStartDateStr())
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.endDate").value(request.getEndDateStr())
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.practicumHours").value(request.getPracticumHours())
        );
    }

    @Test
    @WithMockUser(username="test", roles = {Roles.ADMIN, Roles.USER})
    public void SessionCreate_ReturnsProblemJsonConflict_WhenNameExists() throws Exception {
        ProgramResult savedProgram = createAndSaveProgram();
        SessionResult savedSession = createAndSaveSession(savedProgram.getId());

        SessionRequest createRequest = createSessionRequest();
        createRequest.setName(savedSession.getName());

        String requestJson = objectMapper.writeValueAsString(createRequest);

        mockMvc.perform(MockMvcRequestBuilders.post(String.format(PROGRAM_SESSIONS_PATH, savedProgram.getId()))
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestJson)
        ).andExpect(
                MockMvcResultMatchers.status().isConflict()
        ).andExpect(
                MockMvcResultMatchers.content().contentType(PROBLEM_JSON_TYPE)
        );

    }


    @Test
    @WithMockUser(username="test", roles = {Roles.USER})
    public void ProgramSessionGetById_Returns200_WhenIdsExist() throws Exception {
        ProgramResult savedProgram = createAndSaveProgram();
        SessionResult savedSession = createAndSaveSession(savedProgram.getId());

        String urlTemplate = String.format(PROGRAM_SESSIONS_ID_PATH, savedProgram.getId(), savedSession.getId());

        mockMvc.perform(MockMvcRequestBuilders.get(urlTemplate)
                .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(
                MockMvcResultMatchers.status().isOk()
        );
    }

    @Test
    @WithMockUser(username="test", roles = {Roles.USER})
    public void ProgramSessionGetById_ReturnsSession_WhenIdsExist() throws Exception {
        ProgramResult savedProgram = createAndSaveProgram();
        SessionResult savedSession = createAndSaveSession(savedProgram.getId());

        String urlTemplate = String.format(PROGRAM_SESSIONS_ID_PATH, savedProgram.getId(), savedSession.getId());

        mockMvc.perform(MockMvcRequestBuilders.get(urlTemplate)
                .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.id").isNumber()
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.name").value(savedSession.getName())
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.description").value(savedSession.getDescription())
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.startDate").value(savedSession.getStartDateStr())
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.endDate").value(savedSession.getEndDateStr())
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.practicumHours").value(savedSession.getPracticumHours())
        );
    }

    @Test
    @WithMockUser(username="test", roles = {Roles.USER})
    public void ProgramSessionGetById_Returns404_WhenIdsNotExist() throws Exception {
        ProgramResult savedProgram = createAndSaveProgram();

        String urlTemplate = String.format(PROGRAM_SESSIONS_ID_PATH, savedProgram.getId(), Instancio.create(Long.class));

        mockMvc.perform(MockMvcRequestBuilders.get(urlTemplate)
                .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(
                MockMvcResultMatchers.status().isNotFound()
        );
    }

    private SessionResult createAndSaveSession(Long programId){
        SessionCreateCommand sessionCreateCmd = SessionCreateCommand.builder()
                .programId(programId)
                .name(Instancio.create(String.class))
                .description(Instancio.create(String.class))
                .startDate(LocalDate.now().plusMonths(1))
                .endDate(LocalDate.now().plusMonths(7))
                .practicumHours(Instancio.create(int.class))
                .build();

        return pipeline.send(sessionCreateCmd).asOption1();
    }


    private ProgramResult createAndSaveProgram(){
        ProgramCreateCommand programCreateCmd = Instancio.create(ProgramCreateCommand.class);
        return pipeline.send(programCreateCmd).asOption1();
    }

    private SessionRequest createSessionRequest(){
        return SessionRequest.builder()
                .name(Instancio.of(String.class).stream().limit(50).toString())
                .description(Instancio.of(String.class).stream().limit(255).toString())
                .startDate(LocalDate.now().plusMonths(1))
                .endDate(LocalDate.now().plusMonths(7))
                .practicumHours(Instancio.create(int.class))
                .build();
    }
}
