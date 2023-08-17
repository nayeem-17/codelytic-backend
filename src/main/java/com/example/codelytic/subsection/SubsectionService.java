package com.example.codelytic.subsection;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.codelytic.course.CourseRepository;
import com.example.codelytic.course.model.schema.Course;

@Service
public class SubsectionService {
    @Autowired
    private SubsectionRepository subsectionRepository;
    @Autowired
    private CourseRepository courseRepository;

    public Subsection createSubsection(Long courseId, String subsectionName) {
        System.out.println(
                "courseId: " + courseId + " subsectionName: " + subsectionName);
        Course course = this.courseRepository.findById(courseId).orElseThrow(
                () -> new RuntimeException("Course not found"));

        Subsection subsection = new Subsection();
        int subsectionSize = course.getSubsections().size();
        subsection.setName(subsectionName);
        course.getSubsections().add(subsection);
        return this.courseRepository.save(course).getSubsections().get(subsectionSize);
    }

    public Subsection updateSubsection(Long subsectionId, String updatedSubsectionName) {
        Subsection subsection = this.subsectionRepository.findById(subsectionId).orElseThrow(
                () -> new RuntimeException("Course not found"));
        subsection.setName(updatedSubsectionName);
        this.subsectionRepository.save(subsection);
        return subsection;
    }

    public List<Subsection> getSubsections(Long courseId) {
        Course course = this.courseRepository.findById(courseId).orElseThrow(
                () -> new RuntimeException("Course not found"));
        return course.getSubsections();
    }

    public Subsection getSubsection(Long courseId, Long subsectionId) {
        return this.subsectionRepository.findById(subsectionId).orElseThrow(
                () -> new RuntimeException("Subsection not found"));
    }

}
