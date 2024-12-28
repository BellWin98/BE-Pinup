package com.pinup.dto.response;

import com.pinup.enums.MemberRelationType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProfileResponse {
    private MemberResponse member;
    private Integer reviewCount;
    private Integer friendCount;
    private Double averageRating;
    private MemberRelationType relationType;
}