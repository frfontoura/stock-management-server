package com.stockmanagement.restapi;

import com.stockmanagement.domain.portfolio.Portfolio;
import com.stockmanagement.domain.portfolio.PortfolioService;
import com.stockmanagement.domain.users.User;
import com.stockmanagement.infra.security.SecurityUtils;
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
@RequestMapping("/api/users/portfolios")
public class PortfolioController {

    private final SecurityUtils securityUtils;
    private final PortfolioService service;

    @PostMapping
    public ResponseEntity<PortfolioDTO> createPortfolio(@Valid @RequestBody final PortfolioDTO portfolioDTO) {
        final User user = securityUtils.getCurrentUser();
        final Portfolio portfolio = service.createPortfolio(user.getId(), portfolioDTO.toEntity());
        final URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(portfolio.getId()).toUri();
        return ResponseEntity.created(uri).body(new PortfolioDTO(portfolio));
    }

    @GetMapping
    public ResponseEntity<List<PortfolioDTO>> findByUser() {
        final User user = securityUtils.getCurrentUser();
        final List<Portfolio> portfolios = service.findByUser(user.getId());
        return ResponseEntity.ok().body(portfolios.stream().map(p -> new PortfolioDTO(p)).collect(Collectors.toList()));
    }

    @PutMapping(path = "/{portfolioId}")
    public ResponseEntity<PortfolioDTO> update(@PathVariable final int portfolioId, @Valid @RequestBody final PortfolioDTO portfolioDTO) {
        final User user = securityUtils.getCurrentUser();
        portfolioDTO.setId(portfolioId);
        final Portfolio portfolio = service.update(user.getId(), portfolioDTO.toEntity());
        return ResponseEntity.ok().body(new PortfolioDTO(portfolio));
    }

    @GetMapping(path = "/{portfolioId}")
    public ResponseEntity<PortfolioDTO> findById(@PathVariable final int portfolioId) {
        final User user = securityUtils.getCurrentUser();
        final Portfolio portfolio = service.findById(user.getId(), portfolioId);
        return ResponseEntity.ok().body(new PortfolioDTO(portfolio));
    }

    @DeleteMapping(path = "/{portfolioId}")
    public ResponseEntity<Void> delete(@PathVariable final int portfolioId) {
        final User user = securityUtils.getCurrentUser();
        service.delete(user.getId(), portfolioId);
        return ResponseEntity.ok().build();
    }

}
