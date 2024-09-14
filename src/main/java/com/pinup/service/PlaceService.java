package com.pinup.service;

import com.pinup.dto.response.PlaceResponse;
import com.pinup.global.maps.NaverClient;
import com.pinup.dto.request.SearchImageRequest;
import com.pinup.dto.response.SearchImageResponse;
import com.pinup.dto.request.SearchLocalRequest;
import com.pinup.dto.response.SearchLocalResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PlaceService {

    private final NaverClient naverClient;

    public List<PlaceResponse> search(String query) {

        // 지역 검색
        SearchLocalRequest searchLocalRequest = new SearchLocalRequest();
        searchLocalRequest.setQuery(query);

        SearchLocalResponse searchLocalResponse = naverClient.searchLocal(searchLocalRequest);

        List<PlaceResponse> placeResponses = new ArrayList<>();

        List<SearchLocalResponse.SearchLocalItem> items = searchLocalResponse.getItems();
        for (SearchLocalResponse.SearchLocalItem localItem : items) {
            String name;
            if (localItem.getTitle().contains("<b>") || localItem.getTitle().contains("</b>")) {
                name = localItem.getTitle().replaceAll("</?b>","");
            } else {
                name = localItem.getTitle();
            }

            SearchImageRequest searchImageRequest = new SearchImageRequest();
            searchImageRequest.setQuery(name);

            SearchImageResponse imageItem = naverClient.searchImage(searchImageRequest);
            PlaceResponse placeResponse = PlaceResponse.builder()
                    .name(name)
                    .address(localItem.getAddress())
                    .roadAddress(localItem.getRoadAddress())
                    .category(localItem.getCategory())
                    .description(localItem.getDescription())
                    .link(localItem.getLink())
                    .telephone(localItem.getTelephone())
                    .defaultImgUrl(imageItem.getItems().stream().findFirst().get().getThumbnail())
                    .latitude(localItem.getMapx())
                    .longitude(localItem.getMapy())
                    .averageRating(0.0)
                    .build();

            placeResponses.add(placeResponse);
        }

        return placeResponses;
    }
}
