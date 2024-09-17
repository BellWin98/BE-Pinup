package com.pinup.dto.request;

import com.pinup.entity.Review;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

@Data
public class ReviewRequest {

    @NotBlank(message = "리뷰 후기는 필수 입력값입니다.")
    private String comment;

    @NotNull(message = "평점은 필수 입력값입니다,")
    @Min(value = 1, message = "최소 평점은 1입니다.")
    @Max(value = 5, message = "최대 평점은 5입니다.")
    private Double rating;

    private List<String> keywords;

    public Review toEntity() {
        return new Review(comment, rating);
    }
}
