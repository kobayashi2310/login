package org.login.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.login.model.User;
import org.login.model.dto.UserDTO;
import org.login.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AdminService {

    private final UserRepository userRepository;

    public List<UserDTO> findByUser() {

        List<User> users = userRepository.findByRole(User.Role.USER);

        return users.stream()
                .map(user -> new UserDTO(user.getId(), user.getName()))
                .toList();

    }

    @Transactional
    public void deleteUser(Long id) { userRepository.deleteById(id); }

}
