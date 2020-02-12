package com.stockmanagement.domain.assets;

import com.stockmanagement.domain.portfolio.Portfolio;
import com.stockmanagement.domain.users.User;
import com.stockmanagement.domain.users.UserService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

/**
 * @author frfontoura
 * @version 1.0 11/02/2020
 */
@ExtendWith(MockitoExtension.class)
public class AssetServiceTest {

    @Mock
    private UserService userService;

    @InjectMocks
    private AssetService assetService;

    @Test
    @DisplayName("should add an asset to a user's portfolio")
    public void addAssetToUserPortfolioTest() {
        final int userId = 99;
        final int portfolioId = 1;
        final User user =  Mockito.mock(User.class);
        when(userService.findById(userId)).thenReturn(user);

        final Asset asset = createAsset();
        when(user.addAssetToPortfolio(portfolioId, asset)).thenReturn(asset);

        final Asset actual = assetService.addAssetToUserPortfolio(userId, portfolioId, asset);

        assertNotNull(actual);
        assertEquals(asset, actual);

        verify(userService, times(1)).findById(userId);
        verify(user, times(1)).addAssetToPortfolio(portfolioId, asset);
    }

    @Test
    @DisplayName("should sale an asset from the user's portfolio")
    public void assetSaleTest() {
        final int userId = 99;
        final int portfolioId = 1;
        final User user =  Mockito.mock(User.class);
        when(userService.findById(userId)).thenReturn(user);

        final Asset asset = createAsset();
        assetService.assetSale(userId, portfolioId, asset);

        verify(userService, times(1)).findById(userId);
        verify(user, times(1)).assetSale(portfolioId, asset);
    }

    @Test
    @DisplayName("should find the assets from user's portfolio")
    public void findByUserPortfolioTest() {
        final int userId = 99;
        final int portfolioId = 1;
        final User user =  Mockito.mock(User.class);
        final Portfolio portfolio = Mockito.mock(Portfolio.class);

        when(userService.findById(userId)).thenReturn(user);
        when(user.findPortfolioById(portfolioId)).thenReturn(Optional.of(portfolio));
        when(portfolio.getAssets()).thenReturn(Arrays.asList(createAsset()));

        final List<Asset> actual = assetService.findByUserPortfolio(userId, portfolioId);

        assertNotNull(actual);
        assertEquals(1, actual.size());

        verify(userService, times(1)).findById(userId);
        verify(user, times(1)).findPortfolioById(portfolioId);
        verify(portfolio, times(1)).getAssets();
    }

    private Asset createAsset() {
        return Asset.builder()
                .id(123)
                .symbol("IVVB11")
                .amount(10)
                .lastPrice(new BigDecimal(15.5))
                .build();
    }

}
