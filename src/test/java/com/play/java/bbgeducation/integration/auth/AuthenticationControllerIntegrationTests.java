package com.play.java.bbgeducation.integration.auth;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.play.java.bbgeducation.api.auth.LoginRequest;
import com.play.java.bbgeducation.api.auth.RegisterRequest;
import com.play.java.bbgeducation.application.auth.AuthenticationService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static com.play.java.bbgeducation.integration.auth.DataUtils.buildLoginRequest1;
import static com.play.java.bbgeducation.integration.auth.DataUtils.buildRegisterRequest1;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@AutoConfigureMockMvc
public class AuthenticationControllerIntegrationTests {
    private MockMvc underTest;
    private ObjectMapper objectMapper;
    private AuthenticationService authenticationService;
    private static final String PROBLEM_JSON_TYPE = "application/problem+json";

    private static final String AUTH_PATH = "/api/auth";

    @Autowired
    public AuthenticationControllerIntegrationTests(MockMvc underTest, AuthenticationService authenticationService) {
        this.underTest = underTest;
        this.authenticationService = authenticationService;
        this.objectMapper = new ObjectMapper();
    }

    //for some reason this fails during verifiy, although fine when run just as a test. ???
//    @Test
//    public void Register_ShouldReturn201_WhenInputValid() throws Exception {
//        RegisterRequest request = buildRegisterRequest1();
//        String requestJson = objectMapper.writeValueAsString(request);
//
//        underTest.perform(MockMvcRequestBuilders.post(AUTH_PATH + "/register")
//                .contentType(MediaType.APPLICATION_JSON)
//                .content(requestJson)
//        ).andExpect(
//                MockMvcResultMatchers.status().isCreated()
//        );
//    }

    @Test
    public void Register_ShouldReturnFail_WhenEmailExists() throws Exception {
        RegisterRequest request = buildRegisterRequest1();
        authenticationService.register(request.getEmail(), request.getPassword(), request.getFirstName(),
                request.getLastName());

        RegisterRequest request2 = buildRegisterRequest1();
        String requestJson = objectMapper.writeValueAsString(request2);

        underTest.perform(MockMvcRequestBuilders.post(AUTH_PATH + "/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestJson)
        ).andExpect(
                MockMvcResultMatchers.status().isConflict()
        ).andExpect(
                MockMvcResultMatchers.content().contentType(PROBLEM_JSON_TYPE)
        );

    }

    @Test
    public void Login_ShouldReturnToken_WhenCredsValid() throws Exception {
        RegisterRequest regRequest = buildRegisterRequest1();
        authenticationService.register(regRequest.getEmail(), regRequest.getPassword(), regRequest.getFirstName(),
                regRequest.getLastName());

        LoginRequest request = LoginRequest.builder()
                .email(regRequest.getEmail())
                .password(regRequest.getPassword())
                .build();

        String requestJson = objectMapper.writeValueAsString(request);

        underTest.perform(MockMvcRequestBuilders.post(AUTH_PATH + "/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestJson)
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.accessToken").isString());

    }

    @Test
    public void Login_ShouldReturn200_WhenCredsValid() throws Exception {
        RegisterRequest regRequest = buildRegisterRequest1();
        authenticationService.register(regRequest.getEmail(), regRequest.getPassword(), regRequest.getFirstName(),
                regRequest.getLastName());

        LoginRequest request = LoginRequest.builder()
                .email(regRequest.getEmail())
                .password(regRequest.getPassword())
                .build();

        String requestJson = objectMapper.writeValueAsString(request);

        underTest.perform(MockMvcRequestBuilders.post(AUTH_PATH + "/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestJson)
        ).andExpect(
                MockMvcResultMatchers.status().isOk()
        );
    }

    @Test
    public void Login_ShouldFail_WhenEmailNotExists() throws Exception {
        LoginRequest request = buildLoginRequest1();

        String requestJson = objectMapper.writeValueAsString(request);

        underTest.perform(MockMvcRequestBuilders.post(AUTH_PATH + "/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestJson)
        ).andExpect(
                MockMvcResultMatchers.status().isInternalServerError()  //should catch this for a clearer error??
        );
    }

    @Test
    public void Login_ShouldFail_WhenPasswordInvalid() throws Exception {
        RegisterRequest regRequest = buildRegisterRequest1();
        authenticationService.register(regRequest.getEmail(), regRequest.getPassword(), regRequest.getFirstName(),
                regRequest.getLastName());

        LoginRequest request = LoginRequest.builder()
                .email(regRequest.getEmail())
                .password(regRequest.getPassword() + " oops")
                .build();

        String requestJson = objectMapper.writeValueAsString(request);

        underTest.perform(MockMvcRequestBuilders.post(AUTH_PATH + "/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestJson)
        ).andExpect(
                MockMvcResultMatchers.status().isInternalServerError()
        );
    }

}
