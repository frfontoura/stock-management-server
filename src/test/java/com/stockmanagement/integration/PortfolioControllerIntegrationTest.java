package com.stockmanagement.integration;

import com.stockmanagement.domain.portfolio.Portfolio;
import org.junit.jupiter.api.*;
import org.springframework.test.web.servlet.MvcResult;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @author frfontoura
 * @version 1.0 04/02/2020
 */
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class PortfolioControllerIntegrationTest extends AbstractIntegrationTest {

    private final static String BASE_URL = "/api/users/portfolios";

    @Order(1)
    @Test
    @DisplayName("should create a new portfolio for the logged in user")
    public void createPortfolioTest() throws Exception {
        final Portfolio portfolio = createPortfolio();

        final MvcResult mvcResult = getMockMvc().perform(post(BASE_URL)
                .header("Authorization", token)
                .contentType("application/json")
                .content(getObjectMapper().writeValueAsString(portfolio)))
                .andExpect(status().isCreated())
                .andReturn();

        final String actual = mvcResult.getResponse().getContentAsString();
        assertEquals(getExpectationsAsString("portfolioIntegrationTest_createPortfolioTest.json"), actual);
    }

    @Order(2)
    @Test
    @DisplayName("should find all Portfolio from the User")
    public void findByUserTest() throws Exception {
        final MvcResult mvcResult = getMockMvc().perform(get(BASE_URL)
                .header("Authorization", token)
                .contentType("application/json"))
                .andExpect(status().isOk())
                .andReturn();

        final String actual = mvcResult.getResponse().getContentAsString();
        assertEquals(getExpectationsAsString("portfolioIntegrationTest_findByUserTest.json"), actual);
    }

    @Order(3)
    @Test
    @DisplayName("should update a portfolio")
    public void updateTest() throws Exception {
        final Integer portfolioId = 1;
        final Portfolio portfolio = Portfolio.builder()
                .id(portfolioId)
                .name("Stock Options")
                .description("Stock Options")
                .build();

        final MvcResult mvcResult = getMockMvc().perform(put(BASE_URL + "/" + portfolioId)
                .header("Authorization", token)
                .contentType("application/json")
                .content(getObjectMapper().writeValueAsString(portfolio)))
                .andExpect(status().isOk())
                .andReturn();

        final String actual = mvcResult.getResponse().getContentAsString();
        assertEquals(getExpectationsAsString("portfolioIntegrationTest_updateTest.json"), actual);
    }

    @Order(4)
    @Test
    @DisplayName("should find a portfolio by id")
    public void findByIdTest() throws Exception {
        final Integer portfolioId = 1;

        final MvcResult mvcResult = getMockMvc().perform(get(BASE_URL + "/" + portfolioId)
                .header("Authorization", token)
                .contentType("application/json"))
                .andExpect(status().isOk())
                .andReturn();

        final String actual = mvcResult.getResponse().getContentAsString();
        assertEquals(getExpectationsAsString("portfolioIntegrationTest_findByIdTest.json"), actual);
    }

    @Order(5)
    @Test
    @DisplayName("should remove a Portfolio from the User")
    public void deleteTest() throws Exception {
        final Integer portfolioId = 1;

        getMockMvc().perform(delete(BASE_URL + "/" + portfolioId)
            .header("Authorization", token)
            .contentType("application/json"))
            .andExpect(status().isOk())
            .andReturn();

        final MvcResult mvcResult = getMockMvc().perform(get(BASE_URL)
                .header("Authorization", token)
                .contentType("application/json"))
                .andExpect(status().isOk())
                .andReturn();

        final String actual = mvcResult.getResponse().getContentAsString();
        assertEquals("[]", actual);
    }

    private Portfolio createPortfolio() {
        return Portfolio.builder()
                .name("Real estate fund")
                .description("Brazilian FII")
                .build();
    }

}
