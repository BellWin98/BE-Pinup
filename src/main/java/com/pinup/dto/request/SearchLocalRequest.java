package com.pinup.dto.request;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

@Data
@NoArgsConstructor
public class SearchLocalRequest {

    private static final int DISPLAY = 5;  // 검색 결과 출력 건수 지정(1 ~ 5)
    private static final int START = 1;  // 검색 시작 위치로 1만 가능
    private static final String SORT = "random";  // 정렬 옵션: random(유사도순), comment(카페/블로그 리뷰 개수 순)

    private String query = "";
    private MultiValueMap<String, String> map = new LinkedMultiValueMap<>();

    public MultiValueMap<String, String> toMultiValueMap() {

        map.add("query", query);
        map.add("display", String.valueOf(DISPLAY));
        map.add("start", String.valueOf(START));
        map.add("sort", SORT);

        return map;

    }
}
