package com.play.java.bbgeducation.integration.api.users;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.play.java.bbgeducation.api.users.GetUserByEmailRequest;
import com.play.java.bbgeducation.api.users.UpdateUserRequest;
import com.play.java.bbgeducation.domain.users.UserEntity;
import com.play.java.bbgeducation.infrastructure.auth.Roles;
import com.play.java.bbgeducation.infrastructure.repositories.UserRepository;
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

import static com.play.java.bbgeducation.integration.api.users.DataUtils.*;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@AutoConfigureMockMvc
public class UserControllerTests {
    private MockMvc underTest;
    private UserRepository userRepository;
    private ObjectMapper objectMapper;

    private static final String USERS_PATH = "/api/users";
    private static final String USERS_EMAIL_PATH = "/api/users/email";
    private static final String PROBLEM_JSON_TYPE = "application/problem+json";
    private UserEntity createdUser;
    @Autowired
    public UserControllerTests(MockMvc mockMvc, UserRepository userRepository) {
        this.underTest = mockMvc;
        this.userRepository = userRepository;
        this.objectMapper = new ObjectMapper();
    }

    @BeforeEach
    void setUp() {
        createdUser = createAndSaveUser1();
    }

    @Test
    @WithMockUser(username="test", roles = {Roles.ADMIN})
    public void UserUpdate_ShouldReturn200_WhenInputValid() throws Exception {       

        UpdateUserRequest updatedRequest = UpdateUserRequest.builder()
                .firstName(createdUser.getFirstName().toString())
                .lastName(createdUser.getLastName().toString() + " updated")
                .email(createdUser.getUsername())
                .build();


        String requestJson = objectMapper.writeValueAsString(updatedRequest);

        underTest.perform(MockMvcRequestBuilders.put(getPath(createdUser.getId()))
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestJson)
        ).andExpect(
                MockMvcResultMatchers.status().isOk()
        );
    }

    @Test
    @WithMockUser(username="test", roles = {Roles.USER})
    public void UserGetById_Returns200_WhenIdExists() throws Exception {
       
        underTest.perform(MockMvcRequestBuilders.get(getPath(createdUser.getId()))
                .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(
                MockMvcResultMatchers.status().isOk()
        );
    }

    @Test
    @WithMockUser(username="test", roles = {Roles.USER})
    public void UserGetById_ReturnsUser_WhenIdExists() throws Exception {
      
        underTest.perform(MockMvcRequestBuilders.get(getPath(createdUser.getId()))
                .contentType(MediaType.APPLICATION_JSON)
        ).andDo(MockMvcResultHandlers.print()
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.id").value(createdUser.getId())
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.firstName").value(createdUser.getFirstName().toString())
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.lastName").value(createdUser.getLastName().toString())
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.email").value(createdUser.getUsername())
        );
    }

    @Test
    @WithMockUser(username="test", roles = {Roles.USER})
    public void UserGetById_Returns404_WhenIdNotExists() throws Exception {

        underTest.perform(MockMvcRequestBuilders.get(getPath(100L))
                .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(
                MockMvcResultMatchers.status().isNotFound()
        );
    }

    @Test
    @WithMockUser(username="test", roles = {Roles.USER})
    public void UserGetByEmail_Returns200_WhenEmailExists() throws Exception {
       
        GetUserByEmailRequest emailRequest = GetUserByEmailRequest.builder()
                .email(createdUser.getEmail().toString())
                .build();

        String requestJson = objectMapper.writeValueAsString(emailRequest);

        underTest.perform(MockMvcRequestBuilders.get(USERS_EMAIL_PATH)
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestJson)
        ).andExpect(
                MockMvcResultMatchers.status().isOk()
        );
    }

    @Test
    @WithMockUser(username="test", roles = {Roles.USER})
    public void UserGetByEmail_ReturnsUser_WhenEmailExists() throws Exception {
      
        GetUserByEmailRequest emailRequest = GetUserByEmailRequest.builder()
                .email(createdUser.getEmail().toString())
                .build();

        String requestJson = objectMapper.writeValueAsString(emailRequest);
        underTest.perform(MockMvcRequestBuilders.get(USERS_EMAIL_PATH)
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestJson)
        ).andDo(MockMvcResultHandlers.print()
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.id").value(createdUser.getId())
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.firstName").value(createdUser.getFirstName().toString())
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.lastName").value(createdUser.getLastName().toString())
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.email").value(createdUser.getUsername())
        );
    }

    @Test
    @WithMockUser(username="test", roles = {Roles.USER})
    public void UserGetByEmail_Returns404_WhenEmailNotExists() throws Exception {

        GetUserByEmailRequest emailRequest = GetUserByEmailRequest.builder()
                .email("test@blah.com")
                .build();

        String requestJson = objectMapper.writeValueAsString(emailRequest);
        underTest.perform(MockMvcRequestBuilders.get(USERS_EMAIL_PATH)
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestJson)
        ).andExpect(
                MockMvcResultMatchers.status().isNotFound()
        );
    }

    @Test
    @WithMockUser(username="test", roles = {Roles.USER})
    public void ProgramGetAll_Returns200_WhenSuccess() throws Exception {

        underTest.perform(MockMvcRequestBuilders.get(USERS_PATH)
                .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(
                MockMvcResultMatchers.status().isOk()
        );
    }

    @Test
    @WithMockUser(username="test", roles = {Roles.USER})
    public void GetAll_ReturnsList_WhenSuccess() throws Exception {
        UserEntity created2 = createAndSaveUser2();

        underTest.perform(MockMvcRequestBuilders.get(USERS_PATH)
                .contentType(MediaType.APPLICATION_JSON)
        ).andDo(MockMvcResultHandlers.print()
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$._embedded.users.[0].id").value(createdUser.getId())
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$._embedded.users.[0].firstName").value(createdUser.getFirstName().toString())
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$._embedded.users.[0].lastName").value(createdUser.getLastName().toString())
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$._embedded.users.[0].email").value(createdUser.getUsername())
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$._embedded.users.[1].id").value(created2.getId())
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$._embedded.users.[1].firstName").value(created2.getFirstName().toString())
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$._embedded.users.[1].lastName").value(created2.getLastName().toString())
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$._embedded.users.[1].email").value(created2.getUsername())
        );
    }

    @Test
    @WithMockUser(username="test", roles = {Roles.ADMIN})
    public void UserDelete_ShouldReturnOkWithLinks_WhenIdExists() throws Exception {

        underTest.perform(MockMvcRequestBuilders.delete(getPath(createdUser.getId()))
                .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(
                MockMvcResultMatchers.status().isOk()
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$._links").exists()
        );
    }

    @Test
    @WithMockUser(username="test", roles = {Roles.ADMIN})
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
        UserEntity user = buildUserEntity(1);
        return userRepository.save(user);
    }

    private UserEntity createAndSaveUser2(){
        UserEntity user = buildUserEntity(2);
        return userRepository.save(user);
    }

}
