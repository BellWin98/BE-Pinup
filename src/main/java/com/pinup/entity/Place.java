package com.pinup.entity;

import com.pinup.global.common.BaseTimeEntity;
import com.pinup.global.enums.PlaceType;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Place extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name; // 장소명
    private String address; // 주소
    private String roadAddress; // 도로명
    private String defaultImgUrl; // 기본 이미지

    private int latitude; // 위도
    private int longitude; // 경도
    private double averageRating; // 평균별점
    private String status; // 상태

    @Enumerated(EnumType.STRING)
    private PlaceType placeType;

    @Builder
    public Place(String name, String address, String roadAddress,
                 int latitude, int longitude, PlaceType placeType) {
        this.name = name;
        this.address = address;
        this.roadAddress = roadAddress;
        this.latitude = latitude;
        this.longitude = longitude;
        this.status = "Y";
        this.placeType = placeType;
    }

    public void updateDefaultImgUrl(String defaultImgUrl) {
        this.defaultImgUrl = defaultImgUrl;
    }

    public void updateAverageRating(double averageRating){
        this.averageRating = averageRating;
    }
}
