package com.assignment.postblog.service;

import com.assignment.postblog.dto.PasswordDto;
import com.assignment.postblog.dto.PostRequestDto;
import com.assignment.postblog.dto.ResponseDto;
import com.assignment.postblog.model.Post;
import com.assignment.postblog.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;


@Service
@RequiredArgsConstructor
public class PostService {
    private final PostRepository postRepository;
    @Transactional
    public ResponseDto<?> createPost(PostRequestDto requestDto) {
        Post post = new Post(requestDto);
        postRepository.save(post);
        return ResponseDto.success(post);
    }

    @Transactional(readOnly = true)
    public ResponseDto<?> getPost(Long id) {
        Optional<Post> optionalPost = postRepository.findById(id);

        if (optionalPost.isEmpty()) {
            return ResponseDto.fail("NULL_POST_ID","포스트가 존재하지 않습니다");
        }
        return ResponseDto.success(optionalPost.get());
    }
    @Transactional(readOnly = true)
    public ResponseDto<?> getAllPost() {
        return ResponseDto.success(postRepository.findAll());
    }

    @Transactional
    public ResponseDto<Post> updatePost(Long id, PostRequestDto requestDto) {
        Optional<Post> optionalPost = postRepository.findById(id);

        if(optionalPost.isEmpty()) {
            return ResponseDto.fail("NULL_POST_ID", "포스트가 존재하지 않습니다");
        }
        Post post = optionalPost.get();
        post.update(requestDto);

        return ResponseDto.success(post);
    }

    @Transactional
    public ResponseDto<?> deletePost(Long id) {
        Optional<Post> optionalPost = postRepository.findById(id);
        if(optionalPost.isEmpty()) {
            return ResponseDto.fail("NOT_FOUND", "포스트가 존재하지 않습니다");
        }
        Post post = optionalPost.get();
        postRepository.delete(post);
        return ResponseDto.success(true);
    }

    @Transactional(readOnly = true)
    public ResponseDto<?> validateAuthorByPassword(Long id, PasswordDto password) {
        Optional<Post> optionalPost = postRepository.findById(id);

        if (optionalPost.isEmpty()) {
            return ResponseDto.fail("NOT_FOUND","포스트가 존재하지 않습니다");
        }
        Post post = optionalPost.get();
        if (!post.getPassword().equals(password.getPassword())) {
            return ResponseDto.fail("PASSWORD_NOT_CORRECT", "패스워드가 일치하지 않습니다");
        }
        return ResponseDto.success(true);
    }
}
