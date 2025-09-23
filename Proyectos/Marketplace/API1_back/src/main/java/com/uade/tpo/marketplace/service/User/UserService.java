package com.uade.tpo.marketplace.service.User;

import com.uade.tpo.marketplace.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.util.List;
import java.util.Optional;

public interface UserService {
    User getCurrentUser();

    List<User> getAllUsers();

    Optional<User> getUserByEmail(String email);
}