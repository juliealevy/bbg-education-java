package com.play.java.bbgeducation.integration.sessions;

import an.awesome.pipelinr.Pipeline;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.play.java.bbgeducation.api.programs.ProgramRequest;
import com.play.java.bbgeducation.api.sessions.SessionRequest;
import com.play.java.bbgeducation.application.common.oneof.OneOf2;
import com.play.java.bbgeducation.application.common.oneof.OneOf3;
import com.play.java.bbgeducation.application.common.oneof.oneoftypes.NotFound;
import com.play.java.bbgeducation.application.common.validation.ValidationFailed;
import com.play.java.bbgeducation.application.programs.create.ProgramCreateCommand;
import com.play.java.bbgeducation.application.programs.result.ProgramResult;
import com.play.java.bbgeducation.application.sessions.create.SessionCreateCommand;
import com.play.java.bbgeducation.application.sessions.getById.SessionGetByIdCommand;
import com.play.java.bbgeducation.application.sessions.result.SessionResult;
import com.play.java.bbgeducation.infrastructure.auth.Roles;
import org.hibernate.query.sqm.DynamicInstantiationNature;
import org.instancio.Instancio;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import static com.play.java.bbgeducation.integration.programs.DataUtils.buildCreateCommandI;
import static com.play.java.bbgeducation.integration.programs.DataUtils.buildRequestI;
import static org.instancio.Select.field;
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
        ProgramCreateCommand programCreateCmd = Instancio.create(ProgramCreateCommand.class);
        OneOf2<ProgramResult, ValidationFailed> savedProgram = pipeline.send(programCreateCmd);

        SessionRequest request = Instancio.create(SessionRequest.class);
        String requestJson = objectMapper.writeValueAsString(request);

        mockMvc.perform(MockMvcRequestBuilders.post(String.format(PROGRAM_SESSIONS_PATH, savedProgram.asOption1().getId()))
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestJson)
        ).andExpect(
                MockMvcResultMatchers.status().isCreated()
        );
    }

    @Test
    @WithMockUser(username="test", roles = {Roles.USER})
    public void SessionCreate_Returns403_WhenNotAdmin() throws Exception {
        ProgramCreateCommand programCreateCmd = Instancio.create(ProgramCreateCommand.class);
        OneOf2<ProgramResult, ValidationFailed> savedProgram = pipeline.send(programCreateCmd);

        SessionRequest request = Instancio.create(SessionRequest.class);
        String requestJson = objectMapper.writeValueAsString(request);

        mockMvc.perform(MockMvcRequestBuilders.post(String.format(PROGRAM_SESSIONS_PATH, savedProgram.asOption1().getId()))
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestJson)
        ).andExpect(
                MockMvcResultMatchers.status().isForbidden()
        );
    }

    @Test
    @WithMockUser(username="test", roles = {Roles.ADMIN, Roles.USER})
    public void SessionCreate_ReturnsResult_WhenSuccess() throws Exception {
        ProgramCreateCommand programCreateCmd = Instancio.create(ProgramCreateCommand.class);
        OneOf2<ProgramResult, ValidationFailed> savedProgram = pipeline.send(programCreateCmd);

        SessionRequest request = Instancio.create(SessionRequest.class);
        String requestJson = objectMapper.writeValueAsString(request);

        mockMvc.perform(MockMvcRequestBuilders.post(String.format(PROGRAM_SESSIONS_PATH, savedProgram.asOption1().getId()))
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
        );
    }

    @Test
    @WithMockUser(username="test", roles = {Roles.ADMIN, Roles.USER})
    public void SessionCreate_ReturnsProblemJsonConflict_WhenNameExists() throws Exception {
        ProgramCreateCommand programCreateCmd = Instancio.create(ProgramCreateCommand.class);
        OneOf2<ProgramResult, ValidationFailed> savedProgram = pipeline.send(programCreateCmd);

        SessionRequest firstRequest = Instancio.create(SessionRequest.class);
        String requestJson = objectMapper.writeValueAsString(firstRequest);

        mockMvc.perform(MockMvcRequestBuilders.post(String.format(PROGRAM_SESSIONS_PATH, savedProgram.asOption1().getId()))
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestJson)
        ).andExpect(
                MockMvcResultMatchers.status().isCreated()
        );

        SessionRequest secondRequest = Instancio.create(SessionRequest.class);
        secondRequest.setName(firstRequest.getName());
        requestJson = objectMapper.writeValueAsString(secondRequest);

        mockMvc.perform(MockMvcRequestBuilders.post(String.format(PROGRAM_SESSIONS_PATH, savedProgram.asOption1().getId()))
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestJson)
        ).andExpect(
                MockMvcResultMatchers.status().isConflict()
        ).andExpect(
                MockMvcResultMatchers.content().contentType(PROBLEM_JSON_TYPE)
        );

    }



}
