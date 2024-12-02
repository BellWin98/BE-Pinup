package com.pinup.dto.request;

import com.pinup.entity.Review;
import jakarta.validation.constraints.*;
import lombok.Data;

import java.util.List;

@Data
public class ReviewRequest {

    @NotBlank(message = "리뷰 후기는 필수 입력값입니다.")
    @Size(min = 5, max = 400, message = "리뷰 글자 수 범위를 벗어납니다.")
    private String content;

    @NotNull(message = "별점은 필수 입력값입니다,")
    @Min(value = 1, message = "최소 별점은 1입니다.")
    @Max(value = 5, message = "최대 별점은 5입니다.")
    private Double starRating;

    @NotBlank(message = "방문 날짜를 입력해주세요.")
    private String visitedDate;

    public Review toEntity() {
        return Review.builder()
                .content(content)
                .starRating(starRating)
                .visitedDate(visitedDate)
                .build();
    }
}
