package com.stockmanagement.domain.assets;


import com.stockmanagement.domain.portfolio.Portfolio;
import lombok.*;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * Represents an asset, stocks or REITs (Real Estate Investment Trust)
 *
 * @author frfontoura
 * @version 1.0 10/02/2020
 */
@Getter @Setter
@AllArgsConstructor @Builder
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Entity
@Table(name = "stm_assets")
public class Asset {

    @EqualsAndHashCode.Include
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "portfolio_id", nullable = false, updatable = false)
    private Portfolio portfolio;

    @Column(name = "symbol", nullable = false, length = 10)
    private String symbol;

    @Column(name = "avg_price", nullable = false, precision = 19, scale = 2)
    private BigDecimal averagePrice;

    @Column(name = "last_price", nullable = false, precision = 19, scale = 2)
    private BigDecimal lastPrice;

    @Column(name = "amount", nullable = false)
    private int amount;

    @Column(name = "last_date", nullable = false)
    private LocalDate lastDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "asset_type", nullable = false)
    private AssetType type;

    /**
     * Returns the average price times the amount
     *
     * @return
     */
    public BigDecimal getAveragePriceTotal() {
        return averagePrice.multiply(new BigDecimal(amount));
    }

    /**
     * Returns the last price times the amount
     *
     * @return
     */
    public BigDecimal getLastPriceTotal() {
        return lastPrice.multiply(new BigDecimal(amount));
    }

    private Asset() {
        this(null, null, null, null, null, 0, null, null);
    }

}
