package com.assignment.postblog.dto;

import com.assignment.postblog.model.Member;
import lombok.Getter;

@Getter
public class MemberResponseDto {
    private Long id;
    private String nickname;
    private String password;

    public MemberResponseDto (Member responseDto) {
        this.id = responseDto.getId();
        this.nickname = responseDto.getNickname();
        this.password = responseDto.getPassword();
    }
}
