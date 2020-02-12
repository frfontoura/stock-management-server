package com.stockmanagement.restapi;

import com.stockmanagement.domain.portfolio.Portfolio;
import com.stockmanagement.domain.portfolio.PortfolioService;
import com.stockmanagement.restapi.dto.PortfolioDTO;
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
 * @version 1.0 18/01/2020
 */
@AllArgsConstructor
@RestController
@RequestMapping("/api/users/{userId}/portfolios")
public class PortfolioController {

    private final PortfolioService service;

    @PostMapping
    public ResponseEntity<PortfolioDTO> createPortfolio(@PathVariable final int userId, @Valid @RequestBody final PortfolioDTO portfolioDTO) {
        final Portfolio portfolio = service.createPortfolio(userId, portfolioDTO.toEntity());
        final URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(portfolio.getId()).toUri();
        return ResponseEntity.created(uri).body(new PortfolioDTO(portfolio));
    }

    @GetMapping
    public ResponseEntity<List<PortfolioDTO>> findByUser(@PathVariable final int userId) {
        final List<Portfolio> portfolios = service.findByUser(userId);
        return ResponseEntity.ok().body(portfolios.stream().map(p -> new PortfolioDTO(p)).collect(Collectors.toList()));
    }

    @PutMapping(path = "/{portfolioId}")
    public ResponseEntity<PortfolioDTO> update(@PathVariable final int userId, @PathVariable final int portfolioId, @Valid @RequestBody final PortfolioDTO portfolioDTO) {
        portfolioDTO.setId(portfolioId);
        final Portfolio portfolio = service.update(userId, portfolioDTO.toEntity());
        return ResponseEntity.ok().body(new PortfolioDTO(portfolio));
    }

    @GetMapping(path = "/{portfolioId}")
    public ResponseEntity<PortfolioDTO> findById(@PathVariable final int userId, @PathVariable final int portfolioId) {
        final Portfolio portfolio = service.findById(userId, portfolioId);
        return ResponseEntity.ok().body(new PortfolioDTO(portfolio));
    }

    @DeleteMapping(path = "/{portfolioId}")
    public ResponseEntity<Void> delete(@PathVariable final int userId, @PathVariable final int portfolioId) {
        service.delete(userId, portfolioId);
        return ResponseEntity.ok().build();
    }

}
