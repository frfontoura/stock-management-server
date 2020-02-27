package com.stockmanagement.restapi.dto;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.stockmanagement.domain.portfolio.Portfolio;
import com.stockmanagement.domain.users.User;
import com.stockmanagement.restapi.serializer.MoneySerializer;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;
import java.math.BigDecimal;

/**
 * @author frfontoura
 * @version 1.0 04/02/2020
 */
@NoArgsConstructor
@Getter @Setter
public class PortfolioDTO {

    private Integer id;
    private Integer userId;

    @NotEmpty
    private String name;

    @NotEmpty
    private String description;

    private int amountOfAssets;

    @JsonSerialize(using = MoneySerializer.class)
    private BigDecimal totalValueOfPortfolio;

    public PortfolioDTO(final Portfolio portfolio) {
        id = portfolio.getId();
        userId = portfolio.getUser().getId();
        name = portfolio.getName();
        description = portfolio.getDescription();

        amountOfAssets = portfolio.getAmountOfAssets();
        totalValueOfPortfolio = portfolio.getTotalValueOfPortolio();
    }

    public Portfolio toEntity() {
        final User user = User.builder()
                .id(userId)
                .build();

        return Portfolio.builder()
                .id(id)
                .user(user)
                .name(name)
                .description(description)
                .build();
    }
}
