package com.example.codelytic.quiz;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.codelytic.quiz.model.Quiz;
import com.example.codelytic.subsection.Subsection;
import com.example.codelytic.subsection.SubsectionRepository;

@Service
public class QuizService {
    @Autowired
    private QuizRepository quizRepository;
    @Autowired
    private SubsectionRepository subsectionRepository;

    public Quiz createQuiz(Long subsectionId, Quiz quiz) {
        Subsection subsection = this.subsectionRepository.findById(subsectionId).orElseThrow(
                () -> new RuntimeException("Subsection not found"));
        subsection.setQuiz(quiz);
        return this.subsectionRepository.save(subsection).getQuiz();
    }

    public boolean updateQuiz(
            Long quizId,
            Quiz updatedQuiz) {
        Quiz quiz = this.quizRepository.findById(quizId).orElse(null);
        if (quiz == null)
            return false;
        quiz.setQuestions(updatedQuiz.getQuestions());
        this.quizRepository.save(quiz);

        return true;
    }

    // delete a quiz
    public boolean deleteQuiz(Long subsectionId, Long quizId) {
        Subsection subsection = this.subsectionRepository.findById(subsectionId).orElseThrow(
                () -> new RuntimeException("Subsection not found"));
        if (subsection.getQuiz() == null)
            return false;
        subsection.setQuiz(null);
        this.subsectionRepository.save(subsection);
        return true;
    }

    // get a quiz
    public Quiz getQuiz(Long quizId) {
        return this.quizRepository.findById(quizId).orElse(null);
    }

}
