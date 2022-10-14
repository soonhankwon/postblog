package com.assignment.postblog.repository;

import com.assignment.postblog.model.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findAllById(Long postId);
    Comment deleteCommentById(Long id);
}
