package com.pinup.service;

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

@Service
@RequiredArgsConstructor
@Slf4j
public class ReviewService {

    private static final String FILE_TYPE = "reviews";

    private final MemberRepository memberRepository;
    private final PlaceRepository placeRepository;
    private final ReviewRepository reviewRepository;
    private final S3Service s3Service;

    @Transactional
    public ReviewResponse register(Long placeId,
                                   ReviewRequest reviewRequest,
                                   List<MultipartFile> images) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        Member findMember = memberRepository.findByEmail(authentication.getName()).orElseThrow(() -> PinUpException.MEMBER_NOT_FOUND);
        Place findPlace = placeRepository.findById(placeId).orElseThrow(() -> PinUpException.PLACE_NOT_FOUND);

        List<String> uploadedFileUrls = new ArrayList<>();
        List<String> inputKeywords = new ArrayList<>();

        Review newReview = reviewRequest.toEntity();

        newReview.attachMember(findMember);
        newReview.attachPlace(findPlace);

        // 등록된 리뷰 이미지가 있으면 S3에 저장 후 URL을 ReviewImage 엔티티에 저장
        if (images != null && !images.get(0).getOriginalFilename().isEmpty()) {
            for (MultipartFile multipartFile : images) {
                String uploadedFileUrl = s3Service.uploadFile(FILE_TYPE, multipartFile);
                ReviewImage reviewImage = new ReviewImage(uploadedFileUrl);
                reviewImage.attachReview(newReview);
                uploadedFileUrls.add(uploadedFileUrl);
            }
        }

        // 등록된 키워드가 있으면 Keyword 엔티티에 저장
        if (reviewRequest.getKeywords() != null) {
            if (!reviewRequest.getKeywords().isEmpty()) {
                for (String comment : reviewRequest.getKeywords()) {
                    Keyword keyword = new Keyword(comment);
                    keyword.attachReview(newReview);
                    inputKeywords.add(comment);
                }
            }
        }

        Review savedReview = reviewRepository.save(newReview);

        // 리뷰 저장 후 평균 평점 업데이트
        findPlace.updateAverageRating();
        placeRepository.save(findPlace);

        return ReviewResponse.of(savedReview, uploadedFileUrls, inputKeywords);
    }
}
