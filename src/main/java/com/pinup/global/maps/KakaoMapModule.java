package com.pinup.global.maps;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pinup.entity.Member;
import com.pinup.repository.PlaceRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
@Slf4j
public class KakaoMapModule {

    private static final String KAKAO_MAP_API_URI = "https://dapi.kakao.com/v2/local/search";
    private static final String CATEGORY_FORMAT = "/category.json";
    private static final String KEYWORD_FORMAT = "/keyword.json";
    private static final String HEADER_KEY = "Authorization";
    private static final String HEADER_VALUE = "KakaoAK ";

    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;
    private final PlaceRepository placeRepository;

    @Value("${kakao.key}")
    private String apiKey;

    public List<Map<String, Object>> search(Member loginMember, String keyword, String latitude,
                                            String longitude, int radius, String sort) {

        URI kakaoSearchUri = buildUri(keyword, latitude, longitude, radius, sort);

        return executeSearchRequest(kakaoSearchUri, loginMember);
    }

    private URI buildUri(String keyword, String latitude, String longitude, int radius, String sort) {

        UriComponentsBuilder uriBuilder = UriComponentsBuilder
                .fromUriString(KAKAO_MAP_API_URI)
                .path(KEYWORD_FORMAT)
                .queryParam("query", keyword)
                .queryParam("x", longitude)
                .queryParam("y", latitude)
                .queryParam("radius", radius)
                .queryParam("sort", sort);

        return uriBuilder.encode().build().toUri();
    }


    public List<Map<String, Object>> searchPlaces(Member currentMember, String key, String value,
                                                  String longitude, String latitude, int radius, String sort) {
        String path;
        if (key.equals("category_group_code")) {
            path = CATEGORY_FORMAT;
        } else {
            path = KEYWORD_FORMAT;
        }

        URI uri = buildPlaceSearchUri(path, key, value, longitude, latitude, radius, sort);

        return executeSearchRequest(uri, currentMember);

    }

    private List<Map<String, Object>> executeSearchRequest(URI uri, Member currentMember) {

        List<Map<String, Object>> placeInfoList = new ArrayList<>();

        try {
            RequestEntity<Void> apiRequest = RequestEntity
                    .get(uri)
                    .header(HEADER_KEY, HEADER_VALUE + apiKey)
                    .build();

            ResponseEntity<String> apiResponse = restTemplate.exchange(apiRequest, String.class);
            JsonNode jsonNode = objectMapper.readTree(apiResponse.getBody());
            JsonNode documentsNode = jsonNode.path("documents");

            for (JsonNode documentNode : documentsNode) {
                Map<String, Object> placeInfo = extractPlaceInfo(documentNode, currentMember);
                placeInfoList.add(placeInfo);
            }

        } catch (Exception e) {
            log.error("카카오맵 API 요청 간 에러 발생!", e);
        }

        return placeInfoList;
    }

    private Map<String, Object> extractPlaceInfo(JsonNode documentNode, Member currentMember) {

        Map<String, Object> placeInfo = new HashMap<>();

        String kakaoMapId = documentNode.path("id").asText();
        Long reviewCount = placeRepository.getReviewCount(currentMember, kakaoMapId);

        // PinUP DB에서 가져온 리뷰 데이터
//        PlaceReviewInfoDto placeReviewInfo = placeRepository.findPlaceReviewInfo(kakaoMapId, currentMember);

        placeInfo.put("kakaoMapId", kakaoMapId);
        placeInfo.put("name", documentNode.path("place_name").asText());
        placeInfo.put("category", documentNode.path("category_group_name").asText());
        // placeInfo.put("phone", documentNode.path("phone").asText());
        placeInfo.put("address", documentNode.path("address_name").asText());
         placeInfo.put("roadAddress", documentNode.path("road_address_name").asText());
         placeInfo.put("longitudeX", documentNode.path("x").asText());
         placeInfo.put("latitudeY", documentNode.path("y").asText());
         placeInfo.put("reviewCount", reviewCount);

//        placeInfo.put("distance", documentNode.path("distance").asText());
//        placeInfo.put("reviewData", placeReviewInfo);

        return placeInfo;
    }

    private URI buildPlaceSearchUri(String path, String key, String value,
                                    String longitude, String latitude, int radius, String sort) {

        UriComponentsBuilder uriBuilder = UriComponentsBuilder
                .fromUriString(KAKAO_MAP_API_URI)
                .path(path)
                .queryParam(key, value)
                .queryParam("x", longitude)
                .queryParam("y", latitude)
                .queryParam("radius", radius)
                .queryParam("sort", sort);

        return uriBuilder.encode().build().toUri();
    }
}
