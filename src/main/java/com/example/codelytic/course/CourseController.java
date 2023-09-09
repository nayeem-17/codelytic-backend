package com.example.codelytic.course;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
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
import com.example.codelytic.progress.ProgressService;
import com.example.codelytic.progress.model.DailyActivity;
import com.example.codelytic.tag.Tag;
import com.example.codelytic.tag.TagService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/course")
public class CourseController {
    @Autowired
    private CourseService courseService;
    @Autowired
    private TagService tagService;
    @Autowired
    private ProgressService progressService;

    @GetMapping("/")
    List<Course> getCourses() {
        return courseService.getCourses();
    }

    @GetMapping("/by-author")
    List<Course> getCoursesByAuthor() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        return courseService.getCoursesByAuthor(email);
    }

    @GetMapping("/{courseId}")
    public Course getCourse(
            @PathVariable Long courseId) {
        if (courseId < 1) {
            throw new IllegalArgumentException(
                    "the course id must be present");
        }
        return courseService.getCourse(courseId);
    }

    @PostMapping
    ResponseEntity<Object> createCourse(
            @RequestBody CreateCourseDTO courseDTO) {
        if (courseDTO == null) {
            throw new IllegalArgumentException(
                    "The request body is empty or does not contain the required data for the CreateCourseDTO object.");
        }
        // System.out.println(courseDTO.getAuthor());
        System.out.println(courseDTO);
        List<String> tags = new ArrayList<>();
        if (courseDTO.getTagIds().size() > 0) {
            courseDTO.getTagIds().forEach(tagId -> {
                if (tagId > 0) {
                    Tag tag = tagService.findById(tagId);
                    if (tag != null)
                        tags.add(tag.getName());
                }
            });
        }
        Course course = new Course();
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        course.setAuthor(email);
        course.setTitle(courseDTO.getTitle());
        course.setIcon(courseDTO.getIcon());
        course.setLive(false);
        course.setPremium(false);
        course.setDescription(courseDTO.getDescription());
        course.setTags(tags);
        course = this.courseService.createCourse(course);
        return ResponseEntity.ok().body(course);
    }

    @PutMapping
    ResponseEntity<Object> updateCourse(
            @RequestBody UpdateCourseDTO courseDTO) {
        log.trace("update course controller");
        if (courseDTO == null) {
            throw new IllegalArgumentException(
                    "The request body is empty or does not contain the required data for the CreateCourseDTO object.");
        }
        if (courseDTO.getId() < 1) {
            throw new IllegalArgumentException(
                    "the course id must be present");
        }
        Course updatedCourse = new Course();
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        updatedCourse.setId(courseDTO.getId());
        updatedCourse.setAuthor(email);
        updatedCourse.setIcon(courseDTO.getIcon());
        updatedCourse.setLive(courseDTO.isLive());
        updatedCourse.setPremium(courseDTO.isPremium());
        updatedCourse.setTitle(courseDTO.getTitle());
        updatedCourse.setDescription(courseDTO.getDescription());
        List<String> tags = new ArrayList<>();
        if (courseDTO.getTagIds().size() > 0) {
            courseDTO.getTagIds().forEach(tagId -> {
                if (tagId > 0) {
                    Tag tag = tagService.findById(tagId);
                    if (tag != null)
                        tags.add(tag.getName());
                }
            });
        }
        System.out.println(tags);
        updatedCourse.setTags(tags);
        System.out.println(
                "updated course: " + updatedCourse.toString());
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

    @PostMapping("/{courseId}/enroll")
    ResponseEntity<Object> enrollCourse(
            @PathVariable Long courseId) {
        if (courseId < 1) {
            throw new IllegalArgumentException(
                    "the course id must be present");
        }
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        courseService.enrollCourse(email, courseId);
        return ResponseEntity.ok().build();
    }

    @PostMapping(value = "{courseId}/complete/{subsectionId}/lecture/{lectureId}")
    ResponseEntity<?> completeLecture(
            @PathVariable Long courseId,
            @PathVariable Long subsectionId,
            @PathVariable Long lectureId) {
        Map<String, String> response = new HashMap<>();
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        try {
            this.courseService.completeLecture(email, courseId, subsectionId, lectureId);
            response.put(
                    "status",
                    "completed");
        } catch (Exception e) {
            response.put(
                    "status",
                    "failed");
        }
        try {
            this.progressService.addActivity(DailyActivity.COMPLETED_LECTURE, email);
        } catch (Exception e) {
            log.error(
                    "Error while adding daily activity: {}", e.getLocalizedMessage());
        }
        return ResponseEntity.ok().body(response);
    }

    @PostMapping(value = "{courseId}/complete/{subsectionId}/quiz")
    ResponseEntity<?> completeQuiz(
            @PathVariable Long courseId,
            @PathVariable Long subsectionId,
            @RequestBody Map<Long, Integer> questionAnswers) {
        Map<String, String> response = new HashMap<>();
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        try {
            this.courseService.completeQuiz(email, courseId, subsectionId, questionAnswers);
            response.put(
                    "status",
                    "completed");
        } catch (Exception e) {
            response.put(
                    "status",
                    "failed");
        }
        try {
            this.progressService.addActivity(DailyActivity.COMPLETED_QUIZ, email);
        } catch (Exception e) {
            log.error(
                    "Error while adding daily activity: {}", e.getLocalizedMessage());
        }
        return ResponseEntity.ok().body(response);
    }
}

// http://localhost:8000/swagger-ui/index.html#/
// swagger ui link
