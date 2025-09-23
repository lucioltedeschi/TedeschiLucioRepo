package com.uade.tpo.marketplace.repository;

import com.uade.tpo.marketplace.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    // Buscar un usuario por email (puede no existir)
    Optional<User> findByEmail(String email);

    // Buscar todos los usuarios
    @Query("select u from User u")
    List<User> findAllUsers();
}
