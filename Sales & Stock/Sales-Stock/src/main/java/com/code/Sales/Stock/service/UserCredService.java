package com.code.Sales.Stock.service;

import com.code.Sales.Stock.Repository.UserCredRepo;
import com.code.Sales.Stock.model.UserCred;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserCredService {

    private final UserCredRepo repo;
    BCryptPasswordEncoder encoder  = new  BCryptPasswordEncoder(12);


    // ❌ Remove loggedInUser — it is dangerous (explained below)

    public UserCredService(UserCredRepo repo) {
        this.repo = repo;
    }

    // LOGIN
//    public ResponseEntity<String> login(String username, String password) {
//
//        UserCred user = repo.findByUsername(username.toLowerCase());
//
//        if (user == null) {
//            return ResponseEntity.status(404).body("User not found");
//        }
//
//        if (!passwordEncoder.matches(password, user.getPassword())) {
//            return ResponseEntity.status(401).body("Invalid password");
//        }
//
//        return ResponseEntity.ok("Login successful");
//    }
//
    // LOGOUT (This is meaningless without JWT/session)
    public ResponseEntity<String> logout() {
        return ResponseEntity.ok("Logged out");
    }

    // SIGNUP
    public ResponseEntity<String> signup(UserCred user) {

        if (user == null) {
            return ResponseEntity.badRequest().body("User data is missing");
        }

        String username = user.getUsername().toLowerCase();
        String email = user.getEmail().toLowerCase();

        if (repo.existsByUsername(username)) {
            return ResponseEntity.badRequest().body("Username already exists");
        }

        user.setUsername(username);
        user.setEmail(email);
        user.setPassword(encoder.encode(user.getPassword()));

        repo.save(user);

        return ResponseEntity.ok("Signup successful");
    }
}