package com.example.codelytic.subsection;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/course/subsection")

public class SubsectionController {

    @Autowired
    private SubsectionService subsectionService;

    /*
     * Get a single subsection for a single course
     */
    @GetMapping(value = "/{courseId}/{subsectionId}")
    ResponseEntity<?> getSubsection(
            @PathVariable("courseId") Long courseId,
            @PathVariable("subsectionId") Long subsectionId) {
        Map<String, String> response = new HashMap<>();
        System.out.println(
                "courseId: " + courseId + " subsectionId: " + subsectionId);
        try {
            Subsection subsection = this.subsectionService.getSubsection(courseId, subsectionId);
            return ResponseEntity.ok().body(subsection);
        } catch (Exception e) {
            response.put(
                    "status",
                    "failed");
        }
        return ResponseEntity.ok().body(response);
    }

    /*
     * create subsection for a specific course (given course id)
     */
    @PostMapping("/{courseId}")
    ResponseEntity<?> createSubsection(
            @PathVariable Long courseId,
            @RequestBody String subsectionName) {
        Subsection subsection = subsectionService.createSubsection(
                courseId,
                subsectionName);
        Map<String, Long> response = new HashMap<>();
        response.put(
                "subsectionId",
                subsection.getId());
        return ResponseEntity.ok().body(response);
    }

    /*
     * update the subsection content
     */
    @PutMapping(value = "/{subsectionId}")

    ResponseEntity<?> updateSubsection(
            @PathVariable Long subsectionId,
            @RequestBody String updatedSubsectionName) {
        Map<String, String> response = new HashMap<>();
        try {
            this.subsectionService.updateSubsection(
                    subsectionId,
                    updatedSubsectionName);
            response.put(
                    "status",
                    "success");
        } catch (Exception e) {
            response.put(
                    "status",
                    "failed");
        }
        return ResponseEntity.ok().body(response);
    }

    /*
     * get all subsections for a course
     */
    @GetMapping(value = "/{courseId}")
    ResponseEntity<?> getSubsections(
            @PathVariable Long courseId) {
        Map<String, String> response = new HashMap<>();
        try {
            var subsections = this.subsectionService.getSubsections(courseId);
            return ResponseEntity.ok().body(subsections);
        } catch (Exception e) {
            response.put(
                    "status",
                    "failed");
        }
        return ResponseEntity.ok().body(response);
    }

}