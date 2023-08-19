package com.example.codelytic.course.model.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class CreateCourseDTO {
    private String title;
    private String icon;
    private String description;
    private List<Long> tagIds;
}
