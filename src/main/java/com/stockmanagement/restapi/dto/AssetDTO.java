package com.stockmanagement.restapi.dto;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.stockmanagement.domain.assets.Asset;
import com.stockmanagement.domain.assets.AssetType;
import com.stockmanagement.domain.portfolio.Portfolio;
import com.stockmanagement.restapi.serializer.MoneySerializer;
import lombok.*;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * @author frfontoura
 * @version 1.0 10/02/2020
 */
@NoArgsConstructor
@Getter @Setter
@AllArgsConstructor @Builder
public class AssetDTO {

    private Integer id;
    private int portfolioId;

    @NotEmpty
    private String symbol;

    @JsonSerialize(using = MoneySerializer.class)
    private BigDecimal averagePrice;

    @JsonSerialize(using = MoneySerializer.class)
    @NotNull
    private BigDecimal lastPrice;

    @Min(0)
    private int amount;

    @NotNull
    private LocalDate lastDate;

    @NotNull
    private AssetType type;

    @JsonSerialize(using = MoneySerializer.class)
    private BigDecimal averagePriceTotal;

    @JsonSerialize(using = MoneySerializer.class)
    private BigDecimal lastPriceTotal;

    public AssetDTO(final Asset asset) {
        id = asset.getId();
        portfolioId = asset.getPortfolio().getId();
        symbol = asset.getSymbol();
        averagePrice = asset.getAveragePrice();
        lastPrice = asset.getLastPrice();
        amount = asset.getAmount();
        lastDate = asset.getLastDate();
        type = asset.getType();

        averagePriceTotal = asset.getAveragePriceTotal();
        lastPriceTotal = asset.getLastPriceTotal();
    }

    public Asset toEntity() {
        final Portfolio portfolio = Portfolio.builder().id(portfolioId).build();

        return Asset.builder()
                .id(id)
                .portfolio(portfolio)
                .symbol(symbol)
                .averagePrice(averagePrice)
                .lastPrice(lastPrice)
                .amount(amount)
                .lastDate(lastDate)
                .type(type)
                .build();
    }

}
