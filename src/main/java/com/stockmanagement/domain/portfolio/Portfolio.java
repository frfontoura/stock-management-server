package com.stockmanagement.domain.portfolio;

import com.stockmanagement.domain.assets.Asset;
import com.stockmanagement.domain.users.User;
import lombok.*;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Represents a grouping of assets, stocks or REITs (Real Estate Investment Trust)
 *
 * @author frfontoura
 * @version 1.0 19/01/2020
 */
@Getter @Setter
@AllArgsConstructor
@Builder
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Entity
@Table(name = "stm_portfolios")
public class Portfolio {

    @EqualsAndHashCode.Include
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false, updatable = false)
    private User user;

    @Column(name = "name", nullable = false, length = 50)
    private String name;

    @Column(name = "description", nullable = false, length = 280)
    private String description;

    @OneToMany(mappedBy = "portfolio", cascade = CascadeType.ALL, orphanRemoval = true)
    private final List<Asset> assets = new ArrayList<>();

    /**
     * Returns an unmodifiable copy of the assets
     *
     * @return
     */
    public List<Asset> getAssets() {
        return Collections.unmodifiableList(assets);
    }

    /**
     * Adds an asset to the portfolio, if one already exists with the same code, the quantity will be added and a new average price will be calculated.
     *
     * @param asset
     * @return
     */
    public Asset addAsset(@NonNull final Asset asset) {
        final Asset previousAsset = assets.stream().filter(a -> a.getSymbol().equals(asset.getSymbol())).findFirst().orElse(null);

        if(previousAsset != null) {
            final int newAmount = previousAsset.getAmount() + asset.getAmount();
            final BigDecimal newAvg = previousAsset.getAveragePriceTotal().add(asset.getLastPriceTotal()).divide(new BigDecimal(newAmount));
            previousAsset.setAmount(newAmount);
            previousAsset.setLastDate(asset.getLastDate());
            previousAsset.setAveragePrice(newAvg);
            return previousAsset;
        } else {
            asset.setAveragePrice(asset.getLastPrice());
            asset.setPortfolio(this);
            assets.add(asset);
        }

        return asset;
    }

    /**
     * Search for an asset in the portfolio
     *
     * @param assetId
     * @return
     */
    public Optional<Asset> findAssetById(final int assetId) {
        return assets.stream().filter(a -> a.getId().equals(assetId)).findFirst();
    }

    /**
     * Performs full or partial sale of an asset, if the total amount is 0, removes the asset portfolio.
     *
     * @param asset
     *
     * @return
     */
    public Asset assetSale(@NonNull final Asset asset) {
        final Asset previousAsset = findAssetById(asset.getId()).orElse(null);
        if(previousAsset != null) {
            final int newAmount = previousAsset.getAmount() - asset.getAmount();
            previousAsset.setAmount(newAmount);
            if(newAmount <= 0) {
                previousAsset.setPortfolio(null);
                assets.remove(previousAsset);
                return null;
            }
        }
        return previousAsset;
    }

    /**
     * Returns the number of different assets in the portfolio
     *
     * @return
     */
    public int getAmountOfAssets() {
        return assets.stream().map(a -> a.getSymbol()).collect(Collectors.toSet()).size();
    }

    /**
     * Returns the sum of all assets in the portfolio, based on the value of the last purchase
     *
     * @return
     */
    public BigDecimal getTotalValueOfPortolio() {
        return assets.stream().map(a -> a.getLastPriceTotal()).reduce(new BigDecimal(0), (a1, a2) -> a1.add(a2));
    }

    private Portfolio() {
        this(null, null, null, null);
    }

}
