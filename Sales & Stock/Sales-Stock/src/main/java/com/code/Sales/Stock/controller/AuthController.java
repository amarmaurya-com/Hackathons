package com.code.Sales.Stock.controller;

import com.code.Sales.Stock.model.UserCred;
import com.code.Sales.Stock.service.JwtService;
import com.code.Sales.Stock.service.UserCredService;
import org.apache.catalina.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private JwtService jwtService;
    @Autowired
    private AuthenticationManager authenticationManager;

    private final UserCredService service;

    public AuthController(UserCredService service) {
        this.service = service;
    }

    // Signup
    @PostMapping("/signup")
    public ResponseEntity<String> signup(@RequestBody UserCred user) {
        return service.signup(user);
    }


    // Login
    @PostMapping("/login")
    public String login(@RequestBody UserCred user) {

        Authentication authentication = authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword()));

        return authentication.isAuthenticated()?jwtService.generateToken(user.getUsername()):"Fail";
    }


    // Logout
    @PostMapping("/logout")
    public ResponseEntity<String> logout() {
        return service.logout();
    }
}
