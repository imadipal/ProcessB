package com.knowtheprocessbackend.knowtheprocessbackend.controller;
import com.knowtheprocessbackend.knowtheprocessbackend.model.User;
import com.knowtheprocessbackend.knowtheprocessbackend.service.AuthenticationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private AuthenticationService authenticationService;

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody User user) {
        return authenticationService.register(user);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody User user) {
        return authenticationService.login(user);
    }
}
