package com.stockmanagement.restapi;

import com.stockmanagement.domain.assets.Asset;
import com.stockmanagement.domain.assets.AssetService;
import com.stockmanagement.restapi.dto.AssetDTO;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author frfontoura
 * @version 1.0 10/02/2020
 */
@AllArgsConstructor
@RestController
@RequestMapping("/api/users/{userId}/portfolios/{portfolioId}/assets")
public class AssetController {

    private final AssetService service;

    /**
     * Adds an asset to a user's portfolio
     * 
     * @param portfolioId
     * @param assetDTO
     * @return
     */
    @PostMapping
    public ResponseEntity<AssetDTO> addAssetToUserPortfolio(@PathVariable final int userId, @PathVariable final int portfolioId, @Valid @RequestBody final AssetDTO assetDTO) {
        final Asset asset = service.addAssetToUserPortfolio(userId, portfolioId, assetDTO.toEntity());
        final URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(asset.getId()).toUri();
        return ResponseEntity.created(uri).body(new AssetDTO(asset));
    }

    /**
     * Returns the assets of a portfolio
     *
     * @param portfolioId
     * @return
     */
    @GetMapping
    public ResponseEntity<List<AssetDTO>> findByUserPortfolio(@PathVariable final int userId, @PathVariable final int portfolioId) {
        final List<Asset> assets = service.findByUserPortfolio(userId, portfolioId);
        return ResponseEntity.ok().body(assets.stream().map(asset -> new AssetDTO(asset)).collect(Collectors.toList()));
    }

    /**
     * Performs full or partial sale of an asset, if the total amount is 0, removes the asset portfolio.
     *
     * @param portfolioId
     * @param assetDTO
     * @return
     */
    @PutMapping(path = "/{assetId}")
    public ResponseEntity<AssetDTO> assetSale(@PathVariable final int userId, @PathVariable final int portfolioId, @PathVariable final int assetId, @Valid @RequestBody final AssetDTO assetDTO) {
        assetDTO.setId(assetId);
        final Asset asset = service.assetSale(userId, portfolioId, assetDTO.toEntity());

        if(asset == null) {
            return ResponseEntity.ok().body(null);
        }

        return ResponseEntity.ok().body(new AssetDTO(asset));
    }

}
