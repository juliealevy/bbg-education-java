package com.play.java.bbgeducation.integration.users;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.play.java.bbgeducation.api.users.CreateUserRequest;
import com.play.java.bbgeducation.api.users.UpdateUserRequest;
import com.play.java.bbgeducation.application.common.validation.ValidationFailed;
import com.play.java.bbgeducation.application.common.oneof.OneOf2;
import com.play.java.bbgeducation.application.users.UserResult;
import com.play.java.bbgeducation.application.users.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.util.Pair;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static com.play.java.bbgeducation.integration.users.DataUtils.*;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@AutoConfigureMockMvc
public class UserControllerIntegrationTests {
    private MockMvc underTest;
    private UserService userService;
    private ObjectMapper objectMapper;

    private static final String USERS_PATH = "/api/users";
    private static final String PROBLEM_JSON_TYPE = "application/problem+json";
    @Autowired
    public UserControllerIntegrationTests(MockMvc mockMvc, UserService userService) {
        this.underTest = mockMvc;
        this.userService = userService;
        this.objectMapper = new ObjectMapper();
    }

    @Test
    @WithMockUser("test")
    public void UserCreate_Returns201_WhenSuccess() throws Exception {
        CreateUserRequest request = buildUserRequest1();
        String requestJson = objectMapper.writeValueAsString(request);

        underTest.perform(MockMvcRequestBuilders.post(USERS_PATH)
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestJson)
        ).andExpect(
                MockMvcResultMatchers.status().isCreated()
        );
    }

    @Test
    @WithMockUser("test")
    public void UserCreate_ReturnsSavedUser_WhenSuccess() throws Exception {
        CreateUserRequest request = buildUserRequest1();
        String requestJson = objectMapper.writeValueAsString(request);

        underTest.perform(MockMvcRequestBuilders.post(USERS_PATH)
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestJson)
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.id").isNumber()
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.firstName").value(request.getFirstName())
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.lastName").value(request.getLastName())
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.email").value(request.getEmail())
        );
    }

    @Test
    @WithMockUser("test")
    public void UserCreate_ReturnsAdminUser_WhenAdminTrue() throws Exception {
        CreateUserRequest request = buildAdminRequest();
        String requestJson = objectMapper.writeValueAsString(request);

        underTest.perform(MockMvcRequestBuilders.post(USERS_PATH)
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestJson)
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.admin").value(true)
        );
    }

    @Test
    @WithMockUser("test")
    public void UserCreate_ReturnsProblemJsonConflict_WhenNameExists() throws Exception {
        CreateUserRequest request = buildUserRequest1();
        userService.createUser(request.getFirstName(), request.getLastName(), request.getEmail(), request.getPassword(),
                false);

        String requestJson = objectMapper.writeValueAsString(request);

        underTest.perform(MockMvcRequestBuilders.post(USERS_PATH)
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestJson)
        ).andExpect(
                MockMvcResultMatchers.status().isConflict()
        ).andExpect(
                MockMvcResultMatchers.content().contentType(PROBLEM_JSON_TYPE)
        );
    }

    @Test
    @WithMockUser("test")
    public void UserUpdate_ShouldReturn200_WhenInputValid() throws Exception {
        Pair<UserResult, String> created = createAndSaveUser1();


        UpdateUserRequest updatedRequest = UpdateUserRequest.builder()
                .firstName(created.getFirst().getFirstName())
                .lastName(created.getFirst().getLastName() + " updated")
                .email(created.getFirst().getEmail())
                .password(created.getSecond())
                .build();


        String requestJson = objectMapper.writeValueAsString(updatedRequest);

        underTest.perform(MockMvcRequestBuilders.put(getPath(created.getFirst().getId()))
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestJson)
        ).andExpect(
                MockMvcResultMatchers.status().isOk()
        );
    }

    @Test
    @WithMockUser("test")
    public void UserGetById_Returns200_WhenIdExists() throws Exception {
        Pair<UserResult, String> created = createAndSaveUser1();

        underTest.perform(MockMvcRequestBuilders.get(getPath(created.getFirst().getId()))
                .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(
                MockMvcResultMatchers.status().isOk()
        );
    }

    @Test
    @WithMockUser("test")
    public void UserGetById_ReturnsUser_WhenIdExists() throws Exception {
        Pair<UserResult, String> created = createAndSaveUser1();

        underTest.perform(MockMvcRequestBuilders.get(getPath(created.getFirst().getId()))
                .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.id").value(created.getFirst().getId())
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.firstName").value(created.getFirst().getFirstName())
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.lastName").value(created.getFirst().getLastName())
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.email").value(created.getFirst().getEmail())
        );
    }

    @Test
    @WithMockUser("test")
    public void UserGetById_Returns404_WhenIdNotExists() throws Exception {

        underTest.perform(MockMvcRequestBuilders.get(getPath(100L))
                .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(
                MockMvcResultMatchers.status().isNotFound()
        );
    }

    @Test
    @WithMockUser("test")
    public void ProgramGetAll_Returns200_WhenSuccess() throws Exception {

        underTest.perform(MockMvcRequestBuilders.get(USERS_PATH)
                .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(
                MockMvcResultMatchers.status().isOk()
        );
    }

    @Test
    @WithMockUser("test")
    public void GetAll_ReturnsList_WhenSuccess() throws Exception {
        Pair<UserResult, String> created = createAndSaveUser1();
        Pair<UserResult, String> created2 = createAndSaveUser2();

        underTest.perform(MockMvcRequestBuilders.get(USERS_PATH)
                .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$[0].id").value(created.getFirst().getId())
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$[0].firstName").value(created.getFirst().getFirstName())
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$[0].lastName").value(created.getFirst().getLastName())
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$[0].email").value(created.getFirst().getEmail())
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$[1].id").value(created2.getFirst().getId())
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$[1].firstName").value(created2.getFirst().getFirstName())
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$[1].lastName").value(created2.getFirst().getLastName())
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$[1].email").value(created2.getFirst().getEmail())
        );
    }

    @Test
    @WithMockUser("test")
    public void UserDelete_ShouldReturnNoContent_WhenIdExists() throws Exception {
        Pair<UserResult, String> created = createAndSaveUser1();

        underTest.perform(MockMvcRequestBuilders.delete(getPath(created.getFirst().getId()))
                .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(
                MockMvcResultMatchers.status().isNoContent()
        );
    }

    @Test
    @WithMockUser("test")
    public void UserDelete_ShouldReturnNotFound_WhenIdNotExists() throws Exception {

        underTest.perform(MockMvcRequestBuilders.delete(getPath(100L))
                .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(
                MockMvcResultMatchers.status().isNotFound()
        );
    }

    private String getPath(Long Id){
        String path = USERS_PATH;
        if (Id != null){
            path += ("/" + Id);
        }
        return path;
    }

    private Pair<UserResult, String> createAndSaveUser1(){
        CreateUserRequest request = buildUserRequest1();
        OneOf2<UserResult, ValidationFailed> created = userService.createUser(request.getFirstName(),
                request.getLastName(), request.getEmail(), request.getPassword(),request.getIsAdmin());
        return  Pair.of(created.asOption1(), request.getPassword());
    }

    private Pair<UserResult, String> createAndSaveUser2(){
        CreateUserRequest request = buildUserRequest2();
        OneOf2<UserResult, ValidationFailed> created = userService.createUser(request.getFirstName(),
                request.getLastName(), request.getEmail(), request.getPassword(),request.getIsAdmin());
        return Pair.of(created.asOption1(), request.getPassword());
    }
}
