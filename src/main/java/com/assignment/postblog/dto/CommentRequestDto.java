package com.assignment.postblog.dto;

import com.assignment.postblog.model.Comment;
import lombok.Getter;

@Getter
public class CommentRequestDto {
    private Long postId;
    private String content;

    public CommentRequestDto (Comment comment) {
        this.postId = comment.getPostId();
        this.content = comment.getContent();
    }
}
