package com.pinup.controller;

import com.pinup.dto.response.PlaceResponse;
import com.pinup.service.PlaceService;
import com.pinup.global.response.ApiSuccessResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/places")
public class PlaceController {

    private final PlaceService placeService;

    @Autowired
    public PlaceController(PlaceService placeService) {
        this.placeService = placeService;
    }

    /**
     * 네이버 지도에 등록된 장소의 세부 정보를 검색한다.
     * @author 한종승
     */
    @GetMapping("/search")
    public ResponseEntity<ApiSuccessResponse<List<PlaceResponse>>> search(@RequestParam("query") String query) {

        List<PlaceResponse> result = placeService.search(query);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ApiSuccessResponse
                .from(result));
    }
}
