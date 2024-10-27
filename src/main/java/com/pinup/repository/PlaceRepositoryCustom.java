package com.pinup.repository;

import com.pinup.dto.response.PlaceReviewInfoDto;
import com.pinup.entity.Member;

public interface PlaceRepositoryCustom {

    PlaceReviewInfoDto findPlaceReviewInfo(String kakaoMapId, Member currentMember);
}
