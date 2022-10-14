package com.assignment.postblog.model;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;

@Getter
@Entity
@NoArgsConstructor
public class RefreshToken extends Timestamped {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotBlank
    private String refreshToken;
    @NotBlank
    private String memberNickname;

    public RefreshToken(String token, String nickname) {
        this.refreshToken = token;
        this.memberNickname = nickname;
    }
    public RefreshToken updateToken(String token) {
        this.refreshToken = token;
        return this;
    }
}
