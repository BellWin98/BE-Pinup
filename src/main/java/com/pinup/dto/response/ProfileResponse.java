package com.pinup.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProfileResponse {
    private MemberResponse member;
    private Integer reviewCount;
    private ReviewCountsResponse reviewCounts;
    private Integer friendCount;
    private List<ReviewTempResponse> reviews;
}
