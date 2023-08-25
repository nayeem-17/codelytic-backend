package com.example.codelytic.progress;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.codelytic.progress.model.CourseProgress;

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
            @PathVariable String courseId) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        return progressService.getProgress(email, courseId);
    }
}
