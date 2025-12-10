package com.code.Sales.Stock.service;

import com.code.Sales.Stock.Repository.UserCredRepo;
import com.code.Sales.Stock.model.UserCred;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserCredService {

    private final UserCredRepo repo;
    private final BCryptPasswordEncoder passwordEncoder;

    // ❌ Remove loggedInUser — it is dangerous (explained below)

    public UserCredService(UserCredRepo repo, BCryptPasswordEncoder passwordEncoder) {
        this.repo = repo;
        this.passwordEncoder = passwordEncoder;
    }

    // LOGIN
    public ResponseEntity<String> login(String username, String password) {

        UserCred user = repo.findByUsername(username.toLowerCase())
                .orElse(null);

        if (user == null) {
            return ResponseEntity.status(404).body("User not found");
        }

        if (!passwordEncoder.matches(password, user.getPassword())) {
            return ResponseEntity.status(401).body("Invalid password");
        }

        return ResponseEntity.ok("Login successful");
    }

    // LOGOUT (This is meaningless without JWT/session)
    public ResponseEntity<String> logout() {
        return ResponseEntity.ok("Logged out");
    }

    // SIGNUP
    public ResponseEntity<String> signup(String username, String password, String email) {

        try {
            username = username.toLowerCase();

            if (repo.existsByUsername(username)) {
                return ResponseEntity.badRequest().body("Username already exists");
            }

            if (repo.existsByUsername(email.toLowerCase())) {
                return ResponseEntity.badRequest().body("Email already exists");
            }

            UserCred user = new UserCred();
            user.setUsername(username);
            user.setPassword(passwordEncoder.encode(password));
            user.setEmail(email);

            repo.save(user);

            return ResponseEntity.ok("Signup successful");

        } catch (Exception e) {
            return ResponseEntity.status(500).body("Signup failed: " + e.getMessage());
        }
    }
}