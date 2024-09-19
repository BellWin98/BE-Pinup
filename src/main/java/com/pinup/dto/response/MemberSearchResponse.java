package com.pinup.dto.response;

import com.pinup.entity.Member;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MemberSearchResponse {
    private Long memberId;
    private String email;
    private String name;
    private String nickname;
    private String profilePictureUrl;

    public static MemberSearchResponse from(Member member) {
        return MemberSearchResponse.builder()
                .memberId(member.getId())
                .email(member.getEmail())
                .name(member.getName())
                .profilePictureUrl(member.getProfileImageUrl())
                .nickname(member.getNickname())
                .build();
    }
}
