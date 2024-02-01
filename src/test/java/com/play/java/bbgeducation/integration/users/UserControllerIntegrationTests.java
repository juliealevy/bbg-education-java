package com.play.java.bbgeducation.integration.users;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.play.java.bbgeducation.api.programs.ProgramRequest;
import com.play.java.bbgeducation.api.users.UserRequest;
import com.play.java.bbgeducation.application.common.exceptions.validation.ValidationFailed;
import com.play.java.bbgeducation.application.common.oneof.OneOf2;
import com.play.java.bbgeducation.application.programs.ProgramResult;
import com.play.java.bbgeducation.application.programs.commands.ProgramCreateCommand;
import com.play.java.bbgeducation.application.users.UserResult;
import com.play.java.bbgeducation.application.users.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.util.Pair;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static com.play.java.bbgeducation.integration.programs.DataUtils.*;
import static com.play.java.bbgeducation.integration.users.DataUtils.buildUserRequest1;
import static com.play.java.bbgeducation.integration.users.DataUtils.buildUserRequest2;

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
    public void UserCreate_Returns201_WhenSuccess() throws Exception {
        UserRequest request = buildUserRequest1();
        String requestJson = objectMapper.writeValueAsString(request);

        underTest.perform(MockMvcRequestBuilders.post(USERS_PATH)
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestJson)
        ).andExpect(
                MockMvcResultMatchers.status().isCreated()
        );
    }

    @Test
    public void UserCreate_ReturnsSavedProgram_WhenSuccess() throws Exception {
        UserRequest request = buildUserRequest1();
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
    public void UserCreate_ReturnsProblemJsonConflict_WhenNameExists() throws Exception {
        UserRequest request = buildUserRequest1();
        userService.createUser(request.getFirstName(), request.getLastName(), request.getEmail(), request.getPassword());

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
    public void UserUpdate_ShouldReturn200_WhenInputValid() throws Exception {
        Pair<UserResult, String> created = createAndSaveUser1();


        UserRequest updatedRequest = UserRequest.builder()
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

    private String getPath(Long Id){
        String path = USERS_PATH;
        if (Id != null){
            path += ("/" + Id);
        }
        return path;
    }

    private Pair<UserResult, String> createAndSaveUser1(){
        UserRequest request = buildUserRequest1();
        OneOf2<UserResult, ValidationFailed> created = userService.createUser(request.getFirstName(),
                request.getLastName(), request.getEmail(), request.getPassword());
        return  Pair.of(created.asOption1(), request.getPassword());
    }

    private Pair<UserResult, String> createAndSaveUser2(){
        UserRequest request = buildUserRequest2();
        OneOf2<UserResult, ValidationFailed> created = userService.createUser(request.getFirstName(),
                request.getLastName(), request.getEmail(), request.getPassword());
        return Pair.of(created.asOption1(), request.getPassword());
    }
}
