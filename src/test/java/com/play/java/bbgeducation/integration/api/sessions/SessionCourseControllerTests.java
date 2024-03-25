package com.play.java.bbgeducation.integration.api.sessions;

import an.awesome.pipelinr.Pipeline;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.play.java.bbgeducation.application.common.oneof.OneOf2;
import com.play.java.bbgeducation.application.common.oneof.OneOf3;
import com.play.java.bbgeducation.application.common.oneof.oneoftypes.NotFound;
import com.play.java.bbgeducation.application.common.validation.ValidationFailed;
import com.play.java.bbgeducation.application.courses.create.CourseCreateCommand;
import com.play.java.bbgeducation.application.courses.results.CourseResult;
import com.play.java.bbgeducation.application.programs.create.ProgramCreateCommand;
import com.play.java.bbgeducation.application.programs.result.ProgramResult;
import com.play.java.bbgeducation.application.sessions.create.SessionCreateCommand;
import com.play.java.bbgeducation.application.sessions.result.SessionResult;
import com.play.java.bbgeducation.domain.programs.ProgramEntity;
import com.play.java.bbgeducation.domain.programs.SessionEntity;
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
public class SessionCourseControllerTests {
    private MockMvc mockMvc;
    private final WebApplicationContext webApplicationContext;
    private final ObjectMapper objectMapper;
    private final Pipeline pipeline;

    private static final String SESSION_COURSE_ID_PATH = "/api/programs/%s/sessions/%s/courses/%s";
    private static final String PROBLEM_JSON_TYPE = "application/problem+json";
    private SessionResult session;
    private CourseResult course;

    @Autowired
    public SessionCourseControllerTests(WebApplicationContext webApplicationContext, Pipeline pipeline) {
        this.webApplicationContext = webApplicationContext;
        this.pipeline = pipeline;
        this.objectMapper = JsonMapper.builder()
                .addModule(new JavaTimeModule())
                .build();
    }

    @BeforeEach
    public void init(){
        this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).apply(springSecurity()).build();
        session = createAndSaveProgramSession();
         course = createAndSaveCourse();
    }

    @Test
    @WithMockUser(username="test", roles = {Roles.ADMIN, Roles.USER})
    public void SessionCourseAdd_ReturnsOk_WhenAdminAndValid() throws Exception {

        mockMvc.perform(MockMvcRequestBuilders.post(String.format(SESSION_COURSE_ID_PATH, session.getProgram().getId(),
                        session.getId(), course.getId()))
                .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(
                MockMvcResultMatchers.status().isOk()
        );
    }

    @Test
    @WithMockUser(username="test", roles = {Roles.USER})
    public void SessionCourseAdd_ReturnsForbidden_WhenNotAdmin() throws Exception {

        mockMvc.perform(MockMvcRequestBuilders.post(String.format(SESSION_COURSE_ID_PATH, session.getProgram().getId(),
                        session.getId(), course.getId()))
                .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(
                MockMvcResultMatchers.status().isForbidden()
        );
    }

    @Test
    @WithMockUser(username="test", roles = {Roles.USER, Roles.ADMIN})
    public void SessionCourseAdd_ReturnsNotFound_WhenInvalidProgram() throws Exception {

        mockMvc.perform(MockMvcRequestBuilders.post(String.format(SESSION_COURSE_ID_PATH, session.getProgram().getId() + 1L,
                        session.getId(), course.getId()))
                .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(
                MockMvcResultMatchers.status().isNotFound()
        );
    }

    @Test
    @WithMockUser(username="test", roles = {Roles.USER, Roles.ADMIN})
    public void SessionCourseAdd_ReturnsNotFound_WhenInvalidSession() throws Exception {

        mockMvc.perform(MockMvcRequestBuilders.post(String.format(SESSION_COURSE_ID_PATH, session.getProgram().getId(),
                        session.getId()+ 1L, course.getId()))
                .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(
                MockMvcResultMatchers.status().isNotFound()
        );
    }

    @Test
    @WithMockUser(username="test", roles = {Roles.USER, Roles.ADMIN})
    public void SessionCourseAdd_ReturnsNotFound_WhenInvalidCourse() throws Exception {

        mockMvc.perform(MockMvcRequestBuilders.post(String.format(SESSION_COURSE_ID_PATH, session.getProgram().getId(),
                        session.getId(), course.getId()+1L))
                .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(
                MockMvcResultMatchers.status().isNotFound()
        );
    }

    private CourseResult createAndSaveCourse(){
        CourseCreateCommand courseCreateCommand  = CourseCreateCommand.builder()
                .name("Course1")
                .description("Course1 description")
                .isPublic(true)
                .isOnline(false)
                .build();
        OneOf2<CourseResult, ValidationFailed> result = pipeline.send(courseCreateCommand);
        return result.asOption1();
    }

    private SessionResult createAndSaveProgramSession(){
        ProgramCreateCommand programCreateCmd = Instancio.create(ProgramCreateCommand.class);
        ProgramResult programResult = pipeline.send(programCreateCmd).asOption1();

        SessionCreateCommand sessionCreateCommand = SessionCreateCommand.builder()
                .name("Session1")
                .description("Session1 description")
                .programId(programResult.getId())
                .startDate( LocalDate.now().plusMonths(1))
                .endDate( LocalDate.now().plusMonths(7))
                .practicumHours(10)
                .build();

        return pipeline.send(sessionCreateCommand).asOption1();
    }
}
