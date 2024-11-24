package com.pinup.repository.querydsl;

import com.pinup.dto.response.PlaceDetailDto;
import com.pinup.dto.response.PlaceSimpleDto;
import com.pinup.entity.Member;
import com.pinup.entity.Place;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface PlaceRepositoryQueryDsl {

    Page<PlaceSimpleDto> findPlaceDtoPageOfFriendReviewByMember(
            Member loginMember,
            Double latitude,
            Double longitude,
            Pageable pageable
    );

//    Optional<PlaceDetailDto> findPlaceDetailDtoOfFriendReviewByPlaceAndMember(
//            Member loginMember,
//            Place place
//    );


}
