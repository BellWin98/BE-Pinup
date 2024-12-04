package com.pinup.controller;

import com.pinup.dto.response.PlaceDetailDto;
import com.pinup.dto.response.PlaceResponse;
import com.pinup.dto.response.PlaceSimpleDto;
import com.pinup.global.response.ApiSuccessResponse;
import com.pinup.global.response.ResultResponse;
import com.pinup.service.PlaceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

import static com.pinup.global.response.ResultCode.GET_PLACES_SUCCESS;
import static com.pinup.global.response.ResultCode.GET_PLACE_DETAIL_SUCCESS;

@RestController
@RequestMapping("/api/places")
public class PlaceController {

    private final PlaceService placeService;

    @Autowired
    public PlaceController(PlaceService placeService) {
        this.placeService = placeService;
    }

    @GetMapping("/search")
    public ResponseEntity<ApiSuccessResponse<List<PlaceResponse>>> searchPlaces(
            @RequestParam(value = "category", required = false) String category,
            @RequestParam(value = "query", required = false) String query,
            @RequestParam(value = "longitude") String longitude,
            @RequestParam(value = "latitude") String latitude,
            @RequestParam(value = "radius", defaultValue = "1000") int radius,
            @RequestParam(value = "sort", defaultValue = "distance") String sort
    ) {
        List<PlaceResponse> result = placeService.searchPlaces(category, query, longitude, latitude, radius, sort);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ApiSuccessResponse.from(result));
    }

    /**
     * DB에 저장된(리뷰가 있는) 가게 목록만 페이징으로 조회
     */
    @GetMapping
    public ResponseEntity<ResultResponse> getPlacePage(@RequestParam(value = "latitude") double latitude,
                                                       @RequestParam(value = "longitude") double longitude,
                                                       @PageableDefault Pageable pageable) {

        Page<PlaceSimpleDto> result = placeService.getPlacePage(latitude, longitude, pageable);

        return ResponseEntity.ok(ResultResponse.of(GET_PLACES_SUCCESS, result));
    }

    @GetMapping("/{placeId}")
    public ResponseEntity<ResultResponse> getPlaceDetail(@PathVariable("placeId") Long placeId) {

        PlaceDetailDto result = placeService.getPlaceDetail(placeId);

        return ResponseEntity.ok(ResultResponse.of(GET_PLACE_DETAIL_SUCCESS, result));
    }

    @GetMapping("/keyword")
    public ResponseEntity<ResultResponse> getPlacePageByKeyword(
            @RequestParam(value = "keyword") String keyword,
            @RequestParam(value = "latitude") String latitude,
            @RequestParam(value = "longitude") String longitude,
            @RequestParam(value = "radius", defaultValue = "1000") int radius,
            @RequestParam(value = "sort", defaultValue = "distance") String sort,
            @PageableDefault Pageable pageable
    ) {

        Page<Map<String, Object>> result =
                placeService.getPlacePageByKeyword(keyword, latitude, longitude, radius, sort, pageable);

        return ResponseEntity.ok(ResultResponse.of(GET_PLACES_SUCCESS, result));

    }
}
