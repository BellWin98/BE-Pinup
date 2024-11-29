package com.pinup.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReviewCountsResponse {
    private long oneStarCount;
    private long oneAndHalfStarCount;
    private long twoStarCount;
    private long twoAndHalfStarCount;
    private long threeStarCount;
    private long threeAndHalfStarCount;
    private long fourStarCount;
    private long fourAndHalfStarCount;
    private long fiveStarCount;
}