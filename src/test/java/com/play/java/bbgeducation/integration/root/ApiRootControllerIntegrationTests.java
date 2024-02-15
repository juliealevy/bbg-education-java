package com.play.java.bbgeducation.integration.root;

import com.play.java.bbgeducation.api.sessions.SessionRequest;
import com.play.java.bbgeducation.application.programs.result.ProgramResult;
import com.play.java.bbgeducation.infrastructure.auth.Roles;
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

import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@AutoConfigureMockMvc
public class ApiRootControllerIntegrationTests {
    private MockMvc mockMvc;
    private WebApplicationContext webApplicationContext;

    @Autowired
    public ApiRootControllerIntegrationTests(WebApplicationContext webApplicationContext) {
        this.webApplicationContext = webApplicationContext;
        this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).apply(springSecurity()).build();
    }


    @Test
    public void getLinks_ShouldReturn200_whenAllValid() throws Exception {

        mockMvc.perform(MockMvcRequestBuilders.get("/api")
                .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(
                MockMvcResultMatchers.status().isOk()
        ).andExpect(
                MockMvcResultMatchers.content().contentType("application/hal+json")
        );
    }
}
