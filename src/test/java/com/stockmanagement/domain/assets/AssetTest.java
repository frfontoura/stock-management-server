package com.stockmanagement.domain.assets;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.math.RoundingMode;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * @author frfontoura
 * @version 1.0 11/02/2020
 */
@ExtendWith(MockitoExtension.class)
public class AssetTest {

    @Test
    @DisplayName("should return average price times amount")
    public void getAveragePriceTotalTest() {
        final Asset asset = createAsset("IVVB11", 159.78);

        final BigDecimal avg = asset.getAveragePriceTotal().setScale(2, RoundingMode.HALF_UP);
        final BigDecimal expected = new BigDecimal(1597.8).setScale(2, RoundingMode.HALF_UP);

        assertEquals(expected, avg);
    }

    @Test
    @DisplayName("should return last price times amount")
    public void getLastPriceTotalTest() {
        final Asset asset = createAsset("IVVB11", 789.56);

        final BigDecimal avg = asset.getLastPriceTotal().setScale(2, RoundingMode.HALF_UP);
        final BigDecimal expected = new BigDecimal(7895.6).setScale(2, RoundingMode.HALF_UP);

        assertEquals(expected, avg);
    }

    private Asset createAsset(final String symbol, final double price) {
        return Asset.builder()
                .id(123)
                .symbol(symbol)
                .amount(10)
                .lastPrice(new BigDecimal(price))
                .averagePrice(new BigDecimal(price))
                .build();
    }
}
