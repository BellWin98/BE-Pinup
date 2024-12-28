package com.pinup.controller;

import com.pinup.dto.request.PlaceRequest;
import com.pinup.dto.request.ReviewRequest;
import com.pinup.global.response.ResultCode;
import com.pinup.global.response.ResultResponse;
import com.pinup.service.ReviewService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/places")
@Tag(name = "리뷰 API", description = "리뷰 등록")
public class ReviewController {

    private final ReviewService reviewService;

    @Autowired
    public ReviewController(ReviewService reviewService) {
        this.reviewService = reviewService;
    }

    @PostMapping(value = "/reviews", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "리뷰 등록 API", description = "리뷰 등록 성공 시 리뷰 ID 반환")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201",
                    description = "리뷰 등록에 성공하였습니다."
            )
    })
    public ResponseEntity<ResultResponse> register(
            @Valid @RequestPart ReviewRequest reviewRequest,
            @Valid @RequestPart PlaceRequest placeRequest,
            @RequestPart(name = "multipartFiles", required = false) List<MultipartFile> multipartFiles
    ) {
        Long reviewId = reviewService.register(reviewRequest, placeRequest, multipartFiles);
        return ResponseEntity.ok(ResultResponse.of(ResultCode.CREATE_REVIEW_SUCCESS, reviewId));
    }
}
