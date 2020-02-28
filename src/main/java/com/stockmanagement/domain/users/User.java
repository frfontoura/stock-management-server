package com.stockmanagement.domain.users;

import com.stockmanagement.domain.assets.Asset;
import com.stockmanagement.domain.portfolio.Portfolio;
import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Getter @Setter
@AllArgsConstructor
@Builder
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Entity
@Table(name = "stm_users", uniqueConstraints = {
        @UniqueConstraint(name = "stm_users_email_un", columnNames = "email"),
        @UniqueConstraint(name = "stm_users_username_un", columnNames = "username")
})
public class User {

    @EqualsAndHashCode.Include
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "name", length = 100, nullable = false)
    private String name;

    @Column(name = "username", length = 50, nullable = false)
    private String username;

    @Column(name = "password", length = 60, nullable = false)
    private String password;

    @Column(name = "email", length = 100, nullable = false)
    private String email;

    @Enumerated(value = EnumType.STRING)
    @Column(name = "role", length = 15, nullable = false)
    private Role role;

    @OrderBy("name")
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private final List<Portfolio> portfolios = new ArrayList<>();

    /**
     * Returns an unmodifiable copy of the portfolios
     *
     * @return
     */
    public List<Portfolio> getPortfolios() {
        return Collections.unmodifiableList(portfolios);
    }

    /**
     * adds a new <code>Portfolio</code> to the <code>User</code>
     * @param portfolio
     */
    public void addPortfolio(@NonNull final Portfolio portfolio) {
        portfolio.setUser(this);
        portfolios.add(portfolio);
    }

    /**
     * Search a user's portfolio by id
     *
     * @param portfolioId
     * @return
     */
    public Optional<Portfolio> findPortfolioById(final int portfolioId) {
        return portfolios.stream().filter(p -> p.getId().equals(portfolioId)).findFirst();
    }

    /**
     * Remove a portfolio by id
     *
     * @param portfolioId
     */
    public void removePortfolio(final int portfolioId) {
        findPortfolioById(portfolioId).ifPresent(portfolio -> {
            portfolio.setUser(null);
            portfolios.remove(portfolio);
        });
    }

    /**
     * Adds an asset to a user's portfolio
     *
     * @param portfolioId
     * @param asset
     */
    public Asset addAssetToPortfolio(final int portfolioId, @NonNull final Asset asset) {
        final Portfolio portfolio = findPortfolioById(portfolioId).orElse(null);
        if(portfolio != null) {
            return portfolio.addAsset(asset);
        }
        return null;
    }

    /**
     * Performs full or partial sale of an asset, if the total amount is 0, removes the asset portfolio.
     *
     * @param portfolioId
     * @param asset
     */
    public Asset assetSale(final int portfolioId, @NonNull final Asset asset) {
        final Portfolio portfolio = findPortfolioById(portfolioId).orElse(null);
        if(portfolio != null){
          return portfolio.assetSale(asset);
        }
        return null;
    }

    private User() {
        this(null, null, null, null, null, null);
    }

}

