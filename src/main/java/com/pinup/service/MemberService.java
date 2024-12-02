package com.pinup.service;

import com.pinup.cache.MemberCacheManager;
import com.pinup.dto.request.BioUpdateRequest;
import com.pinup.dto.request.NicknameUpdateRequest;
import com.pinup.dto.response.MemberResponse;
import com.pinup.dto.response.ProfileResponse;
import com.pinup.dto.response.ReviewCountsResponse;
import com.pinup.dto.response.ReviewResponse;
import com.pinup.entity.Member;
import com.pinup.entity.Review;
import com.pinup.entity.ReviewImage;
import com.pinup.global.exception.PinUpException;
import com.pinup.global.s3.S3Service;
import com.pinup.repository.MemberRepository;
import com.pinup.repository.ReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.stream.Collectors;

import static com.pinup.global.exception.PinUpException.ALREADY_EXIST_NICKNAME;
import static com.pinup.global.exception.PinUpException.NICKNAME_UPDATE_TIME_LIMIT;


@Service
@RequiredArgsConstructor
public class MemberService {

    private static final String PROFILE_IMAGE_DIRECTORY = "profiles";

    private final MemberRepository memberRepository;
    private final ReviewRepository reviewRepository;
    private final S3Service s3Service;
    private final MemberCacheManager memberCacheManager;

    @Transactional(readOnly = true)
    public MemberResponse searchUsers(String query) {
        Member member = memberRepository.findByNickname(query)
                .orElseThrow(() -> PinUpException.MEMBER_NOT_FOUND);

        return MemberResponse.from(member);
    }

    @Transactional(readOnly = true)
    public MemberResponse getCurrentMemberInfo() {
        Member currentMember = getCurrentMember();

        return MemberResponse.from(currentMember);
    }

    private Member getCurrentMember() {
        String currentMemberEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        return memberRepository.findByEmail(currentMemberEmail)
                .orElseThrow(() -> PinUpException.MEMBER_NOT_FOUND);
    }

    @Transactional
    public void deleteMember() {
        Member currentMember = getCurrentMember();
        memberCacheManager.evictAllCaches(currentMember.getId());
        memberRepository.delete(currentMember);
    }

    @Transactional(readOnly = true)
    public MemberResponse getMemberInfo(Long memberId) {
        return memberCacheManager.getMemberWithCache(memberId, () -> {
            Member member = memberRepository.findById(memberId)
                    .orElseThrow(() -> PinUpException.MEMBER_NOT_FOUND);
            return MemberResponse.from(member);
        });
    }

    @Transactional(readOnly = true)
    public boolean checkNicknameDuplicate(String nickname) {
        return memberRepository.findByNickname(nickname).isPresent();
    }

    @Transactional
    public MemberResponse updateBio(BioUpdateRequest request) {
        Member member = getCurrentMember();
        member.updateBio(request.getBio());
        memberRepository.save(member);

        memberCacheManager.evictAllCaches(member.getId());
        return MemberResponse.from(member);
    }

    @Transactional
    public MemberResponse updateNickname(NicknameUpdateRequest request) {
        Member member = getCurrentMember();
        validateNicknameUpdate(member, request.getNickname());

        member.updateNickname(request.getNickname());
        memberRepository.save(member);

        memberCacheManager.evictAllCaches(member.getId());
        return MemberResponse.from(member);
    }

    private void validateNicknameUpdate(Member member, String newNickname) {
        validateNicknameUpdateTimeLimit(member);
        validateNicknameDuplicate(newNickname);
    }

    private void validateNicknameUpdateTimeLimit(Member member) {
        if (!member.canUpdateNickname()) {
            throw NICKNAME_UPDATE_TIME_LIMIT;
        }
    }

    private void validateNicknameDuplicate(String nickname) {
        if (checkNicknameDuplicate(nickname)) {
            throw ALREADY_EXIST_NICKNAME;
        }
    }

    @Transactional
    public MemberResponse updateProfileImage(MultipartFile image) {
        Member member = getCurrentMember();

        if (member.getProfileImageUrl() != null) {
            s3Service.deleteFile(member.getProfileImageUrl());
        }

        String imageUrl = s3Service.uploadFile(PROFILE_IMAGE_DIRECTORY, image);
        member.updateProfileImage(imageUrl);
        memberRepository.save(member);

        memberCacheManager.evictAllCaches(member.getId());
        return MemberResponse.from(member);
    }

    @Transactional(readOnly = true)
    public ProfileResponse getMyProfile() {
        Member currentMember = getCurrentMember();
        return getProfile(currentMember.getId());
    }

    @Transactional(readOnly = true)
    public ProfileResponse getProfile(Long memberId) {
        return memberCacheManager.getProfileWithCache(memberId, () -> {
            Member member = memberRepository.findById(memberId)
                    .orElseThrow(() -> PinUpException.MEMBER_NOT_FOUND);
            return getProfileForMember(member);
        });
    }

    private ProfileResponse getProfileForMember(Member member) {
        List<Review> reviews = reviewRepository.findAllByMember(member);

        ReviewCountsResponse reviewCounts = ReviewCountsResponse.builder()
                .oneStarCount(getReviewCountForRating(reviews, 1.0))
                .oneAndHalfStarCount(getReviewCountForRating(reviews, 1.5))
                .twoStarCount(getReviewCountForRating(reviews, 2.0))
                .twoAndHalfStarCount(getReviewCountForRating(reviews, 2.5))
                .threeStarCount(getReviewCountForRating(reviews, 3.0))
                .threeAndHalfStarCount(getReviewCountForRating(reviews, 3.5))
                .fourStarCount(getReviewCountForRating(reviews, 4.0))
                .fourAndHalfStarCount(getReviewCountForRating(reviews, 4.5))
                .fiveStarCount(getReviewCountForRating(reviews, 5.0))
                .build();

        return ProfileResponse.builder()
                .member(MemberResponse.from(member))
                .reviewCount(reviews.size())
                .reviewCounts(reviewCounts)
                .friendCount(member.getFriendships().size())
                .reviews(reviews.stream()
                        .map(review -> ReviewResponse.of(
                                review,
                                review.getReviewImages().stream()
                                        .map(ReviewImage::getUrl)
                                        .collect(Collectors.toList())
                        ))
                        .collect(Collectors.toList()))
                .build();
    }

    private long getReviewCountForRating(List<Review> reviews, double rating) {
        return reviews.stream()
                .filter(review -> review.getStarRating() == rating)
                .count();
    }
}
