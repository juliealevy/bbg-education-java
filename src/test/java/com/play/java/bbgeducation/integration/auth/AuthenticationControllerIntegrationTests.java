package com.play.java.bbgeducation.integration.auth;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.play.java.bbgeducation.api.auth.LoginRequest;
import com.play.java.bbgeducation.api.auth.RegisterRequest;
import com.play.java.bbgeducation.application.auth.AuthenticationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static com.play.java.bbgeducation.integration.auth.DataUtils.buildLoginRequest1;
import static com.play.java.bbgeducation.integration.auth.DataUtils.buildRegisterRequest1;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@AutoConfigureMockMvc
public class AuthenticationControllerIntegrationTests {
    private MockMvc mockMvc;
    private WebApplicationContext webApplicationContext;
    private ObjectMapper objectMapper;
    private AuthenticationService authenticationService;
    private static final String PROBLEM_JSON_TYPE = "application/problem+json";

    private static final String AUTH_PATH = "/api/auth";

    @Autowired
    public AuthenticationControllerIntegrationTests(WebApplicationContext webApplicationContext, AuthenticationService authenticationService) {
        this.webApplicationContext = webApplicationContext;
        this.authenticationService = authenticationService;
        this.objectMapper = new ObjectMapper();
    }

    @BeforeEach
    public void init(){
        this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).apply(springSecurity()).build();
    }

    @Test
    public void Register_ShouldReturn201_WhenInputValid() throws Exception {
        //not sure why i have to use different data for this test.  If i try and
        //use the datautil method to build the request, i get an error:  email already exists...
        //something must be off with the set up of these tests.   each one should start fresh
        RegisterRequest request = RegisterRequest.builder()
                .email("testcreate@test.com")
                .firstName("test")
                .lastName("test")
                .password("123456")
                .isAdmin(false)
                .build();
        String requestJson = objectMapper.writeValueAsString(request);

        mockMvc.perform(MockMvcRequestBuilders.post(AUTH_PATH + "/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestJson)
        ).andExpect(
                MockMvcResultMatchers.status().isCreated()
        );
    }

    @Test
    public void Register_ShouldReturnFail_WhenEmailExists() throws Exception {
        RegisterRequest request = buildRegisterRequest1();
        registerUser(request);

        RegisterRequest request2 = buildRegisterRequest1();
        String requestJson = objectMapper.writeValueAsString(request2);

        mockMvc.perform(MockMvcRequestBuilders.post(AUTH_PATH + "/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestJson)
        ).andExpect(
                MockMvcResultMatchers.status().isConflict()
        ).andExpect(
                MockMvcResultMatchers.content().contentType(PROBLEM_JSON_TYPE)
        );

    }
    @Test
    public void Login_ShouldReturn200_WhenCredsValid() throws Exception {
        RegisterRequest regRequest = buildRegisterRequest1();
        registerUser(regRequest);

        LoginRequest request = LoginRequest.builder()
                .email(regRequest.getEmail())
                .password(regRequest.getPassword())
                .build();

        String requestJson = objectMapper.writeValueAsString(request);

        mockMvc.perform(MockMvcRequestBuilders.post(AUTH_PATH + "/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestJson)
        ).andExpect(
                MockMvcResultMatchers.status().isOk()
        );
    }

    @Test
    public void Login_ShouldReturnToken_WhenCredsValid() throws Exception {
        RegisterRequest regRequest = buildRegisterRequest1();
        registerUser(regRequest);

        LoginRequest request = LoginRequest.builder()
                .email(regRequest.getEmail())
                .password(regRequest.getPassword())
                .build();

        String requestJson = objectMapper.writeValueAsString(request);

        mockMvc.perform(MockMvcRequestBuilders.post(AUTH_PATH + "/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestJson)
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.accessToken").isString());

    }



    @Test
    public void Login_ShouldFail_WhenEmailNotExists() throws Exception {
        LoginRequest request = buildLoginRequest1();

        String requestJson = objectMapper.writeValueAsString(request);

        mockMvc.perform(MockMvcRequestBuilders.post(AUTH_PATH + "/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestJson)
        ).andExpect(
                MockMvcResultMatchers.status().isUnauthorized()  //should catch this for a clearer error??
        );
    }

    @Test
    public void Login_ShouldFail_WhenPasswordInvalid() throws Exception {
        RegisterRequest regRequest = buildRegisterRequest1();
        registerUser(regRequest);

        LoginRequest request = LoginRequest.builder()
                .email(regRequest.getEmail())
                .password(regRequest.getPassword() + " oops")
                .build();

        String requestJson = objectMapper.writeValueAsString(request);

        mockMvc.perform(MockMvcRequestBuilders.post(AUTH_PATH + "/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestJson)
        ).andExpect(
                MockMvcResultMatchers.status().isUnauthorized()
        );
    }

    private void registerUser(RegisterRequest regRequest){
        authenticationService.register(regRequest.getEmail(), regRequest.getPassword(), regRequest.getFirstName(),
                regRequest.getLastName(),regRequest.getIsAdmin());
    }
}
