package com.pinup.service;

import com.pinup.dto.request.PlaceRequest;
import com.pinup.dto.request.ReviewRequest;
import com.pinup.dto.response.ReviewTempResponse;
import com.pinup.entity.Member;
import com.pinup.entity.Place;
import com.pinup.entity.Review;
import com.pinup.entity.ReviewImage;
import com.pinup.exception.ImagesLimitExceededException;
import com.pinup.exception.MemberNotFoundException;
import com.pinup.global.s3.S3Service;
import com.pinup.global.util.AuthUtil;
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
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class ReviewService {

    private static final String FILE_TYPE = "reviews";
    private static final int IMAGES_LIMIT = 3;

    private final MemberRepository memberRepository;
    private final AuthUtil authUtil;
    private final PlaceRepository placeRepository;
    private final ReviewRepository reviewRepository;
    private final S3Service s3Service;

    @Transactional
    public Long register(ReviewRequest reviewRequest, PlaceRequest placeRequest, List<MultipartFile> images) {

        Member loginMember = authUtil.getLoginMember();
        Place place = findOrCreatePlace(placeRequest);
        List<String> uploadedFileUrls = uploadImages(images);
        Review newReview = createReview(reviewRequest, loginMember, place, uploadedFileUrls);
        Review savedReview = reviewRepository.save(newReview);

        return savedReview.getId();
    }

    @Transactional(readOnly = true)
    public List<ReviewTempResponse> getCurrentUserReviews() {
        Member findMember = findMember();
        return getReviews(findMember.getId());
    }

    @Transactional(readOnly = true)
    public List<ReviewTempResponse> getReviews(Long memberId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(MemberNotFoundException::new);

        return reviewRepository.findAllByMember(member).stream()
                .map(review -> ReviewTempResponse.of(
                        review,
                        review.getReviewImages().stream()
                                .map(ReviewImage::getUrl)
                                .collect(Collectors.toList())
                ))
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public Double getCurrentUserAverageRating() {
        Member findMember = findMember();

        return getMemberAverageRating(findMember().getId());
    }

    @Transactional(readOnly = true)
    public Integer getCurrentUserReviewsCount() {
        Member findMember = findMember();

        return getReviews(findMember.getId()).size();
    }

    @Transactional(readOnly = true)
    public Integer getUserReviewsCount(Long memberId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(MemberNotFoundException::new);

        return getReviews(member.getId()).size();
    }

    @Transactional(readOnly = true)
    public Double getMemberAverageRating(Long memberId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(MemberNotFoundException::new);

        return reviewRepository.findAllByMember(member).stream()
                .mapToDouble(Review::getStarRating)
                .average()
                .orElse(0.0);
    }


    /**
     * SecurityContextHolder 에서 현재 로그인 한 사용자 이메일 Get 후 회원 정보 탐색
     */
    private Member findMember() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        return memberRepository.findByEmail(authentication.getName()).orElseThrow(MemberNotFoundException::new);
    }

    /**
     * 카카오맵 ID로 DB에 등록된 업체 정보 조회
     * DB에 업체 미등록 시, 업체 신규 생성
     */
    private Place findOrCreatePlace(PlaceRequest placeRequest) {
        String kakaoPlaceId = placeRequest.getKakaoPlaceId();

        return placeRepository.findByKakaoMapId(kakaoPlaceId)
                .orElseGet(() -> placeRepository.save(placeRequest.toEntity()));
    }

    /**
     * S3에 이미지 업로드
     * 이미지 최대 업로드 가능 갯수: 3장
     */
    private List<String> uploadImages(List<MultipartFile> images) {

        if (images == null || images.isEmpty()) {
            return Collections.emptyList();
        }

        if (images.size() > IMAGES_LIMIT) {
            throw new ImagesLimitExceededException();
        }

        List<String> uploadedFileUrls = new ArrayList<>();

        for (MultipartFile multipartFile : images) {
            String uploadedFileUrl = s3Service.uploadFile(FILE_TYPE, multipartFile);
            uploadedFileUrls.add(uploadedFileUrl);
        }

        return uploadedFileUrls;
    }

    /**
     * 생성된 리뷰 엔티티에 작성자, 업체, 리뷰 이미지, 키워드 연결
     */
    private Review createReview(
            ReviewRequest reviewRequest,
            Member writer,
            Place place,
            List<String> uploadedFileUrls
    ) {

        Review newReview = reviewRequest.toEntity();
        newReview.attachMember(writer);
        newReview.attachPlace(place);

        // 리뷰 이미지와 키워드를 리뷰 엔티티에 연결
        for (String fileUrl : uploadedFileUrls) {
            ReviewImage reviewImage = new ReviewImage(fileUrl);
            reviewImage.attachReview(newReview);
        }

        return newReview;
    }
}
