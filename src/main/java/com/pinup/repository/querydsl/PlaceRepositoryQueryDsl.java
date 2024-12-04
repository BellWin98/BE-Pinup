package com.pinup.repository.querydsl;

import com.pinup.dto.response.PlaceDetailDto;
import com.pinup.dto.response.PlaceSimpleDto;
import com.pinup.entity.Member;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface PlaceRepositoryQueryDsl {

    Page<PlaceSimpleDto> findPlaceListByMemberAndCoordinate(
            Member loginMember,
            Double latitude,
            Double longitude,
            Pageable pageable
    );

    PlaceDetailDto findPlaceDetailByPlaceIdAndMember(
            Member loginMember,
            Long placeId
    );

    Long getReviewCount(Member loginMember, String kakaoMapId);
}
