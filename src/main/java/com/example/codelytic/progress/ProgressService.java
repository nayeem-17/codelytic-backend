package com.example.codelytic.progress;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
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

@Service
public class ProgressService {
        // @Autowired
        // private ProgressRepository progressRepository;
        @Autowired
        private UserRepository userRepository;
        @Autowired
        private DailyProgressRepository dailyProgressRepository;
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
                        System.out.println(
                                        "dailyProgress: " + dailyProgress.getDate() + " "
                                                        + dailyProgress.getActivities().size());
                        System.out.println(
                                        "WWWWWWWWWWWWWWWWWWWHHHHHHHHHHHHHHHHHHHHHHHHHHYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYy");
                }

                if (activity.equals(DailyActivity.LOGGED_IN)) {
                        if (dailyProgress.getActivities()
                                        .stream().filter(
                                                        activity_ -> activity_
                                                                        .equals(DailyActivity.LOGGED_IN))
                                        .findFirst()
                                        .orElse(null) == null) {
                                dailyProgress.getActivities().add(DailyActivity.LOGGED_IN);
                        }

                } else {

                }
                this.progressRepository.save(progress);

                // this.userRepository.save(user);
        }
}
