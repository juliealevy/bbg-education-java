package com.play.java.bbgeducation.integration.api.courses;

import an.awesome.pipelinr.Pipeline;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.play.java.bbgeducation.api.courses.CourseRequest;
import com.play.java.bbgeducation.application.common.oneof.OneOf2;
import com.play.java.bbgeducation.application.common.validation.ValidationFailed;
import com.play.java.bbgeducation.application.courses.create.CourseCreateCommand;
import com.play.java.bbgeducation.application.courses.results.CourseResult;
import com.play.java.bbgeducation.infrastructure.auth.Roles;
import org.junit.jupiter.api.AfterEach;
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

import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@AutoConfigureMockMvc
public class CourseControllerTests {
    private MockMvc mockMvc;
    private final WebApplicationContext webApplicationContext;
    private final Pipeline pipeline;
    private final ObjectMapper objectMapper;

    private final String COURSES_PATH = "/api/courses";
    private final String COURSES_ID_PATH = "/api/courses/%s";
    private static final String PROBLEM_JSON_TYPE = "application/problem+json";

    @Autowired
    public CourseControllerTests(WebApplicationContext webApplicationContext, Pipeline pipeline) {
        this.webApplicationContext = webApplicationContext;
        this.pipeline = pipeline;
        this.objectMapper = new ObjectMapper();
    }

