package com.stockmanagement.domain.assets;

import com.stockmanagement.domain.portfolio.Portfolio;
import com.stockmanagement.domain.users.User;
import com.stockmanagement.domain.users.UserService;
import com.stockmanagement.infra.exception.StockManagementException;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author frfontoura
 * @version 1.0 10/02/2020
 */
@Service
@AllArgsConstructor
public class AssetService {

    private final UserService userService;

    /**
     * Adds an asset to a user's portfolio
     *
     * @param userId
     * @param portfolioId
     * @param asset
     * @return
     */
    @PreAuthorize("@userOwner.isOwner(principal, #userId)")
    @Transactional
    public Asset addAssetToUserPortfolio(final int userId, final int portfolioId, @NonNull final Asset asset) {
        final User user = userService.findById(userId);
        return user.addAssetToPortfolio(portfolioId, asset);
    }

    /**
     * Performs full or partial sale of an asset, if the total amount is 0, removes the asset portfolio.
     *
     * @param userId
     * @param portfolioId
     * @param asset
     * @return
     */
    @PreAuthorize("@userOwner.isOwner(principal, #userId)")
    @Transactional
    public Asset assetSale(final int userId, final int portfolioId, @NonNull final Asset asset) {
        final User user = userService.findById(userId);
        return user.assetSale(portfolioId, asset);
    }

    /**
     * Returns the assets of a portfolio
     *
     * @param userId
     * @param portfolioId
     * @return
     */
    @PreAuthorize("@userOwner.isOwner(principal, #userId)")
    public List<Asset> findByUserPortfolio(final int userId, final int portfolioId) {
        final User user = userService.findById(userId);
        final Portfolio portfolio = user.findPortfolioById(portfolioId).orElseThrow(() -> new StockManagementException("Portfolio with id:" + portfolioId + " not found."));
        return portfolio.getAssets();
    }
}
