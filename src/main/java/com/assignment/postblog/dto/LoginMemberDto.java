package com.assignment.postblog.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@Getter
@AllArgsConstructor
@ToString
public class LoginMemberDto {
    private String nickname;
    private String password;
}
