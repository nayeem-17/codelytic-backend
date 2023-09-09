package com.example.codelytic.course;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.example.codelytic.course.model.schema.Course;
import com.example.codelytic.progress.model.CourseProgress;
import com.example.codelytic.progress.model.QuizProgress;
import com.example.codelytic.user.UserRepository;
import com.example.codelytic.user.model.User;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class CourseService {

    @Autowired
    private CourseRepository courseRepository;
    @Autowired
    private UserRepository userRepository;

    public Course createCourse(Course course) {
        System.out.println(course);
        return courseRepository.save(course);
    }

    public List<Course> getCourses() {
        return courseRepository.findAll();
    }

    public void updateCourse(Course updatedCourse) {
        Optional<Course> optionalCourse = courseRepository.findById(updatedCourse.getId());
        if (optionalCourse.isPresent()) {
            Course existingCourse = optionalCourse.get();

            // Update properties
            if (updatedCourse.getAuthor() != null) {
                existingCourse.setAuthor(updatedCourse.getAuthor());
            }
            if (updatedCourse.getTitle() != null) {
                existingCourse.setTitle(updatedCourse.getTitle());
            }
            if (updatedCourse.getIcon() != null) {
                existingCourse.setIcon(updatedCourse.getIcon());
            }
            // Handle boolean properties (assuming the default value is false if not
            // provided)
            existingCourse.setPremium(updatedCourse.isPremium());
            existingCourse.setLive(updatedCourse.isLive());
            existingCourse.setDescription(updatedCourse.getDescription());
            existingCourse.setTags(updatedCourse.getTags());
            courseRepository.save(existingCourse);
        } else {

        }
    }

    public void deleteCourse(Long courseId) {
        Objects.requireNonNull(courseId, "Course ID must not be null");

        Optional<Course> optionalCourse = courseRepository.findById(courseId);
        if (optionalCourse.isPresent()) {
            Course courseToDelete = optionalCourse.get();

            // Handle any necessary logic before deletion, if needed

            courseRepository.delete(courseToDelete);
        } else {
            // Handle the case when the course with the given id is not found
            // For example, you can throw an exception or log an error.
            // It depends on your application's requirements.
        }
    }

    public Course getCourse(Long courseId) {
        Optional<Course> course = courseRepository.findById(courseId);
        if (course.isPresent()) {
            return course.get();
        } else {
            return null;
        }
    }

    public List<Course> getCoursesByAuthor(String email) {
        return courseRepository.findByAuthor(email);
    }

    public void enrollCourse(String email, Long courseId) {
        Course course = this.courseRepository.findById(courseId).orElseThrow(
                () -> new IllegalArgumentException("Course with id " + courseId + " not found"));
        User currentUser = this.userRepository.findByEmail(email).orElseThrow(
                () -> new UsernameNotFoundException("User with email " + email + " not found"));
        // currentUser.getProgress().getCourseProgresses().add(course.getProgress());
        currentUser.getEnrolledCourse().add(course);

        // now create a complete course progress object based on course
        CourseProgress courseProgress = new CourseProgress(course);
        currentUser.getProgress().getCourseProgresses().add(courseProgress);
        this.userRepository.save(currentUser);
    }

    public void completeLecture(String email, Long courseId, Long subsectionId, Long lectureId) {
        User user = this.userRepository.findByEmail(email).orElseThrow(
                () -> new UsernameNotFoundException("User with email " + email + " not found"));
        var progress = user.getProgress().getCourseProgresses()
                .stream()
                .filter(
                        courseProgress -> courseProgress.getCourse().getId().equals(courseId))
                .findFirst().orElseThrow(
                        () -> new IllegalArgumentException("Course with id " + courseId + " not found"));
        Map<Long, Boolean> lectures = progress.getSubsectionsProgresses().stream()
                .filter(subsectionProgress -> subsectionProgress.getSubsectionId().equals(subsectionId))
                .findFirst().orElseThrow(
                        () -> new IllegalArgumentException("Subsection with id " + subsectionId + " not found"))
                .getLectures();
        if (lectures.get(lectureId) == true) {
            throw new IllegalArgumentException("Lecture with id " + lectureId + " already completed");
        } else {
            lectures.put(lectureId, true);
            this.userRepository.save(user);
        }
    }

    public void completeQuiz(String email, Long courseId, Long subsectionId, Map<Long, Integer> questionAnswers) {
        User user = this.userRepository.findByEmail(email).orElseThrow(
                () -> new UsernameNotFoundException("User with email " + email + " not found"));
        var progress = user.getProgress().getCourseProgresses()
                .stream()
                .filter(
                        courseProgress -> courseProgress.getCourse().getId().equals(courseId))
                .findFirst().orElseThrow(
                        () -> new IllegalArgumentException("Course with id " + courseId + " not found"));
        QuizProgress quizProgress = progress.getSubsectionsProgresses().stream()
                .filter(subsectionProgress -> subsectionProgress.getSubsectionId().equals(subsectionId))
                .findFirst().orElseThrow(
                        () -> new IllegalArgumentException("Subsection with id " + subsectionId + " not found"))
                .getQuizProgress();
        if (quizProgress.getQuizProgressInPercentage() != 0) {
            throw new IllegalArgumentException("Quiz for subsection id " + subsectionId + " already completed");
        } else {
            quizProgress.getQuestions().putAll(questionAnswers);
            // quizAnswers = questionAnswers;
            this.userRepository.save(user);
        }
    }

    public Boolean setLive(Long courseId) {
        Course course = this.courseRepository.findById(courseId).orElseThrow(
                () -> new IllegalArgumentException("Course with id " + courseId + " not found"));
        if (course.isLive()) {
            return false;
        }
        course.setLive(true);
        this.courseRepository.save(course);
        return true;
    }

    public Boolean setPremium(Long courseId) {
        Course course = this.courseRepository.findById(courseId).orElseThrow(
                () -> new IllegalArgumentException("Course with id " + courseId + " not found"));
        if (course.isPremium()) {
            course.setPremium(false);
            this.courseRepository.save(course);
            return false;
        }
        course.setPremium(true);
        this.courseRepository.save(course);
        return true;
    }

}
