package com.code.Sales.Stock.controller;

import com.code.Sales.Stock.model.UserCred;
import com.code.Sales.Stock.service.UserCredService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final UserCredService service;

    public AuthController(UserCredService service) {
        this.service = service;
    }

    // Signup
    @PostMapping("/signup")
    public ResponseEntity<String> signup(@RequestBody UserCred user) {
        return service.signup(user.getUsername(), user.getPassword(), user.getEmail());
    }


    // Login
    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody UserCred user) {
        return service.login(user.getUsername(), user.getPassword());
    }


    // Logout
    @PostMapping("/logout")
    public ResponseEntity<String> logout() {
        return service.logout();
    }
}
