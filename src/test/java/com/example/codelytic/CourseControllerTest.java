package com.example.codelytic;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.util.Arrays;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import com.example.codelytic.course.CourseController;
import com.example.codelytic.course.CourseService;
import com.example.codelytic.course.model.dto.CreateCourseDTO;
import com.example.codelytic.course.model.schema.Course;
import com.example.codelytic.tag.Tag;
import com.example.codelytic.tag.TagService;
import com.fasterxml.jackson.databind.ObjectMapper;

@WebMvcTest(CourseController.class)
public class CourseControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private CourseService courseService;

    @MockBean
    private TagService tagService;

    @Test
    public void testCreateCourse() throws Exception {
        // Mock the authenticated user
        Authentication auth = new UsernamePasswordAuthenticationToken("test@example.com", "password");
        SecurityContextHolder.getContext().setAuthentication(auth);

        // Mock the tag retrieval
        Tag tag1 = new Tag();
        tag1.setId(1L);
        tag1.setName("Tag 1");
        Tag tag2 = new Tag();
        tag2.setId(2L);
        tag2.setName("Tag 2");

        when(tagService.findById(1L)).thenReturn(tag1);
        when(tagService.findById(2L)).thenReturn(tag2);

        // Create a sample CreateCourseDTO
        CreateCourseDTO courseDTO = new CreateCourseDTO();
        courseDTO.setTitle("Sample Course");
        courseDTO.setIcon("icon.png");
        courseDTO.setDescription("Sample course description");
        courseDTO.setTagIds(Arrays.asList(1L, 2L)); // Tag IDs to associate with the course

        // Mock the course creation
        Course createdCourse = new Course();
        createdCourse.setId(123L); // Assign a course_id to the created course

        when(courseService.createCourse(any(Course.class))).thenReturn(createdCourse);

        // Perform the POST request to create the course
        mockMvc.perform(MockMvcRequestBuilders.post("/courses")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(courseDTO)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(123)); // Verify the course_id
    }
}
