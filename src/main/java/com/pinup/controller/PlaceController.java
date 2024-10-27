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

    @GetMapping("/search")
    public ResponseEntity<ApiSuccessResponse<List<PlaceResponse>>> searchPlaces(
            @RequestParam(value = "category", required = false) String category,
            @RequestParam(value = "query", required = false) String query,
            @RequestParam(value = "longitude") String longitude,
            @RequestParam(value = "latitude") String latitude,
            @RequestParam(value = "radius", defaultValue = "20000") int radius
    ) {
       List<PlaceResponse> result = placeService.searchPlaces(category, query, longitude, latitude, radius);

       return ResponseEntity
               .status(HttpStatus.OK)
               .body(ApiSuccessResponse.from(result));
    }
}
