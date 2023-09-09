package com.example.codelytic.progress.model;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import com.example.codelytic.quiz.model.Quiz;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.MapKeyColumn;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@JsonIgnoreProperties(value = { "createdAt", "updatedAt" })
@Data
@NoArgsConstructor

public class QuizProgress {
    public QuizProgress(Quiz quiz) {
        if (quiz == null)
            return;
        System.out.println(quiz);

        int questionLength = quiz.getQuestions().size();
        for (int i = 0; i < questionLength; i++) {
            this.questions.put(quiz.getQuestions().get(i).getId(), -1);
        }
        this.quizId = quiz.getId();
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long quizId;

    @ElementCollection
    @CollectionTable(name = "quiz_progress_question", joinColumns = @JoinColumn(name = "quiz_progress_id"))
    @MapKeyColumn(name = "question_id")
    @Column(name = "user_answer")
    private Map<Long, Integer> questions = new HashMap<>();

    @CreationTimestamp
    @Column(updatable = false, name = "created_at")
    private LocalDateTime createdAt;
    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    public float getQuizProgressInPercentage() {
        int questionLength = this.questions.size();
        int answeredQuestion = 0;
        for (Map.Entry<Long, Integer> entry : this.questions.entrySet()) {
            if (entry.getValue() == 1) {
                answeredQuestion++;
            }
        }
        if (questionLength == 0)
            return 0.0f;
        return (float) answeredQuestion / questionLength;
    }
}
