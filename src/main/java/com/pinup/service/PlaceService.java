package com.pinup.service;

import com.pinup.dto.response.PlaceResponse;
import com.pinup.entity.Member;
import com.pinup.enums.PlaceCategory;
import com.pinup.global.exception.PinUpException;
import com.pinup.global.maps.KakaoMapModule;
import com.pinup.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PlaceService {

    private final MemberRepository memberRepository;
    private final KakaoMapModule kakaoMapModule;

    public List<PlaceResponse> searchPlaces(String category, String query, String longitude,
                                            String latitude, int radius) {

        List<Map<String, Object>> placeInfoList = new ArrayList<>();
        Member currentMember = findMember();

        if (category != null && !category.isEmpty()) {

            // Enum 에서 category 와 일치하는 장소의 code 찾기
            for (PlaceCategory placeCategory : PlaceCategory.values()) {
                String placeType = placeCategory.name();
                if (placeType.equalsIgnoreCase(category)) {
                    placeInfoList = kakaoMapModule.searchPlaces(
                            currentMember, "category_group_code", placeCategory.getCode(), longitude, latitude, radius
                    );
                    break;
                }
            }
        }

        if (query != null && !query.isEmpty()) {
            placeInfoList = kakaoMapModule.searchPlaces(currentMember, "query", query, longitude, latitude, radius);
        }

        return placeInfoList.stream()
                .map(PlaceResponse::from)
                .collect(Collectors.toList());
    }

    private Member findMember() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        return memberRepository.findByEmail(email).orElseThrow(() -> PinUpException.MEMBER_NOT_FOUND);
    }
}
