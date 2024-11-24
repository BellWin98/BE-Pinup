// 리팩토링 전 성공 코드 (PlaceSimpleDto)

package com.pinup.dto.response;

import lombok.*;

import java.util.List;

@Data
public class PlaceSimpleDto {

    private Long placeId;
    private String name;
    private Double averageStarRating;
    private Long reviewCount;
    private long distance;
    private List<String> reviewImageUrls;
    private List<String> reviewerProfileImageUrls;


    public PlaceSimpleDto(Long placeId, String name, Double averageStarRating,
                          Long reviewCount, Double distance) {
        this.placeId = placeId;
        this.name = name;
        this.averageStarRating = averageStarRating;
        this.reviewCount = reviewCount;
        this.distance = Math.round(distance * 1000);
    }
}