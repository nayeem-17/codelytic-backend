package com.example.codelytic.course;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.codelytic.course.model.schema.Course;

public interface CourseRepository extends JpaRepository<Course, Long> {
    List<Course> findByAuthor(String author);
}
