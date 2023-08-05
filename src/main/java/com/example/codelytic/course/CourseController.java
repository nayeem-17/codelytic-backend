package com.example.codelytic.course;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.codelytic.course.model.dto.CreateCourseDTO;
import com.example.codelytic.course.model.dto.UpdateCourseDTO;
import com.example.codelytic.course.model.schema.Course;
import com.example.codelytic.course.model.schema.Lecture;
import com.example.codelytic.course.model.schema.Quiz;

import io.swagger.v3.core.util.Json;

@RestController
@RequestMapping("/course")
public class CourseController {
    @Autowired
    private CourseService courseService;

    @GetMapping("/")
    List<Course> getCourses() {
        return courseService.getCourses();
    }

    @PostMapping
    ResponseEntity<Object> createCourse(
            @RequestBody CreateCourseDTO courseDTO) {
        if (courseDTO == null) {
            throw new IllegalArgumentException(
                    "The request body is empty or does not contain the required data for the CreateCourseDTO object.");
        }
        System.out.println(courseDTO.getAuthor());
        System.out.println(courseDTO);
        Course course = new Course();
        course.setAuthor(courseDTO.getAuthor());
        course.setTitle(courseDTO.getTitle());
        course.setIcon(courseDTO.getIcon());
        course.setLive(false);
        course.setPremium(false);
        courseService.createCourse(course);
        return ResponseEntity.ok().build();
    }

    @PutMapping
    ResponseEntity<Object> updateCourse(
            @RequestBody UpdateCourseDTO courseDTO) {
        if (courseDTO == null) {
            throw new IllegalArgumentException(
                    "The request body is empty or does not contain the required data for the CreateCourseDTO object.");
        }
        if (courseDTO.getId() < 1) {
            throw new IllegalArgumentException(
                    "the course id must be present");
        }
        Course updatedCourse = new Course();

        updatedCourse.setId(courseDTO.getId());
        updatedCourse.setAuthor(courseDTO.getAuthor());
        updatedCourse.setIcon(courseDTO.getIcon());
        updatedCourse.setLive(courseDTO.isLive());
        updatedCourse.setPremium(courseDTO.isPremium());
        updatedCourse.setTitle(courseDTO.getTitle());

        courseService.updateCourse(updatedCourse);

        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    ResponseEntity<Object> deleteCourse(
            @PathVariable Long id) {
        if (id < 1) {
            throw new IllegalArgumentException(
                    "the course id must be present");
        }
        courseService.deleteCourse(id);
        return ResponseEntity.ok().build();
    }

    @PostMapping("{id}/subsection/lecture")
    ResponseEntity<Object> createLecture(
            @PathVariable Long id,
            @RequestBody Lecture lecture) {
        // first create a subsection then add the subsection
        Long subsectionId = courseService.createSubsection(id, lecture);
        Object responseJson = new Object() {
            public Long subsection = subsectionId;
        };
        // responseJson.put("subsection", subsectionId);

        return ResponseEntity.ok().body(responseJson);
    }

    @PostMapping("{courseId}/subsection/{id}/quiz")
    ResponseEntity<Json> createQuiz(
            @PathVariable Long courseId,
            @RequestBody Quiz quiz,
            @PathVariable Long id) {
        courseService.addQuiz(courseId, id, quiz);
        return ResponseEntity.ok().build();
    }
}

// http://localhost:8000/swagger-ui/index.html#/
// swagger ui link
