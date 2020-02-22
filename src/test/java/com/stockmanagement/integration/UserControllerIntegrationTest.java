package com.stockmanagement.integration;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.test.web.servlet.MvcResult;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @author frfontoura
 * @version 1.0 18/01/2020
 */
public class UserControllerIntegrationTest extends AbstractIntegrationTest {

    @Test
    @DisplayName("should return the logged User")
    void getCurrentUserTest() throws Exception {
        final MvcResult mvcResult = getMockMvc().perform(get("/api/users/me")
                .header("Authorization", token)
                .contentType("application/json"))
                .andExpect(status().isOk())
                .andReturn();

        final String actual = mvcResult.getResponse().getContentAsString();
        assertEquals(getExpectationsAsString("userIntegrationTest_currentUserTest.json"), actual);
    }
}
