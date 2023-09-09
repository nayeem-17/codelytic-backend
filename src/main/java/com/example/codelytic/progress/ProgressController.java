package com.example.codelytic.progress;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.codelytic.progress.model.CourseProgress;
import com.example.codelytic.progress.model.DailyProgress;
import com.example.codelytic.progress.model.Progress;

@RequestMapping("/progress")
@RestController
public class ProgressController {
    @Autowired
    private ProgressService progressService;

    /*
     * get progress for a specific course for a specific user
     */
    @GetMapping("/{courseId}")
    public CourseProgress getProgress(
            @PathVariable Long courseId) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        return progressService.getProgress(email, courseId);
    }

    @GetMapping
    public Progress getProgress() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        return progressService.getProgress(email);
    }

    @GetMapping(value = "/percentage")
    public ResponseEntity<?> getProgressInPercentage() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        Object response = progressService.getProgressInPercentage(email);
        return ResponseEntity.ok().body(response);
    }

    @GetMapping("/daily-progress")
    public ResponseEntity<?> getDailyProgress() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        List<DailyProgress> response = progressService.getDailyProgress(email);
        return ResponseEntity.ok().body(response);
    }
}
