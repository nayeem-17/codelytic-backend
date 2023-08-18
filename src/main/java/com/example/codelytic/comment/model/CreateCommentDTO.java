package com.example.codelytic.comment.model;

import lombok.Data;

@Data

public class CreateCommentDTO {
    String content;
    Long parentCommentId;
    Long postId;
}
