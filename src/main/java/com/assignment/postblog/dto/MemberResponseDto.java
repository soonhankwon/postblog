package com.assignment.postblog.dto;

import com.assignment.postblog.model.Member;
import com.assignment.postblog.model.MemberRoleEnum;
import lombok.Getter;

@Getter
public class MemberResponseDto {
    private Long id;
    private String nickname;
    private String password;
    private MemberRoleEnum role;

    public MemberResponseDto (Member responseDto) {
        this.id = responseDto.getId();
        this.nickname = responseDto.getNickname();
        this.password = responseDto.getPassword();
        this.role = responseDto.getRole();
    }

    public MemberResponseDto(int value, String detail, String s) {
    }
}
