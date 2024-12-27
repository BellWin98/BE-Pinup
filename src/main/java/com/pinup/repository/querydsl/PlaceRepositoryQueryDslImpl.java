package com.pinup.repository.querydsl;

import com.pinup.dto.response.PlaceDetailResponse;
import com.pinup.dto.response.PlaceResponseWithFriendReview;
import com.pinup.entity.Member;
import com.pinup.enums.PlaceCategory;
import com.pinup.exception.PlaceNotFoundException;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.NumberPath;
import com.querydsl.core.types.dsl.NumberTemplate;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.JPQLQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.List;

import static com.pinup.entity.QFriendShip.friendShip;
import static com.pinup.entity.QMember.member;
import static com.pinup.entity.QPlace.place;
import static com.pinup.entity.QReview.review;
import static com.pinup.entity.QReviewImage.reviewImage;

@RequiredArgsConstructor
@Slf4j
public class PlaceRepositoryQueryDslImpl implements PlaceRepositoryQueryDsl{

    private final JPAQueryFactory queryFactory;

    @Override
    public List<PlaceResponseWithFriendReview> findAllByMemberAndLocation(
            Member loginMember, double swLatitude, double swLongitude,
            double neLatitude, double neLongitude, double currentLatitude, double currentLongitude
    ) {
        List<PlaceResponseWithFriendReview> result = fetchPlaces(
                loginMember, swLatitude, swLongitude,
                neLatitude, neLongitude, currentLatitude,
                currentLongitude, null
        );

        addImageInfoOnPlaceResult(result, loginMember);

        return result;
    }

    @Override
    public List<PlaceResponseWithFriendReview> findAllByMemberAndCategoryAndLocation(
            Member loginMember, PlaceCategory placeCategory, double swLatitude,
            double swLongitude, double neLatitude, double neLongitude,
            double currentLatitude, double currentLongitude
    ) {

        List<PlaceResponseWithFriendReview> result = fetchPlaces(
                loginMember, swLatitude, swLongitude,
                neLatitude, neLongitude, currentLatitude,
                currentLongitude, placeCategory
        );

        addImageInfoOnPlaceResult(result, loginMember);

        return result;
    }

    @Override
    public PlaceDetailResponse findByKakaoPlaceIdAndMember(Member loginMember, String kakaoPlaceId) {
        List<PlaceDetailResponse.ReviewDetailResponse> reviewDetailList = queryFactory
                .select(Projections.constructor(
                        PlaceDetailResponse.ReviewDetailResponse.class,
                        review.id.as("reviewId"),
                        review.member.name.as("writerName"),
                        review.member.reviews.size().as("writerTotalReviewCount"),
                        review.starRating.as("starRating"),
                        review.visitedDate.as("visitedDate"),
                        review.content.as("content"),
                        review.member.profileImageUrl.as("writerProfileImageUrl")
                ))
                .from(review)
                .where(review.place.kakaoMapId.eq(kakaoPlaceId)
                        .and(review.member.id.eq(loginMember.getId())
                                .or(review.member.id.in(getFriendMemberIds(loginMember.getId())))
                        )
                )
                .orderBy(review.createdAt.desc())
                .fetch();

        for (PlaceDetailResponse.ReviewDetailResponse reviewDetailResponse : reviewDetailList) {

            Long reviewId = reviewDetailResponse.getReviewId();

            List<String> reviewImageUrls = queryFactory
                    .select(reviewImage.url)
                    .from(reviewImage)
                    .where(reviewImage.review.id.eq(reviewId))
                    .fetch();

            reviewDetailResponse.setReviewImageUrls(reviewImageUrls);
        }

        PlaceDetailResponse placeDetailResponse = queryFactory
                .select(Projections.constructor(
                                PlaceDetailResponse.class,
                                place.name.as("placeName"),
                                review.countDistinct().as("reviewCount"),
                                review.starRating.avg().as("averageStarRating")
                        )
                )
                .from(place)
                .join(review).on(place.eq(review.place))
                .where(place.kakaoMapId.eq(kakaoPlaceId)
                        .and(review.member.id.eq(loginMember.getId())
                                .or(review.member.id.in(getFriendMemberIds(loginMember.getId())))
                        )
                )
                .fetchOne();

        if (placeDetailResponse != null) {
            placeDetailResponse.setReviews(reviewDetailList);
        } else {
            throw new PlaceNotFoundException();
        }

        return placeDetailResponse;
    }

