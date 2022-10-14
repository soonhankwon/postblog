package com.assignment.postblog.controller;

import com.assignment.postblog.dto.CommentRequestDto;
import com.assignment.postblog.model.Comment;
import com.assignment.postblog.security.MemberDetailsImpl;
import com.assignment.postblog.service.CommentService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Validated
@RestController
public class CommentController {
    private final CommentService commentService;
    public CommentController (CommentService commentService) {
        this.commentService = commentService;
    }

    @PostMapping("/auth/comment")  //댓글 생성
    public Comment createComment(@RequestBody CommentRequestDto requestDto, @AuthenticationPrincipal MemberDetailsImpl userdetails) {
        //로그인 되어 있는 회원 테이블의 ID
        Long memberId = userdetails.getMember().getId();
        Comment comment = commentService.createComment(requestDto, memberId);
        //응답 보내기
        return comment;
    }
    @GetMapping("/comment/{id}") //로그인한 멤버가 등록한 댓글 조회
    public List<Comment> getComments(@AuthenticationPrincipal MemberDetailsImpl userDetails) {
        //로그인 되어 있는 회원 테이블의 ID
        Long memberId = userDetails.getMember().getId();
        return commentService.getComments(memberId);
    }
    @PutMapping("/auth/comment/{id}") //댓글 수정
    public Long updateComment (@PathVariable Long id, @RequestBody CommentRequestDto requestDto) {
        Comment comment = commentService.updateComment(id, requestDto);
        // 응답 보내기 (업데이트 된 댓글 id)
        return comment.getPostId();
    }
    @DeleteMapping("/auth/comment/{id}") //댓글 삭제
    public Long deleteComment(@PathVariable Long id) {
        Comment comment = commentService.deleteComment(id);
        return id;
    }
}
