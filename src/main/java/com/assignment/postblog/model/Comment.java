package com.assignment.postblog.model;

import com.assignment.postblog.dto.CommentRequestDto;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import javax.persistence.*;

@Setter
@Getter
@NoArgsConstructor
@Entity(name = "comments")
public class Comment {
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Id
    private Long id;
    @Column(nullable = false)
    private Long postId;
    @Column(nullable = false)
    private String content;

    //댓글을 등록한 회원 Id저장
    public Comment (CommentRequestDto requestDto, Long postId) {
        this.postId = postId;
        this.content = requestDto.getContent();
    }


//    public void update(CommentRequestDto requestDto) {
//        this.postId = requestDto.getPostId();
//        this.content = requestDto.getContent();
//    }
}
