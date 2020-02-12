package com.stockmanagement.domain.portfolio;

import com.stockmanagement.domain.assets.Asset;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author frfontoura
 * @version 1.0 11/02/2020
 */
@ExtendWith(MockitoExtension.class)
public class PortfolioTest {

    @Test
    @DisplayName("should return an error when add an asset to the assets list")
    public void getAssetsTest() {
        final Portfolio portfolio = createPortfolio();
        final Asset asset = Asset.builder().build();

        assertThrows(UnsupportedOperationException.class, () -> {
            portfolio.getAssets().add(asset);
        });
    }

    @Test
    @DisplayName("should add an asset to a portfolio")
    public void addAssetTest() {
        final Portfolio portfolio = createPortfolio();
        final Asset asset = createAsset("BCFF11", 10);

        final Asset actual = portfolio.addAsset(asset);

        assertNotNull(actual);
        assertEquals(portfolio, actual.getPortfolio());
        assertEquals(asset.getLastPrice(), actual.getAveragePrice());
        assertFalse(portfolio.getAssets().isEmpty());
    }

    @Test
    @DisplayName("should add 3 assets with the same symbol")
    public void addAssetSameSymbolTest() {
        final Portfolio portfolio = createPortfolio();

        portfolio.addAsset(createAsset("BCFF11", 5));
        portfolio.addAsset(createAsset("BCFF11", 10));
        final Asset actual = portfolio.addAsset(createAsset("BCFF11", 15));

        assertNotNull(actual);
        assertEquals(portfolio, actual.getPortfolio());
        assertTrue(actual.getAveragePrice().compareTo(new BigDecimal(10)) == 0);
        assertEquals(30, actual.getAmount());
        assertEquals(1, portfolio.getAssets().size());
    }

    @Test
    @DisplayName("should add 3 assets with the different symbol")
    public void addAssetDifferentSymbolTest() {
        final Portfolio portfolio = createPortfolio();

        portfolio.addAsset(createAsset("BCFF11", 5));
        portfolio.addAsset(createAsset("KNRI11", 15));
        portfolio.addAsset(createAsset("MXRF11", 12.5));

        assertEquals(3, portfolio.getAssets().size());
    }

    @Test
    @DisplayName("should find an asset by id")
    public void findAssetByIdTest() {
        final Portfolio portfolio = createPortfolio();
        portfolio.addAsset(createAsset("BCFF11", 5));

        final Optional<Asset> optional = portfolio.findAssetById(123);
        final Asset asset = optional.get();

        assertNotNull(asset);
        assertEquals("BCFF11", asset.getSymbol());
    }

    @Test
    @DisplayName("should make a partial sale")
    public void assetPartialSaleTest() {
        final Portfolio portfolio = createPortfolio();
        portfolio.addAsset(createAsset("IVVB11", 5));

        final Asset assetSale = createAsset("IVVB11", 5);
        assetSale.setAmount(5);

        final Asset actual = portfolio.assetSale(assetSale);

        assertNotNull(actual);
        assertEquals(5, actual.getAmount());
    }

    @Test
    @DisplayName("should make a sale")
    public void assetSaleTest() {
        final Portfolio portfolio = createPortfolio();
        final Asset asset = createAsset("IVVB11", 5);
        portfolio.addAsset(asset);

        final Asset actual = portfolio.assetSale(asset);

        assertNull(actual);
    }

    private Asset createAsset(final String symbol, final double price) {
        return Asset.builder()
                .id(123)
                .symbol(symbol)
                .amount(10)
                .lastPrice(new BigDecimal(price))
                .build();
    }

    public Portfolio createPortfolio() {
        return Portfolio.builder()
                .id(1)
                .name("Stock Portfolio")
                .description("Stock portfolio with dividends")
                .build();
    }
}
