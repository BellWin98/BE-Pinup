package com.pinup.service;

import com.pinup.cache.RedisFeedCache;
import com.pinup.dto.request.BioUpdateRequest;
import com.pinup.dto.request.NicknameUpdateRequest;
import com.pinup.dto.response.FeedResponse;
import com.pinup.dto.response.MemberResponse;
import com.pinup.dto.response.ReviewResponse;
import com.pinup.entity.Keyword;
import com.pinup.entity.Member;
import com.pinup.entity.Review;
import com.pinup.entity.ReviewImage;
import com.pinup.global.s3.S3Service;
import com.pinup.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.stream.Collectors;

import static com.pinup.global.exception.PinUpException.*;

@Service
@RequiredArgsConstructor
public class FeedService {
    private static final String PROFILE_IMAGE_DIRECTORY = "profiles";

    private final MemberRepository memberRepository;
    private final S3Service s3Service;
    private final MemberCacheService memberCacheService;
    private final RedisFeedCache redisFeedCache;

    @Transactional
    public boolean checkNicknameDuplicate(String nickname) {
        return memberCacheService.isNicknameDuplicate(nickname);
    }

    @Transactional
    public MemberResponse updateBio(BioUpdateRequest request) {
        Member member = getCurrentMember();
        member.updateBio(request.getBio());
        memberRepository.save(member);
        redisFeedCache.invalidateCache(member.getId());
        return MemberResponse.from(member);
    }

    @Transactional
    public MemberResponse updateNickname(NicknameUpdateRequest request) {
        Member member = getCurrentMember();
        validateNicknameUpdate(member, request.getNickname());

        member.updateNickname(request.getNickname());
        memberCacheService.cacheNickname(member.getEmail(), request.getNickname());
        redisFeedCache.invalidateCache(member.getId());

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
        if (memberCacheService.isNicknameDuplicate(nickname)) {
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
        redisFeedCache.invalidateCache(member.getId());

        return MemberResponse.from(member);
    }

    @Transactional(readOnly = true)
    public FeedResponse getFeed(Long memberId) {
        return redisFeedCache.getOrCreateFeed(memberId, this::buildFeedResponse);
    }

    @Transactional(readOnly = true)
    public FeedResponse getMyFeed() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        Member member = memberRepository.findByEmail(email)
                .orElseThrow(() -> MEMBER_NOT_FOUND);
        return redisFeedCache.getOrCreateFeed(member.getId(), this::buildFeedResponse);
    }

    private Member getCurrentMember() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        return memberRepository.findByEmail(email)
                .orElseThrow(() -> MEMBER_NOT_FOUND);
    }

    @Transactional(readOnly = true)
    public FeedResponse buildFeedResponse(Long memberId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> MEMBER_NOT_FOUND);

        List<Review> reviews = member.getReviews();

        double avgRating = reviews.stream()
                .mapToDouble(Review::getRating)
                .average()
                .orElse(0.0);

        return FeedResponse.builder()
                .member(MemberResponse.from(member))
                .reviewCount(reviews.size())
                .reviewRateAverage(avgRating)
                .friendCount(member.getFriendships().size())
                .reviews(reviews.stream()
                        .map(review -> ReviewResponse.of(
                                review,
                                review.getReviewImages().stream()
                                        .map(ReviewImage::getUrl)
                                        .collect(Collectors.toList()),
                                review.getKeywords().stream()
                                        .map(Keyword::getKeyword)
                                        .collect(Collectors.toList())
                        ))
                        .collect(Collectors.toList()))
                .build();
    }
}