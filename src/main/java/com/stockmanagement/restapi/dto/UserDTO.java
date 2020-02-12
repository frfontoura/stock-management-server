package com.stockmanagement.restapi.dto;

import com.stockmanagement.domain.users.User;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;

/**
 * @author frfontoura
 * @version 1.0 03/02/2020
 */
@NoArgsConstructor
@Getter @Setter
public class UserDTO {

    private Integer id;

    @NotEmpty
    private String name;

    @NotEmpty
    private String username;

    @NotEmpty
    private String password;

    @NotEmpty
    private String email;

    public UserDTO(final User user) {
        id = user.getId();
        name = user.getName();
        username = user.getUsername();
        email = user.getEmail();
    }

    public User toEntity() {
        return User.builder()
                .id(id)
                .name(name)
                .username(username)
                .password(password)
                .email(email)
                .build();
    }
}
