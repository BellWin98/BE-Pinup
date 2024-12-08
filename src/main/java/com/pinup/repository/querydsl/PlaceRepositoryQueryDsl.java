package com.pinup.repository.querydsl;

import com.pinup.dto.response.PlaceDetailResponse;
import com.pinup.dto.response.PlaceResponseWithFriendReview;
import com.pinup.entity.Member;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface PlaceRepositoryQueryDsl {

    Page<PlaceResponseWithFriendReview> findPlaceListByMemberAndCoordinate(
            Member loginMember,
            Double latitude,
            Double longitude,
            Pageable pageable
    );

    PlaceDetailResponse findPlaceDetailByPlaceIdAndMember(
            Member loginMember,
            Long placeId
    );

    Long getReviewCount(Member loginMember, String kakaoMapId);
}
