package com.play.java.bbgeducation.integration.api.auth;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.play.java.bbgeducation.api.auth.LoginRequest;
import com.play.java.bbgeducation.api.auth.RegisterRequest;
import com.play.java.bbgeducation.application.auth.AuthenticationResult;
import com.play.java.bbgeducation.application.auth.AuthenticationService;
import com.play.java.bbgeducation.application.common.oneof.OneOf2;
import com.play.java.bbgeducation.application.common.validation.ValidationFailed;
import com.play.java.bbgeducation.domain.valueobjects.emailaddress.EmailAddress;
import com.play.java.bbgeducation.domain.valueobjects.firstname.FirstName;
import com.play.java.bbgeducation.domain.valueobjects.lastname.LastName;
import com.play.java.bbgeducation.domain.valueobjects.password.Password;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static com.play.java.bbgeducation.integration.api.auth.DataUtils.buildLoginRequest1;
import static com.play.java.bbgeducation.integration.api.auth.DataUtils.buildRegisterRequest1;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@AutoConfigureMockMvc
public class AuthenticationControllerTests {
    private MockMvc mockMvc;
    private WebApplicationContext webApplicationContext;
    private ObjectMapper objectMapper;
    private AuthenticationService authenticationService;
    private static final String PROBLEM_JSON_TYPE = "application/problem+json";

    private static final String AUTH_PATH = "/api/auth";

    @Autowired
    public AuthenticationControllerTests(WebApplicationContext webApplicationContext, AuthenticationService authenticationService) {
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
    public void Login_ShouldReturnTokens_WhenCredsValid() throws Exception {
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
                MockMvcResultMatchers.jsonPath("$.access_token").isString()
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.refresh_token").isString()
        );

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

    @Test
    public void Refresh_ShouldReturnTokens() throws Exception {
        RegisterRequest regRequest = buildRegisterRequest1();
        registerUser(regRequest);

        OneOf2<AuthenticationResult, ValidationFailed> authenticated = authenticationService.authenticate(
                EmailAddress.from(regRequest.getEmail()), Password.from(regRequest.getPassword()));

        mockMvc.perform(MockMvcRequestBuilders.post(AUTH_PATH + "/refresh")
                .contentType(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + authenticated.asOption1().getAccessToken())
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.access_token").isString()
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.refresh_token").isString()
        );

    }

    private void registerUser(RegisterRequest regRequest){
        authenticationService.register(
                EmailAddress.from(regRequest.getEmail()),
                Password.from(regRequest.getPassword()),
                FirstName.from(regRequest.getFirstName()),
                LastName.from(regRequest.getLastName()),
                regRequest.getIsAdmin());
    }
}
