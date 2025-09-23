package com.uade.tpo.marketplace.controllers.user;

import com.uade.tpo.marketplace.entity.User;
import com.uade.tpo.marketplace.service.User.UserService;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    // GET /api/v1/users → devuelve todos los usuarios
    @GetMapping
    public ResponseEntity<List<User>> getAllUsers() {
        List<User> users = userService.getAllUsers();
        return ResponseEntity.ok(users);
    }

    // GET /api/v1/users/search?email=algo@ejemplo.com → busca por email
    @GetMapping("/search")
    public ResponseEntity<User> getUserByEmail(@RequestParam String email) {
        return userService.getUserByEmail(email)
                .map(ResponseEntity::ok) // si existe, devuelve 200 con el usuario
                .orElseGet(() -> ResponseEntity.notFound().build()); // si no existe, devuelve 404
    }
}
