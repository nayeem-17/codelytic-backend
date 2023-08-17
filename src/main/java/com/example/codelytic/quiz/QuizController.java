package com.example.codelytic.quiz;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.codelytic.quiz.model.Quiz;

@RequestMapping(value = "/course/quiz")
@RestController
public class QuizController {
    @Autowired
    private QuizService quizService;

    /*
     * create a quiz for a course id
     */
    @PostMapping(value = "/{subsectionId}")
    ResponseEntity<?> createQuiz(
            @PathVariable Long subsectionId,
            @RequestBody Quiz quiz) {
        Quiz createdQuiz = this.quizService.createQuiz(subsectionId, quiz);
        Map<String, Long> response = new HashMap<>();
        response.put(
                "quizId",
                createdQuiz.getId());
        return ResponseEntity.ok().body(response);
    }

    /*
     * update a quiz
     */
    @PutMapping(value = "/{quizId}")
    ResponseEntity<?> updateQuiz(
            @PathVariable Long quizId,
            @RequestBody Quiz quiz) {
        boolean isSuccessful = this.quizService.updateQuiz(quizId, quiz);
        Map<String, String> response = new HashMap<>();

        if (isSuccessful) {
            response.put(
                    "status",
                    "updated");
            return ResponseEntity.ok().body(response);
        } else {
            response.put(
                    "status",
                    "failed");
            return ResponseEntity.badRequest().body(response);
        }
    }

    /*
     * get a quiz
     */
    @GetMapping(value = "/{quizId}")
    ResponseEntity<?> getQuiz(
            @PathVariable Long quizId) {
        Quiz quiz = this.quizService.getQuiz(quizId);
        if (quiz == null) {
            Map<String, String> response = new HashMap<>();
            response.put(
                    "status",
                    "failed");
            return ResponseEntity.badRequest().body(response);
        }
        return ResponseEntity.ok().body(quiz);
    }

    /*
     * delete a quiz
     */
    @DeleteMapping(value = "/{subsectionId}/{quizId}")
    ResponseEntity<?> deleteQuiz(
            @PathVariable Long subsectionId,
            @PathVariable Long quizId) {
        boolean isSuccessful = this.quizService.deleteQuiz(subsectionId, quizId);
        Map<String, String> response = new HashMap<>();
        if (isSuccessful) {
            response.put(
                    "status",
                    "deleted");
            return ResponseEntity.ok().body(response);
        } else {
            response.put(
                    "status",
                    "failed");
            return ResponseEntity.badRequest().body(response);
        }
    }

}
