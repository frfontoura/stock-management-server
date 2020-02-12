package com.stockmanagement.domain.portfolio;

import com.stockmanagement.domain.users.User;
import com.stockmanagement.domain.users.UserService;
import com.stockmanagement.infra.exception.StockManagementException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * @author frfontoura
 * @version 1.0 19/01/2020
 */
@ExtendWith(MockitoExtension.class)
public class PortfolioServiceTest {

    @Mock
    private UserService userService;

    @InjectMocks
    private PortfolioService service;

    @Test
    @DisplayName("should create a new portfolio for the logged in user")
    public void createPortfolioTest() {
        final Integer userId = 123;
        final User user = User.builder().id(userId).build();
        when(userService.findById(userId)).thenReturn(user);

        final Portfolio portfolio = createPortfolio(null);
        service.createPortfolio(userId, portfolio);

        assertEquals(user, portfolio.getUser());
        assertEquals(1, user.getPortfolios().size());
        verify(userService, times(1)).findById(userId);
    }

    @Test
    @DisplayName("should fail when trying to create a portfolio for a non-existent user")
    public void createPortfolioFailTest() {
        final Integer userId = 1000;
        when(userService.findById(userId)).thenThrow(new StockManagementException(""));

        final Portfolio portfolio = createPortfolio(null);

        assertThrows(StockManagementException.class, () -> {
            service.createPortfolio(userId, portfolio);
        });

        verify(userService, times(1)).findById(userId);
    }

    @Test
    @DisplayName("should list all portfolio from user")
    public void findByUserTest() {
        final Integer userId = 123;
        final User user = User.builder().id(userId).build();
        final Portfolio portfolio = createPortfolio(null);
        user.addPortfolio(portfolio);
        when(userService.findById(userId)).thenReturn(user);

        final List<Portfolio> actual = service.findByUser(userId);

        assertNotNull(actual);
        assertEquals(1, actual.size());
        verify(userService, times(1)).findById(userId);
    }

    @Test
    @DisplayName("should fail to search the portfolios of a non-existent user")
    public void findByUserFailTest() {
        final Integer userId = 1000;
        when(userService.findById(userId)).thenThrow(new StockManagementException(""));

        assertThrows(StockManagementException.class, () -> {
            service.findByUser(userId);
        });

        verify(userService, times(1)).findById(userId);
    }

    @Test
    @DisplayName("should update a Portfolio from User")
    public void updateTest() {
        final Integer userId = 123;
        final Integer portfolioId = 43;
        final User user = User.builder().id(userId).build();
        final Portfolio portfolio = createPortfolio(portfolioId);
        user.addPortfolio(portfolio);
        when(userService.findById(userId)).thenReturn(user);

        final Portfolio portfolioUpdate = Portfolio.builder()
                .id(portfolioId)
                .name("Stock Options")
                .description("Stock Options")
                .build();

        final Portfolio actual = service.update(userId, portfolioUpdate);
        assertNotNull(actual);
        assertEquals(portfolioUpdate.getDescription(), actual.getDescription());
        assertEquals(portfolioUpdate.getName(), actual.getName());
        verify(userService, times(1)).findById(userId);
    }

    @Test
    @DisplayName("should return an error when updating a non-existing portfolio")
    public void updateNonExistentPortfolioTest() {
        final int userId = 123;
        final User user = User.builder().id(userId).build();
        when(userService.findById(userId)).thenReturn(user);

        final Portfolio portfolioUpdate = Portfolio.builder().id(5).build();

        assertThrows(StockManagementException.class, () -> {
            final Portfolio actual = service.update(userId, portfolioUpdate);
        });

        verify(userService, times(1)).findById(userId);
    }

    @Test
    @DisplayName("should search for a portfolio by id")
    public void findByIdTest() {
        final Integer userId = 123;
        final Integer portfolioId = 43;
        final User user = User.builder().id(userId).build();
        final Portfolio portfolio = createPortfolio(portfolioId);
        user.addPortfolio(portfolio);
        when(userService.findById(userId)).thenReturn(user);

        final Portfolio actual = service.findById(userId, portfolioId);
        assertNotNull(actual);
        assertEquals(portfolio, actual);
        verify(userService, times(1)).findById(userId);
    }

    @Test
    @DisplayName("should return an error when searching for a non-existent portfolio by id")
    public void findByIdNonExistentPortfolioTest() {
        final Integer userId = 123;
        final Integer portfolioId = 43;
        final User user = User.builder().id(userId).build();
        when(userService.findById(userId)).thenReturn(user);

        assertThrows(StockManagementException.class, () -> {
            service.findById(userId, portfolioId);
        });

        verify(userService, times(1)).findById(userId);
    }

    @Test
    @DisplayName("should remove a portfolio from User")
    public void deleteTest() {
        final Integer userId = 123;
        final Integer portfolioId = 43;
        final User user = User.builder().id(userId).build();
        final Portfolio portfolio = createPortfolio(portfolioId);
        user.addPortfolio(portfolio);
        when(userService.findById(userId)).thenReturn(user);

        service.delete(userId, portfolioId);
        assertTrue(user.getPortfolios().isEmpty());

        verify(userService, times(1)).findById(userId);
    }

    private Portfolio createPortfolio(final Integer portfolioId) {
        return Portfolio.builder()
                .id(portfolioId)
                .name("Real estate fund")
                .description("Brazilian FII")
                .build();
    }

}