    @Override
    public Long getReviewCount(Member loginMember, String kakaoMapId) {
        return queryFactory
                .select(review.count())
                .from(review)
                .where(review.place.kakaoMapId.eq(kakaoMapId)
                        .and(review.member.id.eq(loginMember.getId())
                                .or(review.member.id.in(getFriendMemberIds(loginMember.getId())))
                        )
                )
                .fetchOne();
    }

    @Override
    public Double getAverageStarRating(Member loginMember, String kakaoMapId) {
        return queryFactory
                .select(review.starRating.avg())
                .from(place)
                .join(review).on(place.eq(review.place))
                .where(place.status.eq("Y")
                        .and(place.kakaoMapId.eq(kakaoMapId))
                        .and(review.member.id.eq(loginMember.getId())
                                .or(review.member.id.in(getFriendMemberIds(loginMember.getId())))
                        )

                )
                .fetchOne();
    }

    private List<PlaceResponseWithFriendReview> fetchPlaces(
            Member loginMember, double swLatitude, double swLongitude,
            double neLatitude, double neLongitude, double currentLatitude,
            double currentLongitude, PlaceCategory placeCategory
    ) {

        var query = queryFactory
                .select(Projections.constructor(
                        PlaceResponseWithFriendReview.class,
                        place.id.as("placeId"),
                        place.kakaoMapId.as("kakaoPlaceId"),
                        place.name.as("name"),
                        review.starRating.avg().as("averageStarRating"),
                        review.id.countDistinct().as("reviewCount"),
                        calculateDistance(currentLatitude, currentLongitude, place.latitude, place.longitude).as("distance")
                ))
                .from(place)
                .join(review).on(place.eq(review.place))
                .where(place.status.eq("Y")
                        .and(review.member.id.eq(loginMember.getId())
                                .or(review.member.id.in(getFriendMemberIds(loginMember.getId()))))
                        .and(place.latitude.between(swLatitude, neLatitude))
                        .and(place.longitude.between(swLongitude, neLongitude)));

        if (placeCategory != null) {
            query.where(place.placeCategory.eq(placeCategory));
        }

        return query
                .groupBy(place)
                .orderBy(calculateDistance(currentLatitude, currentLongitude, place.latitude, place.longitude).asc())
                .fetch();

    }

    private void addImageInfoOnPlaceResult(List<PlaceResponseWithFriendReview> result, Member loginMember) {
        for (PlaceResponseWithFriendReview response : result) {
            String kakaoPlaceId = response.getKakaoPlaceId();
            response.setReviewImageUrls(fetchReviewImageUrls(kakaoPlaceId, loginMember.getId()));
            response.setReviewerProfileImageUrls(fetchReviewerProfileImageUrls(kakaoPlaceId, loginMember.getId()));
        }
    }

    private List<String> fetchReviewImageUrls(String kakaoPlaceId, Long loginMemberId) {
        return queryFactory
                .select(reviewImage.url)
                .from(reviewImage)
                .join(review).on(reviewImage.review.eq(review))
                .where(review.place.kakaoMapId.eq(kakaoPlaceId)
                        .and(review.member.id.eq(loginMemberId)
                                .or(review.member.id.in(getFriendMemberIds(loginMemberId))))
                        .and(reviewImage.url.isNotNull()))
                .orderBy(reviewImage.createdAt.asc())
                .limit(3)
                .fetch();
    }

    private List<String> fetchReviewerProfileImageUrls(String kakaoPlaceId, Long loginMemberId) {
        return queryFactory
                .select(member.profileImageUrl)
                .from(member)
                .join(review).on(member.eq(review.member))
                .where(review.place.kakaoMapId.eq(kakaoPlaceId)
                        .and(review.member.id.eq(loginMemberId)
                                .or(review.member.id.in(getFriendMemberIds(loginMemberId)))))
                .orderBy(review.updatedAt.desc())
                .limit(3)
                .fetch();
    }

    private NumberTemplate<Double> calculateDistance(
            double latitude1, double longitude1, NumberPath<Double> latitude2, NumberPath<Double> longitude2
    ) {
        return Expressions.numberTemplate(Double.class,
                "6371 * acos(cos(radians({0})) * cos(radians({1})) * cos(radians({2}) - radians({3})) + sin(radians({4})) * sin(radians({5})))",
                latitude1, latitude2, longitude2, longitude1, latitude1, latitude2
        );
    }

    private JPQLQuery<Long> getFriendMemberIds(Long loginMemberId) {
        return JPAExpressions
                .select(friendShip.friend.id)
                .from(friendShip)
                .where(friendShip.member.id.eq(loginMemberId));
    }

}