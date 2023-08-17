package com.example.codelytic.comment;

import lombok.Data;

@Data

public class CreateCommentDTO {
    String content;
    Long parentCommentId;
    Long postId;
}
