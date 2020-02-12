package com.stockmanagement.domain.users;

import com.stockmanagement.domain.portfolio.Portfolio;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author frfontoura
 * @version 1.0 19/01/2020
 */
public class UserTest {

    @Test
    @DisplayName("The User should start without portfolio and allow to add a new one")
    void portfolioListTest() {
        final User user = createUser(123);
        assertNotNull(user.getPortfolios());
        assertTrue(user.getPortfolios().isEmpty());

        final Portfolio portfolio = Portfolio.builder()
                .name("Stock portfolio")
                .description("S&P500")
                .build();

        user.addPortfolio(portfolio);

        assertFalse(user.getPortfolios().isEmpty());
        assertEquals(1, user.getPortfolios().size());
        assertEquals(user, portfolio.getUser());
    }

    private User createUser(final Integer id) {
        return User.builder()
                .id(id)
                .name("Tony Stark")
                .username("tonystark")
                .email("ironman@avengers.com")
                .password("friday")
                .build();
    }
}
