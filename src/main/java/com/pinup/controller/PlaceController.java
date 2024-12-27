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
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
    @GetMapping("/list")
    @Operation(summary = "키워드 없이 장소 목록 조회 API", description = "현 위치 근방 친구 리뷰 있는 가게 목록만 조회")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "장소 목록 조회에 성공하였습니다.",
                    content = {
                            @Content(schema = @Schema(implementation = PlaceResponseWithFriendReview.class))
                    }
            )
    })
    public ResponseEntity<ResultResponse> getPlaces(
            @Schema(description = "SW 위도", example = "37.600720") @RequestParam(value = "swLatitude") double swLatitude,
            @Schema(description = "SW 경도", example = "127.013901") @RequestParam(value = "swLongitude") double swLongitude,
            @Schema(description = "NE 위도", example = "37.613230") @RequestParam(value = "neLatitude") double neLatitude,
            @Schema(description = "NE 경도", example = "127.030003") @RequestParam(value = "neLongitude") double neLongitude,
            @Schema(description = "현 위치 위도(집)", example = "37.607798") @RequestParam(value = "currentLatitude") double currentLatitude,
            @Schema(description = "현 위치 경도(집)", example = "127.025612") @RequestParam(value = "currentLongitude") double currentLongitude
    ) {

//        Page<PlaceResponseWithFriendReview> result = placeService.getPlacePage(latitude, longitude, pageable);
        List<PlaceResponseWithFriendReview> result = placeService.getPlaces(
                swLatitude, swLongitude, neLatitude, neLongitude, currentLatitude, currentLongitude
        );

        return ResponseEntity.ok(ResultResponse.of(GET_PLACES_SUCCESS, result));
    }

    @GetMapping("/{kakaoPlaceId}")
    @Operation(summary = "장소 상세 조회 API", description = "카카오맵에서 부여한 고유 ID로 장소 상세 조회")
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
            @Schema(description = "카카오맵 장소 고유 ID", example = "1997608947")
            @PathVariable("kakaoPlaceId") String kakaoPlaceId
    ) {

        PlaceDetailResponse result = placeService.getPlaceDetail(kakaoPlaceId);

        return ResponseEntity.ok(ResultResponse.of(GET_PLACE_DETAIL_SUCCESS, result));
    }

    @GetMapping("/list/keyword")
    @Operation(summary = "키워드로 장소 목록 조회 API", description = "리뷰 작성 시 장소 목록 조회할 때 사용 / 카카오맵 API 호출함")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "장소 목록 조회에 성공하였습니다.",
                    content = {
                            @Content(schema = @Schema(implementation = PlaceResponseByKeyword.class))
                    }
            )
    })
    public ResponseEntity<ResultResponse> getPlacesByKeyword(
            @Schema(description = "검색어", example = "하루카페") @RequestParam(value = "keyword") String keyword
    ) {

        List<PlaceResponseByKeyword> result = placeService.getPlacesByKeyword(keyword);
//        Page<PlaceResponseByKeyword> result = placeService.getPlacePageByKeyword(keyword, pageable);

        return ResponseEntity.ok(ResultResponse.of(GET_PLACES_SUCCESS, result));
    }

    @GetMapping("/list/category")
    @Operation(summary = "카테고리로 장소 목록 조회 API", description = "SW, NE 위도/경도 좌표 활용")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "장소 목록 조회에 성공하였습니다.",
                    content = {
                            @Content(schema = @Schema(implementation = PlaceResponseWithFriendReview.class))
                    }
            )
    })
    public ResponseEntity<ResultResponse> getPlacesByCategory(
            @Schema(description = "카테고리", example = "카페")@RequestParam(value = "category") String category,
            @Schema(description = "SW 위도", example = "37.600720") @RequestParam(value = "swLatitude") double swLatitude,
            @Schema(description = "SW 경도", example = "127.013901") @RequestParam(value = "swLongitude") double swLongitude,
            @Schema(description = "NE 위도", example = "37.613230") @RequestParam(value = "neLatitude") double neLatitude,
            @Schema(description = "NE 경도", example = "127.030003") @RequestParam(value = "neLongitude") double neLongitude,
            @Schema(description = "현 위치 위도(집)", example = "37.607798") @RequestParam(value = "currentLatitude") double currentLatitude,
            @Schema(description = "현 위치 경도(집)", example = "127.025612") @RequestParam(value = "currentLongitude") double currentLongitude
    ) {

        List<PlaceResponseWithFriendReview> result = placeService.getPlacesByCategory(
                category, swLatitude, swLongitude,
                neLatitude, neLongitude, currentLatitude, currentLongitude
        );

        return ResponseEntity.ok(ResultResponse.of(GET_PLACES_SUCCESS, result));
    }

//    public ResponseEntity<ResultResponse> getPlacePageByKeyword(
//            @Schema(description = "검색어", example = "맛집") @RequestParam(value = "keyword") String keyword,
//            @Schema(description = "위도", example = "37.56706784998933") @RequestParam(value = "latitude") String latitude,
//            @Schema(description = "경도", example = "126.82759102697081") @RequestParam(value = "longitude") String longitude,
//            @Schema(description = "반경", example = "20000") @RequestParam(value = "radius", defaultValue = "20000") int radius,
//            @Schema(description = "정렬조건(가까운순)", example = "distance") @RequestParam(value = "condition", defaultValue = "distance") String condition,
//            @PageableDefault Pageable pageable
//    ) {
//
//        Page<PlaceResponseByKeyword> result =
//                placeService.getPlacePageByKeyword(keyword, latitude, longitude, radius, condition, pageable);
//
//        return ResponseEntity.ok(ResultResponse.of(GET_PLACES_SUCCESS, result));
//    }
}
