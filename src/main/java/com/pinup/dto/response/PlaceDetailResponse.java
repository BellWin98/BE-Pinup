package com.pinup.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
@Schema(title = "장소 상세 조회 응답 DTO", description = "장소명, 리뷰 수, 평균별점, 리뷰 통계, reviews(리뷰 상세 데이터 리스트)")
public class PlaceDetailResponse {

    @Schema(description = "장소명")
    private String placeName;

    @Schema(description = "리뷰 수")
    private Long reviewCount;

    @Schema(description = "평균 별점")
    private Double averageStarRating;

    @Schema(description = "리뷰 통계 그래프")
    private Map<Integer, Integer> ratingGraph;

    @Schema(description = "리뷰 상세 정보 리스트")
    private List<ReviewDetailResponse> reviews;

    public PlaceDetailResponse(String placeName, Long reviewCount, Double averageStarRating) {

        if (averageStarRating != null) {
            averageStarRating = Math.round(averageStarRating * 10) / 10.0;
        } else {
            averageStarRating = 0.0;
        }

        this.placeName = placeName;
        this.reviewCount = reviewCount;
        this.averageStarRating = averageStarRating;
    }

    @Data
    @Schema(title = "리뷰 상세 조회 응답 DTO", description = "리뷰 ID, 작성자명, 작성자 등록 리뷰 수, 별점, 방문날짜, 리뷰내용, 작성자 프로필 사진, 리뷰 이미지 리스트")
    public static class ReviewDetailResponse {

        @Schema(description = "DB에 등록된 리뷰 고유 ID")
        private Long reviewId;

        @Schema(description = "리뷰 작성자명")
        private String writerName; // 작성자 이름(또는 닉네임)

        @Schema(description = "리뷰 작성자의 총 리뷰 수")
        private int writerTotalReviewCount; // 작성자의 총 리뷰 수

        @Schema(description = "리뷰 작성자가 해당 가게에 부여한 별점")
        private Double starRating; // 해당 가게에 부여한 별점

        @Schema(description = "장소 방문 날짜")
        private String visitedDate; // 방문날짜

        @Schema(description = "리뷰 내용")
        private String content; // 리뷰내용

        @Schema(description = "리뷰 작성자의 프로필 사진 URL")
        private String writerProfileImageUrl; // 작성자 프로필 사진

        @Schema(description = "리뷰 작성자가 등록한 리뷰 이미지 URL 리스트")
        private List<String> reviewImageUrls; // 리뷰 이미지 목록

        public ReviewDetailResponse(Long reviewId, String writerName, int writerTotalReviewCount,
                                    Double starRating, String visitedDate, String content, String writerProfileImageUrl) {

            this.reviewId = reviewId;
            this.writerName = writerName;
            this.writerTotalReviewCount = writerTotalReviewCount;
            this.starRating = starRating;
            this.visitedDate = visitedDate;
            this.content = content;
            this.writerProfileImageUrl = writerProfileImageUrl;
        }
    }
}
