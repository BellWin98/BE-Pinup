package com.pinup.controller;

import com.pinup.dto.response.PlaceDetailResponse;
import com.pinup.dto.response.PlaceResponseByKeyword;
import com.pinup.dto.response.PlaceResponseWithFriendReview;
import com.pinup.global.response.ResultResponse;
import com.pinup.service.PlaceService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static com.pinup.global.response.ResultCode.GET_PLACES_SUCCESS;
import static com.pinup.global.response.ResultCode.GET_PLACE_DETAIL_SUCCESS;

@RestController
@RequestMapping("/api/places")
@Tag(name = "장소 API", description = "키워드 없이 장소 목록 조회, 키워드로 장소 목록 조회, 장소 상세 조회")
public class PlaceController {

    private final PlaceService placeService;

    @Autowired
    public PlaceController(PlaceService placeService) {
        this.placeService = placeService;
    }

    /**
     * DB에 저장된(리뷰가 있는) 가게 목록만 페이징으로 조회
     */
    @GetMapping
    @Operation(summary = "키워드 없이 장소 목록 조회 API", description = "리뷰 있는(DB에 등록된) 가게 목록만 조회")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "장소 목록 조회에 성공하였습니다.",
                    content = {
                            @Content(schema = @Schema(implementation = PlaceResponseWithFriendReview.class))
                    }
            )
    })
    public ResponseEntity<ResultResponse> getPlacePage(
            @Schema(description = "위도", example = "37.56706784998933") @RequestParam(value = "latitude") double latitude,
            @Schema(description = "경도", example = "126.82759102697081") @RequestParam(value = "longitude") double longitude,
            @PageableDefault Pageable pageable
    ) {

        Page<PlaceResponseWithFriendReview> result = placeService.getPlacePage(latitude, longitude, pageable);

        return ResponseEntity.ok(ResultResponse.of(GET_PLACES_SUCCESS, result));
    }

    @GetMapping("/{placeId}")
    @Operation(summary = "장소 상세 조회 API", description = "장소 상세 조회")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "장소 상세 조회에 성공하였습니다.",
                    content = {
                            @Content(schema = @Schema(implementation = PlaceDetailResponse.class))
                    }
            )
    })
    public ResponseEntity<ResultResponse> getPlaceDetail(
            @Schema(description = "DB에 등록된 장소 고유 ID", example = "1")
            @PathVariable("placeId") Long placeId
    ) {

        PlaceDetailResponse result = placeService.getPlaceDetail(placeId);

        return ResponseEntity.ok(ResultResponse.of(GET_PLACE_DETAIL_SUCCESS, result));
    }

    @GetMapping("/keyword")
    @Operation(summary = "키워드로 장소 목록 조회 API", description = "카카오맵 API 사용")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "장소 목록 조회에 성공하였습니다.",
                    content = {
                            @Content(schema = @Schema(implementation = PlaceResponseByKeyword.class))
                    }
            )
    })
    public ResponseEntity<ResultResponse> getPlacePageByKeyword(
            @Schema(description = "검색어", example = "맛집") @RequestParam(value = "keyword") String keyword,
            @Schema(description = "위도", example = "37.56706784998933") @RequestParam(value = "latitude") String latitude,
            @Schema(description = "경도", example = "126.82759102697081") @RequestParam(value = "longitude") String longitude,
            @Schema(description = "반경", example = "20000") @RequestParam(value = "radius", defaultValue = "20000") int radius,
            @Schema(description = "정렬조건(가까운순)", example = "distance") @RequestParam(value = "condition", defaultValue = "distance") String condition,
            @PageableDefault Pageable pageable
    ) {

        Page<PlaceResponseByKeyword> result =
                placeService.getPlacePageByKeyword(keyword, latitude, longitude, radius, condition, pageable);

        return ResponseEntity.ok(ResultResponse.of(GET_PLACES_SUCCESS, result));
    }
}
