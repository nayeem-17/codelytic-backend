package com.example.codelytic.post;

import java.util.List;

import lombok.Data;

@Data
public class CreatePostDTO {
    private String title;
    private String content;
    private List<Long> tagIds;
}
