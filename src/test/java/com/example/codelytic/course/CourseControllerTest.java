package com.example.codelytic.course;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class CourseControllerTest {

    @Autowired
    public CourseController controller;

    @Test
    public void contextLoads() throws Exception {
        assert controller != null;
    }

}
