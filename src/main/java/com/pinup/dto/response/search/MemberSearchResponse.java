package com.pinup.dto.response.search;

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
}
