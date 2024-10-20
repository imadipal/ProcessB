package com.knowtheprocessbackend.knowtheprocessbackend.service;
import com.knowtheprocessbackend.knowtheprocessbackend.model.Question;
import com.knowtheprocessbackend.knowtheprocessbackend.model.User;
import com.knowtheprocessbackend.knowtheprocessbackend.model.UserQuestion;
import com.knowtheprocessbackend.knowtheprocessbackend.repository.QuestionRepository;
import com.knowtheprocessbackend.knowtheprocessbackend.repository.UserQuestionRepository;
import com.knowtheprocessbackend.knowtheprocessbackend.repository.UserRepository;
import com.knowtheprocessbackend.knowtheprocessbackend.security.JwtUtil;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.ResponseEntity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.stream.Collectors;

@Service
public class AuthenticationService implements UserDetailsService {

    private final UserRepository userRepository;
    private final QuestionRepository questionRepository;
    private final UserQuestionRepository userQuestionRepository;
    private final JwtUtil jwtUtil;
    private final PasswordEncoder passwordEncoder;
    @Autowired
    public AuthenticationService(UserRepository userRepository,
                                 QuestionRepository questionRepository,
                                 UserQuestionRepository userQuestionRepository,
                                 JwtUtil jwtUtil,
                                 PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.questionRepository = questionRepository;
        this.userQuestionRepository = userQuestionRepository;
        this.jwtUtil = jwtUtil;
        this.passwordEncoder = passwordEncoder;
    }

    // Register a new user
    public ResponseEntity<?> register(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRole("USER");
        userRepository.save(user);
        return ResponseEntity.ok("User registered successfully!");
    }

    // Inside your login method
    public ResponseEntity<?> login(User user, HttpServletResponse response) {
        User foundUser = userRepository.findByUsername(user.getUsername());
        if (foundUser != null && passwordEncoder.matches(user.getPassword(), foundUser.getPassword())) {
            String token = jwtUtil.generateToken(foundUser.getUsername());

            // Create a cookie with the JWT token
            Cookie cookie = new Cookie("token", token);
            cookie.setHttpOnly(true); // Prevent JavaScript access to the cookie
            cookie.setSecure(false); // Only send cookie over HTTPS
            cookie.setPath("/"); // Set cookie path
            cookie.setMaxAge(86400); // Set cookie expiration time (1 day)

            // Add the cookie to the response
            response.addCookie(cookie);
            return ResponseEntity.ok("User logged in successfully!");
        }
        return ResponseEntity.status(401).body("Invalid credentials");
    }

    public ResponseEntity<?> logout(HttpServletResponse response) {
        Cookie cookie = new Cookie("token", null);
        cookie.setHttpOnly(true);
        cookie.setSecure(true);
        cookie.setPath("/");
        cookie.setMaxAge(0); // Set cookie to expire immediately
        response.addCookie(cookie);
        return ResponseEntity.ok("User logged out successfully!");
    }

    // Add a new question to the database (Admin role)
    public ResponseEntity<?> addQuestion(Question question) {
        questionRepository.save(question);
        return ResponseEntity.ok("Question added successfully!");
    }

    // Fetch a random question that the user has not yet completed
    public ResponseEntity<?> getRandomQuestion(Authentication authentication) {
        String username = authentication.getName();
        Long userId = getUserIdFromUsername(username);  // Get userId based on username

        // Fetch all completed questions for the user
        List<UserQuestion> completedQuestions = userQuestionRepository.findByUserIdAndDoneTrue(userId);
        List<Long> completedQuestionIds = completedQuestions.stream()
                .map(UserQuestion::getQuestionId)
                .collect(Collectors.toList());

        // Fetch a random question that is not completed yet
        List<Question> availableQuestions = questionRepository.findAll();
        List<Question> remainingQuestions = availableQuestions.stream()
                .filter(q -> !completedQuestionIds.contains(q.getId()))
                .collect(Collectors.toList());

        if (remainingQuestions.isEmpty()) {
            return ResponseEntity.ok("No new questions available");
        }

        // Pick a random question
        Random random = new Random();
        Question randomQuestion = remainingQuestions.get(random.nextInt(remainingQuestions.size()));

        return ResponseEntity.ok(randomQuestion);
    }

    // Mark a question as completed for the user
    public ResponseEntity<?> markQuestionAsDone(Long questionId, Authentication authentication) {
        String username = authentication.getName();
        Long userId = getUserIdFromUsername(username);

        Optional<Question> questionOpt = questionRepository.findById(questionId);
        if (questionOpt.isEmpty()) {
            return ResponseEntity.status(404).body("Question not found");
        }

        Question question = questionOpt.get();

        // Check if this UserQuestion entry already exists
        Optional<UserQuestion> userQuestionOpt = userQuestionRepository.findByUserIdAndQuestionId(userId, questionId);
        UserQuestion userQuestion = userQuestionOpt.orElse(new UserQuestion());


        userQuestion.setUserId(userId); // Set userId
        userQuestion.setQuestionId(questionId); // Set questionId
        userQuestion.setDone(true); // Mark as done

        userQuestionRepository.save(userQuestion);
        return ResponseEntity.ok("Question marked as done!");
    }

    // Fetch the list of completed questions for the user
    public ResponseEntity<List<Question>> getCompletedQuestions(Authentication authentication) {
        String username = authentication.getName();
        Long userId = getUserIdFromUsername(username);

        // Fetch completed questions for the user
        List<UserQuestion> completedQuestions = userQuestionRepository.findByUserIdAndDoneTrue(userId);

        // Extract and return the questions
        List<Question> questions = completedQuestions.stream()
                .map(userQuestion -> questionRepository.findById(userQuestion.getQuestionId()).orElse(null))
                .filter(question -> question != null) // Filter out null questions
                .collect(Collectors.toList());

        return ResponseEntity.ok(questions);
    }

    // Load user by username for authentication
    @Override
    public UserDetails loadUserByUsername(String username) {
        User user = userRepository.findByUsername(username);
        return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(), new ArrayList<>());
    }

    // Helper method to get user ID from username
    private Long getUserIdFromUsername(String username) {
        return userRepository.findByUsername(username).getId();
    }
}
