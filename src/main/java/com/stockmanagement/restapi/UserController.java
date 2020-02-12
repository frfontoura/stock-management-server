package com.stockmanagement.restapi;

import com.stockmanagement.infra.security.SecurityUtils;
import com.stockmanagement.restapi.dto.UserDTO;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author frfontoura
 * @version 1.0 18/01/2020
 */
@AllArgsConstructor
@RestController
@RequestMapping("/api/users")
public class UserController {

    private final SecurityUtils securityUtils;

    @GetMapping(path = "/me")
    public ResponseEntity<UserDTO> getCurrentUser() {
        return ResponseEntity.ok().body(new UserDTO(securityUtils.getCurrentUser()));
    }

}

