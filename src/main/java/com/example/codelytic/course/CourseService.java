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

}
