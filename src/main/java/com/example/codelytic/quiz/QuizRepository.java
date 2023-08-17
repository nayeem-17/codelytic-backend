package com.example.codelytic.quiz;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.codelytic.quiz.model.Quiz;

public interface QuizRepository extends JpaRepository<Quiz, Long> {

}
