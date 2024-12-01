package com.pinup.dto.response;

import com.pinup.entity.Member;
import com.pinup.entity.Place;
import com.pinup.entity.Review;
import lombok.Builder;
import lombok.Getter;

import java.util.List;
import java.util.Map;

@Getter
@Builder
public class PlaceDetailDto {

    private int reviewCount;
    private Double averageStarRating;
    private Map<String, Integer> ratingGraph;
    private List<ReviewDto> reviews;

    public static PlaceDetailDto from(Place place, Double averageStarRating) {
        return PlaceDetailDto.builder()
                .reviewCount(place.getReviews().size())
                .build();
    }

    @Getter
    @Builder
    static class ReviewDto {

        private String writerName; // 작성자 이름(또는 닉네임)
        private int writerTotalReviewCount; // 작성자의 총 리뷰 수
        private Double starRating; // 해당 가게에 부여한 별점
        private String visitedDate; // 방문날짜
        private String content; // 리뷰내용

        public static ReviewDto from(Review review) {
            return ReviewDto.builder()
                    .writerName(review.getMember().getName())
                    .writerTotalReviewCount(review.getMember().getReviews().size())
                    .starRating(review.getStarRating())
                    .visitedDate(review.getVisitedDate())
                    .content(review.getContent())
                    .build();
        }
    }
}
