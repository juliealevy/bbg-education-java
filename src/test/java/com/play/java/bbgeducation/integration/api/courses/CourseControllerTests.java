package com.play.java.bbgeducation.integration.api.courses;

import an.awesome.pipelinr.Pipeline;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.play.java.bbgeducation.api.courses.CourseRequest;
import com.play.java.bbgeducation.application.common.oneof.OneOf2;
import com.play.java.bbgeducation.application.common.validation.ValidationFailed;
import com.play.java.bbgeducation.application.courses.create.CourseCreateCommand;
import com.play.java.bbgeducation.application.courses.results.CourseResult;
import com.play.java.bbgeducation.infrastructure.auth.Roles;
import org.instancio.Instancio;
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

import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@AutoConfigureMockMvc
public class CourseControllerTests {
    private MockMvc mockMvc;
    private Pipeline pipeline;
    private final ObjectMapper objectMapper;

    private final String COURSES_PATH = "/api/courses";
    private final String COURSES_ID_PATH = "/api/courses/%s";
    private static final String PROBLEM_JSON_TYPE = "application/problem+json";

    @Autowired
    public CourseControllerTests(WebApplicationContext webApplicationContext, Pipeline pipeline) {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).apply(springSecurity()).build();
        this.pipeline = pipeline;
        this.objectMapper = JsonMapper.builder()
                .addModule(new JavaTimeModule())
                .build();
    }

    @Test
    @WithMockUser(username="test", roles = {Roles.ADMIN, Roles.USER})
    public void Create_Returns201_WhenAdminAndValid() throws Exception {
        CourseRequest request = buildCourseRequest();

        String requestJson = objectMapper.writeValueAsString(request);

        mockMvc.perform(MockMvcRequestBuilders.post(String.format(COURSES_PATH))
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestJson)
        ).andExpect(
                MockMvcResultMatchers.status().isCreated()
        );
    }

    @Test
    @WithMockUser(username="test", roles = {Roles.USER})
    public void Create_ReturnsForbidden_WhenNotAdmin() throws Exception {
        CourseRequest request = buildCourseRequest();

        String requestJson = objectMapper.writeValueAsString(request);

        mockMvc.perform(MockMvcRequestBuilders.post(String.format(COURSES_PATH))
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestJson)
        ).andExpect(
                MockMvcResultMatchers.status().isForbidden()
        );
    }

    @Test
    @WithMockUser(username="test", roles = {Roles.ADMIN, Roles.USER})
    public void Create_ReturnsResult_WhenInputValid() throws Exception {
        CourseRequest request = buildCourseRequest();

        String requestJson = objectMapper.writeValueAsString(request);

        mockMvc.perform(MockMvcRequestBuilders.post(String.format(COURSES_PATH))
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestJson)
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.id").isNumber()
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.name").value(request.getName())
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.description").value(request.getDescription())
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.isPublic").value(request.getIsPublic())
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.isOnline").value(request.getIsOnline())
        );
    }

    @Test
    @WithMockUser(username="test", roles = {Roles.ADMIN, Roles.USER})
    public void Create_ReturnsProblemJsonConflict_WhenNameExists() throws Exception {
        CourseResult first = createAndSaveCourse();
        CourseRequest second = buildCourseRequest();
        second.setName(first.getName());

        String requestJson = objectMapper.writeValueAsString(second);
        mockMvc.perform(MockMvcRequestBuilders.post(String.format(COURSES_PATH))
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestJson)
        ).andExpect(
                MockMvcResultMatchers.status().isConflict()
        ).andExpect(
                MockMvcResultMatchers.content().contentType(PROBLEM_JSON_TYPE)
        );

    }

    @Test
    @WithMockUser(username="test", roles = {Roles.ADMIN, Roles.USER})
    public void getById_ShouldReturn200_WhenIdExists() throws Exception {
        CourseResult first = createAndSaveCourse();

        mockMvc.perform(MockMvcRequestBuilders.get(String.format(COURSES_ID_PATH, first.getId()))
                .contentType(MediaType.APPLICATION_JSON)
        ).andDo(MockMvcResultHandlers.print()
        ).andExpect(
                MockMvcResultMatchers.status().isOk()
        );

    }

    @Test
    @WithMockUser(username="test", roles = {Roles.ADMIN, Roles.USER})
    public void getById_ShouldReturnCourse_WhenIdExists() throws Exception {
        CourseResult first = createAndSaveCourse();

        mockMvc.perform(MockMvcRequestBuilders.get(String.format(COURSES_ID_PATH, first.getId()))
                .contentType(MediaType.APPLICATION_JSON)
        ).andDo(MockMvcResultHandlers.print()
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.id").value(first.getId())
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.name").value(first.getName())
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.description").value(first.getDescription())
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.isPublic").value(first.getIsPublic())
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.isOnline").value(first.getIsOnline())
        );

    }

    @Test
    @WithMockUser(username="test", roles = {Roles.ADMIN, Roles.USER})
    public void getById_ShouldReturnNotFound_WhenIdNotExists() throws Exception {

        mockMvc.perform(MockMvcRequestBuilders.get(String.format(COURSES_ID_PATH, 100L))
                .contentType(MediaType.APPLICATION_JSON)
        ).andDo(MockMvcResultHandlers.print()
        ).andExpect(
                MockMvcResultMatchers.status().isNotFound()
        );

    }

    private CourseRequest buildCourseRequest() {
        return CourseRequest.builder()
                .name(Instancio.create(String.class))
                .description(Instancio.create(String.class))
                .isOnline(true)
                .isPublic(false)
                .build();
    }

    private CourseResult createAndSaveCourse(){
        CourseCreateCommand command = CourseCreateCommand.builder()
                .name(Instancio.create(String.class))
                .description(Instancio.create(String.class))
                .isOnline(true)
                .isPublic(false)
                .build();

        OneOf2<CourseResult, ValidationFailed> result = pipeline.send(command);
        return result.asOption1();
    }
}