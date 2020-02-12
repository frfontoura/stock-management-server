package com.stockmanagement.restapi.dto;

import com.stockmanagement.domain.portfolio.Portfolio;
import com.stockmanagement.domain.users.User;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;

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

    public PortfolioDTO(final Portfolio portfolio) {
        id = portfolio.getId();
        userId = portfolio.getUser().getId();
        name = portfolio.getName();
        description = portfolio.getDescription();
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
