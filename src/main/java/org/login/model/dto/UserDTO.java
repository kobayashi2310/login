package org.login.model.dto;

import org.login.model.User;

public record UserDTO(
        Long id,
        String name,
        User.Role role
) { }
