package com.pinup.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.util.List;

@Data
@Schema(title = "키워드 없이 장소 목록 조회 응답 DTO", description = "친구 리뷰가 있는 장소 목록 데이터만 조회")
public class PlaceResponseWithFriendReview {

    @Schema(description = "DB에 저장된 장소 고유 ID")
    private Long placeId;

    @Schema(description = "장소명")
    private String name;

    @Schema(description = "평균 별점")
    private Double averageStarRating;

    @Schema(description = "리뷰 수")
    private Long reviewCount;

    @Schema(description = "현재 위치에서 해당 장소까지 떨어진 거리(단위: km)")
    private long distance;

    @Schema(description = "리뷰 이미지 URL 리스트 (가장 먼저 등록된 리뷰 이미지 순서로 최대 3장)")
    private List<String> reviewImageUrls;

    @Schema(description = "리뷰 작성자 프로필 이미지 URL 리스트 (가장 최근에 리뷰 작성한 유저 순서로 최대 3장)")
    private List<String> reviewerProfileImageUrls;

    public PlaceResponseWithFriendReview(Long placeId, String name, Double averageStarRating,
                                         Long reviewCount, Double distance) {
        this.placeId = placeId;
        this.name = name;
        this.averageStarRating = averageStarRating;
        this.reviewCount = reviewCount;
        this.distance = Math.round(distance * 1000);
    }
}