package com.pinup.service;

import com.pinup.dto.response.PlaceDetailDto;
import com.pinup.dto.response.PlaceResponse;
import com.pinup.dto.response.PlaceSimpleDto;
import com.pinup.entity.Member;
import com.pinup.enums.PlaceCategory;
import com.pinup.global.exception.PinUpException;
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
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PlaceService {

    private final MemberRepository memberRepository;
    private final AuthUtil authUtil;
    private final KakaoMapModule kakaoMapModule;
    private final PlaceRepository placeRepository;
    private final ReviewRepository reviewRepository;
    private final FriendShipRepository friendShipRepository;

    public List<PlaceResponse> searchPlaces(String category, String query, String longitude,
                                            String latitude, int radius, String sort) {

        List<Map<String, Object>> placeInfoList = new ArrayList<>();
        Member currentMember = findMember();

        if (category != null && !category.isEmpty()) {

            // Enum 에서 category 와 일치하는 장소의 code 찾기
            for (PlaceCategory placeCategory : PlaceCategory.values()) {
                String placeType = placeCategory.name();
                if (placeType.equalsIgnoreCase(category)) {
                    placeInfoList = kakaoMapModule
                            .searchPlaces(currentMember, "category_group_code", placeCategory.getCode(),
                                    longitude, latitude, radius, sort);
                    break;
                }
            }
        }

        if (query != null && !query.isEmpty()) {
            placeInfoList = kakaoMapModule
                    .searchPlaces(currentMember, "query", query, longitude, latitude, radius, sort);
        }

        return placeInfoList.stream()
                .map(PlaceResponse::from)
                .collect(Collectors.toList());
    }

    @Transactional
    public Page<PlaceSimpleDto> getPlacePage(double latitude, double longitude, Pageable pageable) {

        Member loginMember = authUtil.getLoginMember();

        return placeRepository.findPlaceListByMemberAndCoordinate(loginMember, latitude, longitude, pageable);
    }

    @Transactional
    public PlaceDetailDto getPlaceDetail(Long placeId) {

        Member loginMember = authUtil.getLoginMember();

        PlaceDetailDto placeDetailDto = placeRepository.findPlaceDetailByPlaceIdAndMember(loginMember, placeId);

        List<PlaceDetailDto.ReviewDto> reviewDtoList = placeDetailDto.getReviews();

        Map<Integer, Integer> ratingGraph = new HashMap<>();

        for (PlaceDetailDto.ReviewDto reviewDto : reviewDtoList) {

            int range = (int) Math.floor(reviewDto.getStarRating());
            ratingGraph.put(range, ratingGraph.getOrDefault(range, 0) + 1);

        }

        placeDetailDto.setRatingGraph(ratingGraph);

        return placeDetailDto;
    }

    public Page<Map<String, Object>> getPlacePageByKeyword(String keyword, String latitude, String longitude,
                                                           int radius, String sort, Pageable pageable) {

        Member loginMember = authUtil.getLoginMember();

        List<Map<String, Object>> placeInfoList =
                kakaoMapModule.search(loginMember, keyword, latitude, longitude, radius, sort);

        return new PageImpl<>(placeInfoList, pageable, placeInfoList.size());
    }

    private Member findMember() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        return memberRepository.findByEmail(email).orElseThrow(() -> PinUpException.MEMBER_NOT_FOUND);
    }
}
