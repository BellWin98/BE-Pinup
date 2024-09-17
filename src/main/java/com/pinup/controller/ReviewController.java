package com.pinup.controller;

import com.pinup.dto.request.ReviewRequest;
import com.pinup.dto.response.RegisterReviewResponse;
import com.pinup.global.response.ApiSuccessResponse;
import com.pinup.service.ReviewService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/places")
public class ReviewController {

    private final ReviewService reviewService;

    @Autowired
    public ReviewController(ReviewService reviewService) {
        this.reviewService = reviewService;
    }

    /**
     * 리뷰 등록 API
     * 내용, 평점, 사진 여러장, 키워드 여러개 등록 가능
     */
    @PostMapping("/{placeId}/reviews")
    public ResponseEntity<ApiSuccessResponse<RegisterReviewResponse>> register(
            @PathVariable("placeId") Long placeId,
            @Valid ReviewRequest reviewRequest,
            @RequestParam(value = "multipartFiles", required = false) List<MultipartFile> multipartFiles) {

        RegisterReviewResponse result = reviewService.register(placeId, reviewRequest, multipartFiles);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiSuccessResponse.from(result));
    }

    /**
     * 리뷰 조회 API
     * 가게에 내 친구들이 쓴 리뷰와 내가 쓴 리뷰만 조회
     */
}
