package com.pinup.global.maps;

import com.pinup.dto.request.SearchImageRequest;
import com.pinup.dto.response.SearchImageResponse;
import com.pinup.dto.request.SearchLocalRequest;
import com.pinup.dto.response.SearchLocalResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

@Component
public class NaverClient {

    @Value("${naver.client.id}")
    private String clientId;

    @Value("${naver.client.secret}")
    private String secret;

    @Value("${naver.url.search.local}")
    private String searchUrl;

    @Value("${naver.url.search.image}")
    private String imageUrl;

    private URI uri;

    public SearchLocalResponse searchLocal(SearchLocalRequest request) {

        uri = UriComponentsBuilder
                .fromUriString(searchUrl)
                .queryParams(request.toMultiValueMap())
                .build()
                .encode()
                .toUri();

        HttpHeaders headers = new HttpHeaders();
        headers.set("X-Naver-Client-Id", clientId);
        headers.set("X-Naver-Client-Secret", secret);
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<?> httpEntity = new HttpEntity<>(headers);
        ParameterizedTypeReference<SearchLocalResponse> responseType = new ParameterizedTypeReference<>() {};

        ResponseEntity<SearchLocalResponse> responseEntity =
                new RestTemplate().exchange(uri, HttpMethod.GET, httpEntity, responseType);

        return responseEntity.getBody();
    }

    public SearchImageResponse searchImage(SearchImageRequest request) {

        uri = UriComponentsBuilder
                .fromUriString(imageUrl)
                .queryParams(request.toMultiValueMap())
                .build()
                .encode()
                .toUri();

        HttpHeaders headers = new HttpHeaders();
        headers.set("X-Naver-Client-Id", clientId);
        headers.set("X-Naver-Client-Secret", secret);
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<?> httpEntity = new HttpEntity<>(headers);
        ParameterizedTypeReference<SearchImageResponse> responseType = new ParameterizedTypeReference<>() {};

        ResponseEntity<SearchImageResponse> responseEntity =
                new RestTemplate().exchange(uri, HttpMethod.GET, httpEntity, responseType);

        return responseEntity.getBody();
    }

}
