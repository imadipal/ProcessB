package com.knowtheprocessbackend.knowtheprocessbackend.controller;

import com.knowtheprocessbackend.knowtheprocessbackend.model.Question;
import com.knowtheprocessbackend.knowtheprocessbackend.model.UserQuestion;
import com.knowtheprocessbackend.knowtheprocessbackend.service.AuthenticationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private AuthenticationService authenticationService;

    @GetMapping("/get-random-question")
    public ResponseEntity<?> getRandomQuestion(Authentication authentication) {
        return authenticationService.getRandomQuestion(authentication);
    }

    @PostMapping("/mark-question")
    public ResponseEntity<?> markQuestionAsDone(@RequestParam Long questionId, Authentication authentication) {
        return authenticationService.markQuestionAsDone(questionId, authentication);
    }

    @GetMapping("/completed-questions")
    public ResponseEntity<List<Question>> getCompletedQuestions(Authentication authentication) {
        return authenticationService.getCompletedQuestions(authentication);
    }
}
