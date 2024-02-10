package com.play.java.bbgeducation.integration.users;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.play.java.bbgeducation.api.users.UpdateUserRequest;
import com.play.java.bbgeducation.domain.users.UserEntity;
import com.play.java.bbgeducation.infrastructure.repositories.UserRepository;
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

import static com.play.java.bbgeducation.integration.users.DataUtils.buildUserEntity1;
import static com.play.java.bbgeducation.integration.users.DataUtils.buildUserEntity2;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@AutoConfigureMockMvc
public class UserControllerIntegrationTests {
    private MockMvc underTest;
    private UserRepository userRepository;
    private ObjectMapper objectMapper;

    private static final String USERS_PATH = "/api/users";
    private static final String PROBLEM_JSON_TYPE = "application/problem+json";
    @Autowired
    public UserControllerIntegrationTests(MockMvc mockMvc, UserRepository userRepository) {
        this.underTest = mockMvc;
        this.userRepository = userRepository;
        this.objectMapper = new ObjectMapper();
    }

    @Test
    @WithMockUser("test")
    public void UserUpdate_ShouldReturn200_WhenInputValid() throws Exception {
        UserEntity created = createAndSaveUser1();

        UpdateUserRequest updatedRequest = UpdateUserRequest.builder()
                .firstName(created.getFirstName())
                .lastName(created.getLastName() + " updated")
                .email(created.getEmail())
                .password(created.getPassword())
                .build();


        String requestJson = objectMapper.writeValueAsString(updatedRequest);

        underTest.perform(MockMvcRequestBuilders.put(getPath(created.getId()))
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestJson)
        ).andExpect(
                MockMvcResultMatchers.status().isOk()
        );
    }

    @Test
    @WithMockUser("test")
    public void UserGetById_Returns200_WhenIdExists() throws Exception {
        UserEntity created = createAndSaveUser1();

        underTest.perform(MockMvcRequestBuilders.get(getPath(created.getId()))
                .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(
                MockMvcResultMatchers.status().isOk()
        );
    }

    @Test
    @WithMockUser("test")
    public void UserGetById_ReturnsUser_WhenIdExists() throws Exception {
        UserEntity created = createAndSaveUser1();

        underTest.perform(MockMvcRequestBuilders.get(getPath(created.getId()))
                .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.id").value(created.getId())
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.firstName").value(created.getFirstName())
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.lastName").value(created.getLastName())
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.email").value(created.getEmail())
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
        UserEntity created = createAndSaveUser1();
        UserEntity created2 = createAndSaveUser2();

        underTest.perform(MockMvcRequestBuilders.get(USERS_PATH)
                .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$[0].id").value(created.getId())
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$[0].firstName").value(created.getFirstName())
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$[0].lastName").value(created.getLastName())
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$[0].email").value(created.getEmail())
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$[1].id").value(created2.getId())
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$[1].firstName").value(created2.getFirstName())
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$[1].lastName").value(created2.getLastName())
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$[1].email").value(created2.getEmail())
        );
    }

    @Test
    @WithMockUser("test")
    public void UserDelete_ShouldReturnNoContent_WhenIdExists() throws Exception {
        UserEntity created = createAndSaveUser1();

        underTest.perform(MockMvcRequestBuilders.delete(getPath(created.getId()))
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

    private UserEntity createAndSaveUser1(){
        UserEntity user = buildUserEntity1();
        return userRepository.save(user);
    }

    private UserEntity createAndSaveUser2(){
        UserEntity user = buildUserEntity2();
        return userRepository.save(user);
    }

}
