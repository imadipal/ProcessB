package com.knowtheprocessbackend.knowtheprocessbackend.controller;

import com.knowtheprocessbackend.knowtheprocessbackend.model.Question;
import com.knowtheprocessbackend.knowtheprocessbackend.service.AuthenticationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private AuthenticationService authenticationService;

    @PostMapping("/add-question")
    public ResponseEntity<?> addQuestion(@RequestBody Question question) {
        return authenticationService.addQuestion(question);
    }
}
