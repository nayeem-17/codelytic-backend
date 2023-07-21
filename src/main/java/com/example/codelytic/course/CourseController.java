package com.example.codelytic.course;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/course")
public class CourseController {
    @GetMapping
    int hello() {
        return 0;
    }
}

//http://localhost:8000/swagger-ui/index.html#/
//swagger ui link
