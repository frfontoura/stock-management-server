package com.stockmanagement.integration;

import com.stockmanagement.domain.users.User;
import org.junit.jupiter.api.*;
import org.springframework.test.web.servlet.MvcResult;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @author frfontoura
 * @version 1.0 14/01/2020
 */
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class SignUpControllerIntegrationTest extends AbstractIntegrationTest {

    @Test
    @Order(1)
    @DisplayName("Should create a new user")
    void signUpTest() throws Exception {
        final User user = createUser();

        final MvcResult mvcResult = getMockMvc().perform(post("/signup")
                .contentType("application/json")
                .content(getObjectMapper().writeValueAsString(user)))
                .andExpect(status().isOk())
                .andReturn();

        final String actual = mvcResult.getResponse().getContentAsString();
        assertEquals(getExpectationsAsString("signUpIntegrationTest_createNewUserTest.json"), actual);
    }

    @Test
    @Order(2)
    @DisplayName("Should fail when trying to create an existing user")
    void signUpWithAnExistingUserTest() throws Exception {
        final User user = createUser();

        getMockMvc().perform(post("/signup")
                .contentType("application/json")
                .content(getObjectMapper().writeValueAsString(user)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @Order(3)
    @DisplayName("Should fail when trying to create an user with all attributes empty")
    void signUpWithBlankUserTest() throws Exception {
        final User user = User.builder().build();

        getMockMvc().perform(post("/signup")
                .contentType("application/json")
                .content(getObjectMapper().writeValueAsString(user)))
                .andExpect(status().isBadRequest());
    }

    private User createUser() {
        return User.builder()
                .name("Tony Stark")
                .username("ironman")
                .email("ironman@avengers.com")
                .password("friday")
                .build();
    }
}
