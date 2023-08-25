package com.example.codelytic.progress;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.example.codelytic.progress.model.CourseProgress;
import com.example.codelytic.user.UserRepository;
import com.example.codelytic.user.model.User;

@Service
public class ProgressService {
    @Autowired
    private ProgressRepository progressRepository;
    @Autowired
    private UserRepository userRepository;

    /*
     * @todo
     * have to dop this in a single query
     */
    public CourseProgress getProgress(String email, String courseId) {
        User user = this.userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException(
                        String.format("User with email %s not found", email)));
        return user.getProgress().getCourseProgresses()
                .stream()
                .filter(courseProgress -> courseProgress.getCourse().getId().equals(courseId))
                .findFirst() // Find the first matching courseProgress
                .orElse(null); // Return null if no matching courseProgress is found

    }
}
