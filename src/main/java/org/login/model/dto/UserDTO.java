package org.login.model.dto;

import org.login.model.User;

public record UserDTO(
        Long id,
        String name
) {

    public static UserDTO fromEntity(User user) {
        if (user == null) return null;
        return new UserDTO(user.getId(), user.getName());
    }

}
