package com.stockmanagement.domain.portfolio;

import com.stockmanagement.domain.users.User;
import com.stockmanagement.domain.users.UserService;
import com.stockmanagement.infra.exception.StockManagementException;
import lombok.AllArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Responsible for services related to the Portfolio entity
 *
 * @author frfontoura
 * @version 1.0 19/01/2020
 */
@Service
@AllArgsConstructor
public class PortfolioService {

    private final UserService userService;

    /**
     * Adds a new Portfolio to the User
     *
     * @param userId
     * @param portfolio
     * @return
     */
    @PreAuthorize("@userOwner.isOwner(principal, #userId)")
    @Transactional
    public Portfolio createPortfolio(final int userId, final Portfolio portfolio) {
        final User user = userService.findById(userId);
        user.addPortfolio(portfolio);
        return portfolio;
    }

    /**
     * Search all user portfolios
     *
     * @param userId
     * @return
     */
    @PreAuthorize("@userOwner.isOwner(principal, #userId)")
    public List<Portfolio> findByUser(final int userId) {
        final User user = userService.findById(userId);
        return user.getPortfolios();
    }

    /**
     * Update a portfolio
     *
     * @param portfolio
     * @return
     */
    @PreAuthorize("@userOwner.isOwner(principal, #userId)")
    @Transactional
    public Portfolio update(final int userId, final Portfolio portfolio) {
        final Portfolio portfolioUpdate = findById(userId, portfolio.getId());
        portfolioUpdate.setName(portfolio.getName());
        portfolioUpdate.setDescription(portfolio.getDescription());
        return portfolioUpdate;
    }

    /**
     * Search a user's portfolio by id
     *
     * @param userId
     * @param portfolioId
     * @return
     */
    @PreAuthorize("@userOwner.isOwner(principal, #userId)")
    public Portfolio findById(final int userId, final int portfolioId) {
        final User user = userService.findById(userId);
        return user.findPortfolioById(portfolioId).orElseThrow(() -> new StockManagementException("Portfolio with id:" + portfolioId + " not found."));
    }

    /**
     * Remove a user's portfolio
     *
     * @param userId
     * @param portfolioId
     */
    @PreAuthorize("@userOwner.isOwner(principal, #userId)")
    @Transactional
    public void delete(final int userId, final int portfolioId) {
        final User user = userService.findById(userId);
        user.removePortfolio(portfolioId);
    }

}
