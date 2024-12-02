package com.pinup.dto.response;

import com.pinup.entity.Review;
import lombok.Builder;
import lombok.Getter;

import java.time.format.DateTimeFormatter;
import java.util.List;

@Getter
@Builder
public class ReviewResponse {

    private Long reviewId;
    private Long writerId;
    private Long placeId;
    private String content;
    private double starRating;
    private List<String> reviewImageUrls;
    private String createdAt;
    private String updatedAt;

    public static ReviewResponse of(Review review,
                                    List<String> reviewImageUrls) {

        return ReviewResponse.builder()
                .reviewId(review.getId())
                .writerId(review.getMember().getId())
                .placeId(review.getPlace().getId())
                .content(review.getContent())
                .starRating(review.getStarRating())
                .reviewImageUrls(reviewImageUrls)
                .createdAt(review.getCreatedAt().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")))
                .updatedAt(review.getUpdatedAt().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")))
                .build();
    }
}
