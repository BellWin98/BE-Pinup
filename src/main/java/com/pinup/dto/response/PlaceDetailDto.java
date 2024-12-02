package com.pinup.dto.response;

import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class PlaceDetailDto {

    private String placeName;
    private Long reviewCount;
    private Double averageStarRating;
    private Map<Integer, Integer> ratingGraph;
    private List<ReviewDto> reviews;

    public PlaceDetailDto(String placeName, Long reviewCount, Double averageStarRating) {
        this.placeName = placeName;
        this.reviewCount = reviewCount;
        this.averageStarRating = averageStarRating;
    }

    @Data
    public static class ReviewDto {

        private Long reviewId;
        private String writerName; // 작성자 이름(또는 닉네임)
        private int writerTotalReviewCount; // 작성자의 총 리뷰 수
        private Double starRating; // 해당 가게에 부여한 별점
        private String visitedDate; // 방문날짜
        private String content; // 리뷰내용
        private String writerProfileImageUrl; // 작성자 프로필 사진
        private List<String> reviewImageUrls; // 리뷰 이미지 목록

        public ReviewDto(Long reviewId, String writerName, int writerTotalReviewCount,
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
