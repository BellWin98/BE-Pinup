package com.pinup.dto.response;

import com.pinup.entity.Place;
import lombok.*;

@Getter
@Builder
public class PlaceResponse {

//    private Long id;
    private String name; // 장소명
    private String address; // 주소
    private String roadAddress; // 도로명
    private String link;
    private String category;
    private String description;
    private String telephone;
    private String defaultImgUrl; // 기본 이미지
    private int latitude; // 위도
    private int longitude; // 경도
    private double averageRating; // 평균별점
//    private String status; // 상태
//    private String placeType;

    public static PlaceResponse from(Place place) {
        return PlaceResponse.builder()
//                .id(place.getId())
                .name(place.getName())
                .address(place.getAddress())
                .roadAddress(place.getRoadAddress())
                .defaultImgUrl(place.getDefaultImgUrl())
                .latitude(place.getLatitude())
                .longitude(place.getLongitude())
//                .status(place.getStatus())
//                .placeType(place.getPlaceType().name())
                .build();
    }
}
