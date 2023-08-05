package com.example.codelytic.course;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.codelytic.course.model.schema.Course;

public interface CourseRepository extends JpaRepository<Course, Long> {

}
