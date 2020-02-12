package com.stockmanagement.restapi;

import com.stockmanagement.domain.users.SignUpService;
import com.stockmanagement.domain.users.User;
import com.stockmanagement.restapi.dto.UserDTO;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/**
 * Controller responsible for handing user registration
 *
 * @author frfontoura
 * @version 1.0 08/01/2020
 */
@RestController
@AllArgsConstructor
public class SignUpController {

    private final SignUpService service;

    /**
     * Registers a new user
     *
     * @param user
     * @return
     */
    @PostMapping(path = "/signup")
    public ResponseEntity<UserDTO> signUp(@Valid @RequestBody final UserDTO user) {
        final User newUser = service.signUp(user.toEntity());
        return ResponseEntity.ok().body(new UserDTO(newUser));
    }

}
