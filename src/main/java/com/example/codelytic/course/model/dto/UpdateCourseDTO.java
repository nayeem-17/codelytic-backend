package com.example.codelytic.course.model.dto;

import java.util.List;

import lombok.Data;

@Data
public class UpdateCourseDTO {
    private Long id;
    private String author;
    private String title;
    private String icon;
    private boolean isPremium;
    private boolean isLive;
    private String description;
    private List<Long> tagIds;

}
