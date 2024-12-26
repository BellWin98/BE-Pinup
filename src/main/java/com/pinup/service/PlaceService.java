package com.pinup.service;

import com.pinup.dto.response.PlaceDetailResponse;
import com.pinup.dto.response.PlaceResponseByKeyword;
import com.pinup.dto.response.PlaceResponseWithFriendReview;
import com.pinup.entity.Member;
import com.pinup.global.maps.KakaoMapModule;
import com.pinup.global.util.AuthUtil;
import com.pinup.repository.FriendShipRepository;
import com.pinup.repository.MemberRepository;
import com.pinup.repository.PlaceRepository;
import com.pinup.repository.ReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class PlaceService {

    private final MemberRepository memberRepository;
    private final AuthUtil authUtil;
    private final KakaoMapModule kakaoMapModule;
    private final PlaceRepository placeRepository;
    private final ReviewRepository reviewRepository;
    private final FriendShipRepository friendShipRepository;

    @Transactional
    public Page<PlaceResponseWithFriendReview> getPlacePage(double latitude, double longitude, Pageable pageable) {
        Member loginMember = authUtil.getLoginMember();
        return placeRepository.findPlaceListByMemberAndCoordinate(loginMember, latitude, longitude, pageable);
    }

    @Transactional
    public PlaceDetailResponse getPlaceDetail(String kakaoPlaceId) {

        Member loginMember = authUtil.getLoginMember();
        PlaceDetailResponse placeDetailResponse = placeRepository.findPlaceDetailByKakaoPlaceIdAndMember(loginMember, kakaoPlaceId);
        List<PlaceDetailResponse.ReviewDetailResponse> reviewDetailResponseList = placeDetailResponse.getReviews();
        Map<Integer, Integer> ratingGraph = new HashMap<>();

        for (PlaceDetailResponse.ReviewDetailResponse reviewDetailResponse : reviewDetailResponseList) {
            int range = (int) Math.floor(reviewDetailResponse.getStarRating());
            ratingGraph.put(range, ratingGraph.getOrDefault(range, 0) + 1);
        }
        placeDetailResponse.setRatingGraph(ratingGraph);

        return placeDetailResponse;
    }

    public Page<PlaceResponseByKeyword> getPlacePageByKeyword(String keyword, Pageable pageable) {
        Member loginMember = authUtil.getLoginMember();
        List<PlaceResponseByKeyword> placeInfoList = kakaoMapModule.search(loginMember, keyword);

        return new PageImpl<>(placeInfoList, pageable, placeInfoList.size());
    }

    public Page<PlaceResponseByKeyword> getPlacePageByKeyword(String keyword, String latitude, String longitude,
                                                              int radius, String sort, Pageable pageable) {
        Member loginMember = authUtil.getLoginMember();
        List<PlaceResponseByKeyword> placeInfoList =
                kakaoMapModule.search(loginMember, keyword, latitude, longitude, radius, sort);

        return new PageImpl<>(placeInfoList, pageable, placeInfoList.size());
    }

//    public Page<PlaceResponseWithFriendReview> getPlacePageByCategory(
//            String category,
//            double neLatitude,
//            double neLongitude,
//            double swLatitude,
//            double swLongitude,
//            Pageable pageable
//    ) {
//        Member loginMember = authUtil.getLoginMember();
//
//    }
}
