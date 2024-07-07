package com.solvia.ecommerce;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class BasicApiTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void shouldLoadAPIRoot() throws Exception {
        this.mockMvc.perform(get("/")).andExpect(status().isOk())
                .andExpect(content().string(containsString("href")));
    }

    @Test
    void shouldReturnForbiddenError() throws Exception {
        this.mockMvc.perform(post("/products")).andExpect(status().isForbidden());
    }

}