package com.pinup.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor  // 이 부분이 필요합니다!
@AllArgsConstructor
@Builder
public class FeedResponse {
    private MemberResponse member;
    private Integer reviewCount;
    private Double reviewRateAverage;
    private Integer friendCount;
    private List<ReviewResponse> reviews;
}