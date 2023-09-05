package com.example.codelytic.lecture;

import java.util.HashMap;
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

@RestController
@RequestMapping(value = "/course/lecture")
public class LectureController {
    @Autowired
    private LectureService lectureService;

    /*
     * create a lecture
     */
    @PostMapping(value = "/{subsectionId}")
    ResponseEntity<?> createLecture(
            @PathVariable Long subsectionId,
            @RequestBody Lecture lecture) {
        Lecture createdLecture = this.lectureService.createLecture(subsectionId, lecture);
        Map<String, Long> response = new HashMap<>();
        response.put(
                "lectureId",
                createdLecture.getId());
        return ResponseEntity.ok().body(response);
    }

    /*
     * update a lecture
     */
    @PutMapping(value = "/{lectureId}")
    ResponseEntity<?> updateLecture(
            @PathVariable Long lectureId,
            @RequestBody Lecture lecture) {
        this.lectureService.updateLecture(lectureId, lecture);
        Map<String, String> response = new HashMap<>();
        response.put(
                "status",
                "updated");
        return ResponseEntity.ok().body(response);
    }

    /*
     * get a lecture
     */
    @GetMapping(value = "/{lectureId}")
    ResponseEntity<?> getLecture(
            @PathVariable Long lectureId)

    {
        Map<String, String> response = new HashMap<>();
        try {
            Lecture lecture = this.lectureService.getLecture(lectureId);
            return ResponseEntity.ok().body(lecture);
        } catch (Exception e) {
            response.put(
                    "status",
                    "failed");
        }
        return ResponseEntity.ok().body(response);
    }

    @DeleteMapping(value = "/{lectureId}")
    ResponseEntity<?> deleteLecture(
            @PathVariable Long lectureId) {
        Map<String, String> response = new HashMap<>();
        try {
            this.lectureService.deleteLecture(lectureId);
            response.put(
                    "status",
                    "deleted");
        } catch (Exception e) {
            response.put(
                    "status",
                    "failed");
        }
        return ResponseEntity.ok().body(response);
    }

}
