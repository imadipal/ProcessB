package com.knowtheprocessbackend.knowtheprocessbackend.repository;

import com.knowtheprocessbackend.knowtheprocessbackend.model.Question;
import org.springframework.data.jpa.repository.JpaRepository;

public interface QuestionRepository extends JpaRepository<Question, Long> {
}
