package com.example.codelytic.progress;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.example.codelytic.progress.model.CourseProgress;
import com.example.codelytic.progress.model.DailyActivity;
import com.example.codelytic.progress.model.DailyProgress;
import com.example.codelytic.progress.model.Progress;
import com.example.codelytic.user.UserRepository;
import com.example.codelytic.user.model.User;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class ProgressService {
        // @Autowired
        // private ProgressRepository progressRepository;
        @Autowired
        private UserRepository userRepository;
        @Autowired
        private ProgressRepository progressRepository;

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
        public List<DailyProgress> getDailyProgress(String email) {
                User user = this.userRepository.findByEmail(email)
                                .orElseThrow(() -> new UsernameNotFoundException(
                                                String.format("User with email %s not found", email)));
                Progress progress = user.getProgress();
                // first check if created or not
                List<DailyProgress> dailyProgress = progress.getDailyProgress();

                return dailyProgress;
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

        public void addActivity(DailyActivity activity, String email) {
                User user = this.userRepository.findByEmail(email)
                                .orElseThrow(() -> new UsernameNotFoundException(
                                                String.format("User with email %s not found", email)));
                Progress progress = user.getProgress();
                Date today = new Date();

                // Define the desired date format
                SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");

                // Format the Date object as a String
                String formattedDate = dateFormat.format(today);

                System.out.println(
                                "today: " + today + " "
                                                + LocalDate.now().atStartOfDay(ZoneId.systemDefault()).toInstant());
                // first check if created or not
                DailyProgress dailyProgress = progress.getDailyProgress().stream().filter(
                                dailyProgress_ -> dailyProgress_
                                                .getDate()
                                                .equals(formattedDate))
                                .findFirst()
                                .orElse(null);

                if (dailyProgress == null) {
                        DailyProgress newDailyProgress = new DailyProgress();
                        newDailyProgress.setDate(formattedDate);
                        newDailyProgress.setActivities(
                                        new ArrayList<>());
                        progress.getDailyProgress().add(newDailyProgress);
                        newDailyProgress.setProgress(progress);
                        // newDailyProgress.s
                        this.progressRepository.save(progress);
                        dailyProgress = newDailyProgress;
                        System.out.println(
                                        "dailyProgress: " + dailyProgress.getDate() + " "
                                                        + dailyProgress.getActivities().size());
                } else {
                        log.info("activity: " + activity + " " + dailyProgress.getActivities().size());
                }

                if (activity.equals(DailyActivity.LOGGED_IN)) {
                        if (dailyProgress.getActivities()
                                        .stream().filter(activity_ -> activity_.equals(DailyActivity.LOGGED_IN))
                                        .findFirst()
                                        .orElse(null) == null) {
                                dailyProgress.getActivities().add(DailyActivity.LOGGED_IN);
                        } else {
                                log.info("already logged in");
                        }
                } else if (activity.equals(DailyActivity.COMPLETED_LECTURE)) {
                        log.info("dailyProgress: " + dailyProgress.getDate() + " "
                                        + dailyProgress.getActivities().size()
                                        + " " + dailyProgress.getActivities().contains(
                                                        DailyActivity.COMPLETED_LECTURE));
                        dailyProgress.getActivities().add(DailyActivity.COMPLETED_LECTURE);
                } else if (activity.equals(DailyActivity.COMPLETED_QUIZ)) {
                        log.info(
                                        "dailyProgress: " + dailyProgress.getDate() + " "
                                                        + dailyProgress.getActivities().size()
                                                        + " " + dailyProgress.getActivities().contains(
                                                                        DailyActivity.COMPLETED_QUIZ));
                        dailyProgress.getActivities().add(DailyActivity.COMPLETED_QUIZ);
                }
                this.progressRepository.save(progress);
        }
}