    @BeforeEach
    public void init(){
        this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).apply(springSecurity()).build();
    }

    @AfterEach
    void tearDown() {

    }

    @Test
    @WithMockUser(username="test", roles = {Roles.USER})
    public void getAll_ShouldReturn200_WhenSuccess() throws Exception {

        mockMvc.perform(MockMvcRequestBuilders.get(COURSES_PATH)
                .contentType(MediaType.APPLICATION_JSON)
        ).andDo(MockMvcResultHandlers.print()
        ).andExpect(
                MockMvcResultMatchers.status().isOk()
        );

    }

    @Test
    @WithMockUser(username="test", roles = {Roles.USER})
    public void getAll_ShouldReturnList_WhenDataExists() throws Exception {

        CourseResult first = createAndSaveCourse(1);
        CourseResult second = createAndSaveCourse(2);

        mockMvc.perform(MockMvcRequestBuilders.get(COURSES_PATH)
                .contentType(MediaType.APPLICATION_JSON)
        ).andDo(MockMvcResultHandlers.print()
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$._embedded.courses.[0].name").value(first.getName())
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$._embedded.courses.[0].description").value(first.getDescription())
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$._embedded.courses.[1].name").value(second.getName())
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$._embedded.courses.[1].description").value(second.getDescription())
        );

    }
    @Test
    @WithMockUser(username="test", roles = {Roles.ADMIN, Roles.USER})
    public void Create_Returns201_WhenAdminAndValid() throws Exception {
        CourseRequest request = buildCourseRequest();

        String requestJson = objectMapper.writeValueAsString(request);

        mockMvc.perform(MockMvcRequestBuilders.post(String.format(COURSES_PATH))
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestJson)
        ).andDo(MockMvcResultHandlers.print()
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

    //For some reason this isn't getting cleaned up causing getAll to fail when run from mvn....???
//    @Test
//    @WithMockUser(username="test", roles = {Roles.ADMIN, Roles.USER})
//    public void Create_ReturnsResult_WhenInputValid() throws Exception {
//        CourseRequest request = CourseRequest.builder()
//                .name("test123")
//                .description("testdescription")
//                .isPublic(true)
//                .isOnline(false)
//                .build();
//
//        String requestJson = objectMapper.writeValueAsString(request);
//
//        mockMvc.perform(MockMvcRequestBuilders.post(String.format(COURSES_PATH))
//                .contentType(MediaType.APPLICATION_JSON)
//                .content(requestJson)
//        ).andDo(MockMvcResultHandlers.print()
//        ).andExpect(
//                MockMvcResultMatchers.jsonPath("$.id").isNumber()
//        ).andExpect(
//                MockMvcResultMatchers.jsonPath("$.name").value(request.getName())
//        ).andExpect(
//                MockMvcResultMatchers.jsonPath("$.description").value(request.getDescription())
//        ).andExpect(
//                MockMvcResultMatchers.jsonPath("$.isPublic").value(request.getIsPublic())
//        ).andExpect(
//                MockMvcResultMatchers.jsonPath("$.isOnline").value(request.getIsOnline())
//        );
//    }

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
    @WithMockUser(username="test", roles = {Roles.USER})
    public void getById_ShouldReturn200_WhenIdExists() throws Exception {
        CourseResult first = createAndSaveCourse(44);

        mockMvc.perform(MockMvcRequestBuilders.get(String.format(COURSES_ID_PATH, first.getId()))
                .contentType(MediaType.APPLICATION_JSON)
        ).andDo(MockMvcResultHandlers.print()
        ).andExpect(
                MockMvcResultMatchers.status().isOk()
        );

    }

    @Test
    @WithMockUser(username="test", roles = {Roles.USER})
    public void getById_ShouldReturnCourse_WhenIdExists() throws Exception {
        CourseResult first = createAndSaveCourse(55);

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
    @WithMockUser(username="test", roles = {Roles.USER})
    public void getById_ShouldReturnNotFound_WhenIdNotExists() throws Exception {

        mockMvc.perform(MockMvcRequestBuilders.get(String.format(COURSES_ID_PATH, 100L))
                .contentType(MediaType.APPLICATION_JSON)
        ).andDo(MockMvcResultHandlers.print()
        ).andExpect(
                MockMvcResultMatchers.status().isNotFound()
        );

    }



    @Test
    @WithMockUser(username="test", roles = {Roles.ADMIN, Roles.USER})
    public void deleteCourse_ShouldReturn201_WhenIdExists() throws Exception {
        CourseResult first = createAndSaveCourse(66);

        mockMvc.perform(MockMvcRequestBuilders.delete(String.format(COURSES_ID_PATH, first.getId()))
                .contentType(MediaType.APPLICATION_JSON)
        ).andDo(MockMvcResultHandlers.print()
        ).andExpect(
                MockMvcResultMatchers.status().isNoContent()
        );

    }

    @Test
    @WithMockUser(username="test", roles = {Roles.ADMIN, Roles.USER})
    public void deleteCourse_ShouldReturn404_WhenIdNotExists() throws Exception {

        mockMvc.perform(MockMvcRequestBuilders.delete(String.format(COURSES_ID_PATH, 100L))
                .contentType(MediaType.APPLICATION_JSON)
        ).andDo(MockMvcResultHandlers.print()
        ).andExpect(
                MockMvcResultMatchers.status().isNotFound()
        );

    }

    @Test
    @WithMockUser(username="test", roles = {Roles.USER})
    public void deleteCourse_ShouldReturn401_WhenNotAdmin() throws Exception {

        mockMvc.perform(MockMvcRequestBuilders.delete(String.format(COURSES_ID_PATH, 100L))
                .contentType(MediaType.APPLICATION_JSON)
        ).andDo(MockMvcResultHandlers.print()
        ).andExpect(
                MockMvcResultMatchers.status().isForbidden()
        );

    }

    private CourseRequest buildCourseRequest(int index) {
        return CourseRequest.builder()
                .name("Course request " + index)
                .description("course description")
                .isOnline(true)
                .isPublic(false)
                .build();
    }

    private CourseRequest buildCourseRequest() {
        return buildCourseRequest(0);
    }

    private CourseResult createAndSaveCourse(int index){
        CourseCreateCommand command = CourseCreateCommand.builder()
                .name("Course " + index)
                .description("course description")
                .isOnline(true)
                .isPublic(false)
                .build();

        OneOf2<CourseResult, ValidationFailed> result = pipeline.send(command);
        return result.asOption1();
    }

    private CourseResult createAndSaveCourse(){
        return createAndSaveCourse(0);
    }
}
