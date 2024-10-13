package com.pinup.service;

import com.pinup.dto.request.PlaceRequest;
import com.pinup.dto.request.ReviewRequest;
import com.pinup.dto.response.ReviewResponse;
import com.pinup.entity.*;
import com.pinup.global.exception.PinUpException;
import com.pinup.global.s3.S3Service;
import com.pinup.repository.MemberRepository;
import com.pinup.repository.PlaceRepository;
import com.pinup.repository.ReviewRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class ReviewService {

    private static final String FILE_TYPE = "reviews";
    private static final int IMAGES_LIMIT = 3;
    private static final int KEYWORDS_LIMIT = 10;
    private static final int KEYWORDS_LENGTH_LIMIT = 10;

    private final MemberRepository memberRepository;
    private final PlaceRepository placeRepository;
    private final ReviewRepository reviewRepository;
    private final S3Service s3Service;

    @Transactional
    public ReviewResponse register(ReviewRequest reviewRequest,
                                   PlaceRequest placeRequest,
                                   List<MultipartFile> images) {

        List<String> uploadedFileUrls = new ArrayList<>();
        List<String> inputKeywords = new ArrayList<>();

        // 유저 존재 여부 확인
        Member findMember = findMember();

        // 업체 카카오맵 ID
        String kakaoPlaceId = placeRequest.getKakaoPlaceId();

        /**
         * DB에 업체 존재 여부 확인
         * 업체 존재 시: 카카오맵 ID로 등록된 업체 조회
         * 업체 미존재 시: DB에 해당 업체 등록
         */
        Optional<Place> findPlaceByKakaoMapId = placeRepository.findByKakaoMapId(kakaoPlaceId);
        Place place;
        if (findPlaceByKakaoMapId.isEmpty()) {
            place = placeRequest.toEntity();
        } else {
            place = placeRepository.findByKakaoMapId(kakaoPlaceId)
                    .orElseThrow(() -> PinUpException.PLACE_NOT_FOUND);
        }

        Place savedPlace = placeRepository.save(place);

        Review newReview = reviewRequest.toEntity();

        newReview.attachMember(findMember);
        newReview.attachPlace(savedPlace);

        // 등록된 리뷰 이미지가 있으면 S3에 저장 후 URL을 ReviewImage 엔티티에 저장
        if (images != null && !images.get(0).getOriginalFilename().isEmpty()) {
            if (images.size() > IMAGES_LIMIT) {
                throw PinUpException.IMAGES_LIMIT_EXCEEDED;
            }

            for (MultipartFile multipartFile : images) {
                String uploadedFileUrl = s3Service.uploadFile(FILE_TYPE, multipartFile);
                ReviewImage reviewImage = new ReviewImage(uploadedFileUrl);
                reviewImage.attachReview(newReview);
                uploadedFileUrls.add(uploadedFileUrl);
            }
        }

        // 등록된 키워드가 있으면 Keyword 엔티티에 저장
        if (reviewRequest.getKeywords() != null && !reviewRequest.getKeywords().isEmpty()) {

            if (reviewRequest.getKeywords().size() > KEYWORDS_LIMIT) {
                throw PinUpException.KEYWORDS_LIMIT_EXCEEDED;
            }

            for (String comment : reviewRequest.getKeywords()) {
                if (comment.length() > KEYWORDS_LENGTH_LIMIT) {
                    throw PinUpException.KEYWORDS_LENGTH_LIMIT_EXCEEDED;
                }
                Keyword keyword = new Keyword(comment);
                keyword.attachReview(newReview);
                inputKeywords.add(comment);
            }
        }

        Review savedReview = reviewRepository.save(newReview);

        return ReviewResponse.of(savedReview, uploadedFileUrls, inputKeywords);
    }

    private Member findMember() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        return memberRepository.findByEmail(authentication.getName())
                .orElseThrow(() -> PinUpException.MEMBER_NOT_FOUND);
    }
}
