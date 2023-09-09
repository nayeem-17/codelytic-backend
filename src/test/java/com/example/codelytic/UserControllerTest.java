package com.example.codelytic;

import static org.mockito.Mockito.when;

import java.util.Arrays;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.example.codelytic.auth.AuthenticationRequestDto;
import com.example.codelytic.course.CourseService;
import com.example.codelytic.course.model.dto.CreateCourseDTO;
import com.example.codelytic.tag.Tag;
import com.example.codelytic.tag.TagService;
import com.example.codelytic.user.UserService;
import com.example.codelytic.user.model.CreateUserDTO;
import com.example.codelytic.user.model.Role;
import com.fasterxml.jackson.databind.ObjectMapper;

@SpringBootTest
public class UserControllerTest {
        @Autowired
        private WebApplicationContext webApplicationContext;

        @Autowired
        private UserService userService;

        private MockMvc mockMvc;

        @Autowired
        private ObjectMapper objectMapper;

        @MockBean
        private CourseService courseService;

        @MockBean
        private TagService tagService;

        @Test
        public void testCreateUserAndAuthenticate() throws Exception {
                mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();

                // Create a user
                CreateUserDTO userDTO = new CreateUserDTO();
                userDTO.setName("Test User");
                userDTO.setEmail("string");
                userDTO.setRole(Role.USER);
                userDTO.setPassword("string");

                ResultActions createUserResult = mockMvc.perform(MockMvcRequestBuilders
                                .post("/user/register")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(new ObjectMapper().writeValueAsString(userDTO)))
                                // .andDo(print())
                                .andExpect(MockMvcResultMatchers.status().isOk());

                // Authenticate the user
                AuthenticationRequestDto authenticationRequest = new AuthenticationRequestDto();
                authenticationRequest.setUsername("string");
                authenticationRequest.setPassword("string");
                MvcResult authenticationResult = mockMvc.perform(MockMvcRequestBuilders.post("/authenticate")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(new ObjectMapper().writeValueAsString(authenticationRequest)))
                                .andExpect(MockMvcResultMatchers.status().isOk())
                                .andReturn();

                // Extract the token from the authentication response
                String token = new ObjectMapper().readTree(authenticationResult.getResponse().getContentAsString())
                                .get("token").asText();
                System.out.println(token);

                Authentication auth = new UsernamePasswordAuthenticationToken("string", "string");
                SecurityContextHolder.getContext().setAuthentication(auth);

                // Make a GET request to /user with the obtained token
                mockMvc.perform(MockMvcRequestBuilders.get("/user/")
                                .header("Authorization", "Bearer " + token)) // Set the token in the header
                                .andExpect(MockMvcResultMatchers.status().isOk())
                                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                                .andExpect(MockMvcResultMatchers.jsonPath("$.id").isNumber())
                                .andExpect(MockMvcResultMatchers.jsonPath("$.role").value("USER"));

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

                mockMvc.perform(MockMvcRequestBuilders.post("/course")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(new ObjectMapper().writeValueAsString(courseDTO))
                                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)) // Include the authorization
                                .andExpect(MockMvcResultMatchers.status().isOk());
                // fetch the course id
                Long courseId = objectMapper.readTree(authenticationResult.getResponse().getContentAsString())
                                .get("id").asLong();
                // now create a subsection
        }

        public void print(MvcResult result) throws Exception {
                System.out.println("Response Status: " + result.getResponse().getStatus());
                System.out.println("Response Body: " + result.getResponse().getContentAsString());
        }
}