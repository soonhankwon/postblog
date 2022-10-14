package com.assignment.postblog.service;

import com.assignment.postblog.dto.CommentInfoResponseDto;
import com.assignment.postblog.dto.CommentRequestDto;
import com.assignment.postblog.dto.DBEmptyDataException;
import com.assignment.postblog.model.Comment;
import com.assignment.postblog.repository.CommentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CommentService {
    private final CommentRepository commentRepository;

    @Autowired
    public CommentService(CommentRepository commentRepository) {
        this.commentRepository = commentRepository;
    }
    public Comment createComment(CommentRequestDto requestDto, Long postId) {
        //요청받은 DTO로 DB db에 저장할 객체 만들기
        Comment comment = new Comment(requestDto, postId);
        commentRepository.save(comment);
        return comment;
    }
    public CommentInfoResponseDto findComment(Long id) {
        Comment findComment = commentRepository.findById(id).orElseThrow(() -> {
            throw new DBEmptyDataException("댓글 조회 실패");
        });
        CommentInfoResponseDto commentInfoResponseDto = new CommentInfoResponseDto(findComment);
        return commentInfoResponseDto;
    }
    public Comment updateComment(Long id, CommentRequestDto requestDto) {
        Comment comment = commentRepository.findById(id).orElseThrow(() -> new NullPointerException("댓글이 존재하지 않습니다."));
        String content = requestDto.getContent();
        comment.setContent(content);
        commentRepository.save(comment);

        return comment;
    }
    //회원 ID로 등록된 댓글 조회
    public List<Comment> getComments(Long postId) {
        return commentRepository.findAllById(postId);
    }
    //회원 포스트 ID로 등록된 댓글 삭제
    public Comment deleteComment(Long id) {
        return commentRepository.deleteCommentById(id);
    }
}
