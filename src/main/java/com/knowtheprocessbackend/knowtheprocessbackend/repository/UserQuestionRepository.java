package com.knowtheprocessbackend.knowtheprocessbackend.repository;

import com.knowtheprocessbackend.knowtheprocessbackend.model.UserQuestion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserQuestionRepository extends JpaRepository<UserQuestion, Long> {

    // Fetch questions for a user that are marked as done
    List<UserQuestion> findByUserIdAndDoneTrue(Long userId);

    // Fetch a specific UserQuestion by userId and questionId
    Optional<UserQuestion> findByUserIdAndQuestionId(Long userId, Long questionId);
}
