package com.example.codelytic.progress;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.example.codelytic.progress.model.CourseProgress;
import com.example.codelytic.progress.model.Progress;
import com.example.codelytic.user.UserRepository;
import com.example.codelytic.user.model.User;

@Service
public class ProgressService {
    // @Autowired
    // private ProgressRepository progressRepository;
    @Autowired
    private UserRepository userRepository;

    /*
     * @todo
     * have to dop this in a single query
     */
    public CourseProgress getProgress(String email, Long courseId) {
        User user = this.userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException(
                        String.format("User with email %s not found", email)));
        return user.getProgress().getCourseProgresses()
                .stream()
                .filter(courseProgress -> courseProgress.getCourse().getId().equals(courseId))
                .findFirst() // Find the first matching courseProgress
                .orElse(null); // Return null if no matching courseProgress is found

    }

    public Progress getProgress(String email) {
        User user = this.userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException(
                        String.format("User with email %s not found", email)));
        System.out.println(user.getProgress().getId());
        return user.getProgress();
    }

    /*
     * @todo: have to do in one single query
     */
    public Map<Long, Float> getProgressInPercentage(String email) {

        User user = this.userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException(
                        String.format("User with email %s not found", email)));
        Progress progress = user.getProgress();
        Map<Long, Float> result = new HashMap<>();
        for (int i = 0; i < progress.getCourseProgresses().size(); i++) {
            result.put(progress.getCourseProgresses().get(i).getCourse().getId(),
                    progress.getCourseProgresses().get(i).getCourseProgressInPercentage());
        }
        return result;

    }
}
