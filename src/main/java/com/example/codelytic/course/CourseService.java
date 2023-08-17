package com.example.codelytic.course;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.codelytic.course.model.schema.Course;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class CourseService {

    @Autowired
    private CourseRepository courseRepository;

    public void createCourse(Course course) {
        System.out.println(course);
        courseRepository.save(course);
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

    // public Long addLecture(Long id, Lecture lecture) {
    // Objects.requireNonNull(id, "Course ID must not be null");
    // Objects.requireNonNull(lecture, "Lecture must not be null");
    // // fetch the subsection
    // Subsection subsection;
    // Optional<Course> optionalCourse = courseRepository.findById(id);
    // if (optionalCourse.isPresent()) {
    // Course course = optionalCourse.get();
    // Subsection subsection = new Subsection();
    // subsection.getLecture().add(lecture);
    // Subsection savedSubsection = courseRepository.save(course).getSubsections()
    // .get(course.getSubsections().size() - 1);
    // Long subsectionId = savedSubsection.getId();
    // return subsectionId;
    // } else {
    // // Handle the case when the course with the given id is not found
    // // For example, you can throw an exception or log an error.
    // // It depends on your application's requirements.
    // }
    // return (long) -1;
    // }

    // public void addQuiz(Long courseId, Long subsectionId, Quiz quiz) {
    // Objects.requireNonNull(courseId, "Course ID must not be null");
    // Objects.requireNonNull(quiz, "Quiz must not be null");

    // Optional<Course> optionalCourse = courseRepository.findById(courseId);
    // if (optionalCourse.isPresent()) {
    // System.out.println("course is present");
    // Course course = optionalCourse.get();
    // List<Subsection> subsections = course.getSubsections();

    // // Find the Subsection with the specified subsectionId
    // Optional<Subsection> optionalSubsection = subsections.stream()
    // .filter(subsection -> subsection.getId().equals(subsectionId))
    // .findFirst();

    // if (optionalSubsection.isPresent()) {
    // System.out.println("subsection is present");

    // Subsection subsection = optionalSubsection.get();
    // List<Quiz> quizzes = subsection.getQuiz();
    // quizzes.add(quiz);
    // System.out.println(quiz);
    // // Save the updated Subsection
    // courseRepository.save(course);
    // } else {
    // // Handle the case when the Subsection with the given subsectionId is not
    // found
    // // For example, you can throw an exception or log an error.
    // // It depends on your application's requirements.
    // }
    // } else {
    // // Handle the case when the Course with the given courseId is not found
    // // For example, you can throw an exception or log an error.
    // // It depends on your application's requirements.
    // }
    // }

    public Course getCourse(Long courseId) {
        Optional<Course> course = courseRepository.findById(courseId);
        if (course.isPresent()) {
            return course.get();
        } else {
            return null;
        }
    }

}
