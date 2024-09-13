package com.pinup.domain.member.entity;

import com.pinup.global.common.BaseTimeEntity;
import com.pinup.global.enums.LoginType;
import com.pinup.global.enums.Role;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(columnDefinition = "VARCHAR(100)", unique = true, nullable = false)
    private String email;

    @Column(nullable = false)
    private String name;

    @Column(columnDefinition = "VARCHAR(10)", unique = true)
    private String nickname;

    private String profileImageUrl;

    @Enumerated(EnumType.STRING)
    private Role role;

    @Column(columnDefinition = "VARCHAR(1)")
    private String status;

    @Enumerated(EnumType.STRING)
    private LoginType loginType;

    private String socialId; // 로그인 한 소셜 타입의 식별자 값

    @Builder
    public Member(String email, String name, String nickname,
                  String profileImageUrl, LoginType loginType, String socialId) {
        this.email = email;
        this.name = name;
        this.nickname = nickname;
        this.profileImageUrl = profileImageUrl;
        this.loginType = loginType;
        this.socialId = socialId;
        this.role = Role.ROLE_USER;
        this.status = "Y";
    }
}
