package com.assignment.postblog.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@NoArgsConstructor
@Getter
public class SignupRequestDto {
    @NotBlank(message = "닉네임을 입력해주세요")
    @Pattern(regexp ="^[a-zA-Z0-9]{4,12}$", message = "닉네임을 4~12자로 입력해주세요")
    private String nickname;
    @NotBlank(message = "비밀번호를 입력해주세요")
    @Pattern(regexp ="^[a-zA-Z0-9]{4,32}$", message = "비밀번호 4~32자로 입력해주세요")
    private String password;
    @NotBlank(message = "비밀번호 확인을 입력해주세요")
    private String passwordConfirm;
    private boolean admin = false;

    public SignupRequestDto (String nickname, String password, String passwordConfirm) {
        this.nickname = nickname;
        this.password = password;
        this.passwordConfirm = passwordConfirm;
    }
    public void setEncodePwd(String encodePwd) {
        this.password = encodePwd;
    }
}
