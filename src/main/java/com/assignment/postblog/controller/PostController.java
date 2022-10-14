package com.assignment.postblog.controller;

import com.assignment.postblog.dto.PasswordDto;
import com.assignment.postblog.dto.PostRequestDto;
import com.assignment.postblog.dto.ResponseDto;
import com.assignment.postblog.repository.PostRepository;
import com.assignment.postblog.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api")
public class PostController {
    private final PostRepository postRepository;
    private final PostService postService;

    @PostMapping("/post") //게시글 작성
    public ResponseDto<?> createPost(@RequestBody PostRequestDto requestDto) {
        return postService.createPost(requestDto);
    }

    @GetMapping("/post") //게시글 조회
    public ResponseDto<?> getAllPosts() {
        return postService.getAllPost();
    }

    @GetMapping("/post/{id}") //게시글 상세 조회
    public ResponseDto<?> getPost(@PathVariable Long id) {
        return postService.getPost(id);
    }

    @PutMapping("/post/{id}") //게시글 수정
    public ResponseDto<?> updatePost(@PathVariable Long id, @RequestBody PostRequestDto postRequestDto) {
        return postService.updatePost(id, postRequestDto);
    }

    @DeleteMapping("/post/{id}") //게시글 삭제
    public ResponseDto<?> deletePost(@PathVariable Long id) {
        return postService.deletePost(id);
    }

    @PostMapping("/post/{id}")
    public ResponseDto<?> validateAuthorByPassword(@PathVariable Long id, @RequestBody PasswordDto password) {
        return postService.validateAuthorByPassword(id, password);
    }
}
