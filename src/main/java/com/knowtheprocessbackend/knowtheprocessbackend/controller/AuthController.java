package com.knowtheprocessbackend.knowtheprocessbackend.controller;
import com.knowtheprocessbackend.knowtheprocessbackend.model.User;
import com.knowtheprocessbackend.knowtheprocessbackend.service.AuthenticationService;
import jakarta.servlet.http.HttpServletResponse;
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
    public ResponseEntity<?> login(@RequestBody User user, HttpServletResponse response) {
        return authenticationService.login(user,response);
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpServletResponse response) {
        return authenticationService.logout(response);
    }
}
