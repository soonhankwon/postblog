package com.assignment.postblog.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@NoArgsConstructor
@Getter
public class SignupRequestDto {
    @NotBlank(message = "{member.nickname.notblank}")
    @Size(min = 4, max = 12, message = "{member.nickname.size}")
    @Pattern(regexp ="[a-z\\d]*${3,12}", message = "{member.nickname.pattern}")
    private String nickname;
    @NotBlank(message = "{member.password.notblank}")
    @Size(min = 8, max = 20, message = "{member.password.size}")
    @Pattern(regexp ="^(?=.*[A-Za-z])(?=.*\\d)(?=.*[~!@#$%^&*()+|=])[A-Za-z\\d~!@#$%^&*()+|=]{8,16}$",
            message = "{member.password.pattern}")
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
