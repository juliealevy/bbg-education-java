package com.play.java.bbgeducation.integration.api.root;

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

import static org.hamcrest.Matchers.startsWith;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@AutoConfigureMockMvc
public class ApiRootControllerTests {
    private MockMvc mockMvc;
    private WebApplicationContext webApplicationContext;

    @Autowired
    public ApiRootControllerTests(WebApplicationContext webApplicationContext) {
        this.webApplicationContext = webApplicationContext;
        this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).apply(springSecurity()).build();
    }


    @Test
    public void getLinks_ShouldReturnOk_whenAllValid() throws Exception {

        mockMvc.perform(MockMvcRequestBuilders.get("/api")
                .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(
                MockMvcResultMatchers.status().isOk()
        );
    }

    @Test
    public void getLinks_ShouldReturnHalJson_whenAllValid() throws Exception {

        mockMvc.perform(MockMvcRequestBuilders.get("/api")
                .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(
                MockMvcResultMatchers.content().contentType("application/hal+json")
        );
    }

    @Test
    public void getLinks_ShouldReturnVersion_whenAllValid() throws Exception {

        mockMvc.perform(MockMvcRequestBuilders.get("/api")
                .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.version").exists()
        );
    }

    @Test
    public void getLinks_ShouldReturnLinks_whenAllValid() throws Exception {

        mockMvc.perform(MockMvcRequestBuilders.get("/api")
                .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$._links").exists()
        );
    }

    @Test
    public void getLinks_ShouldReturnSelfWithFullURL() throws Exception {

        mockMvc.perform(MockMvcRequestBuilders.get("/api")
                .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(
                MockMvcResultMatchers.content().contentType("application/hal+json")
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$._links.self.href").isString()
        ).andExpect(
                jsonPath("$._links.self.href",startsWith("http"))
        );

    }
}
