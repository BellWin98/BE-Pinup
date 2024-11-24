package com.pinup.repository.querydsl;

import com.pinup.dto.response.PlaceDetailDto;
import com.pinup.dto.response.PlaceSimpleDto;
import com.pinup.entity.Member;
import com.pinup.entity.Place;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.NumberTemplate;
import com.querydsl.core.types.dsl.StringPath;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.JPQLQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

import static com.pinup.entity.QFriendShip.friendShip;
import static com.pinup.entity.QMember.member;
import static com.pinup.entity.QPlace.place;
import static com.pinup.entity.QReview.review;
import static com.pinup.entity.QReviewImage.reviewImage;

@RequiredArgsConstructor
public class PlaceRepositoryQueryDslImpl implements PlaceRepositoryQueryDsl{

    private final JPAQueryFactory queryFactory;

    @Override
    public Page<PlaceSimpleDto> findPlaceDtoPageOfFriendReviewByMember(
            Member loginMember,
            Double latitude,
            Double longitude,
            Pageable pageable
    ) {

        JPQLQuery<Long> friendMemberIds = JPAExpressions
                .select(friendShip.friend.id)
                .from(friendShip)
                .where(friendShip.member.id.eq(loginMember.getId()));

        List<PlaceSimpleDto> result = queryFactory
                .select(Projections.constructor(
                        PlaceSimpleDto.class,
                        place.id.as("placeId"),
                        place.name.as("name"),
                        review.starRating.avg().as("averageStarRating"),
                        review.id.countDistinct().as("reviewCount"),
                        calculateDistance(latitude, longitude, place.latitude, place.longitude).as("distance")
                ))
                .from(place)
                .join(review).on(place.eq(review.place))
                .join(member).on(review.member.eq(member))
                .where(
                        place.status.eq("Y"),
                        review.member.id.in(
                                JPAExpressions
                                        .select(member.id)
                                        .from(member)
                                        .where(member.id.eq(loginMember.getId())
                                                .or(member.id.in(friendMemberIds)))
                        )
                )
                .groupBy(place)
                .orderBy(calculateDistance(latitude, longitude, place.latitude, place.longitude).asc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        for (PlaceSimpleDto placeSimpleDto : result) {
            Long placeId = placeSimpleDto.getPlaceId();

            // 리뷰 이미지 URL 리스트 조회 (최대 3개, 먼저 등록된 순서로)
            List<String> reviewImageUrls = queryFactory
                    .select(reviewImage.url)
                    .from(reviewImage)
                    .join(review).on(reviewImage.review.eq(review))
                    .where(review.place.id.eq(placeId)
                            .and(review.member.id.eq(loginMember.getId())
                                    .or(review.member.id.in(friendMemberIds)))
                            .and(reviewImage.url.isNotNull()))
                    .orderBy(reviewImage.createdAt.asc())
                    .limit(3)
                    .fetch();

            // 리뷰 작성자 프로필 이미지 URL 리스트 조회 (최대 3개, 최근 등록된 순서로)
            List<String> reviewerProfileImageUrls = queryFactory
                    .select(member.profileImageUrl)
                    .from(member)
                    .join(review).on(member.eq(review.member))
                    .where(review.place.id.eq(placeId)
                            .and(review.member.id.eq(loginMember.getId())
                                    .or(review.member.id.in(friendMemberIds)))
                            .and(member.profileImageUrl.isNotNull()))
                    .orderBy(review.updatedAt.desc())
                    .limit(3)
                    .fetch();

            placeSimpleDto.setReviewImageUrls(reviewImageUrls);
            placeSimpleDto.setReviewerProfileImageUrls(reviewerProfileImageUrls);
        }

        Long total = queryFactory
                .select(place.id.count())
                .from(place)
                .join(review).on(place.eq(review.place))
                .where(
                        place.status.eq("Y"),
                        review.member.id.in(
                                JPAExpressions
                                        .select(member.id)
                                        .from(member)
                                        .where(member.id.eq(loginMember.getId())
                                                .or(member.id.in(friendMemberIds)))
                        )
                )
                .fetchOne();

        return new PageImpl<>(result, pageable, total != null ? total : 0L);
    }

//    @Override
//    public Optional<PlaceDetailDto> findPlaceDetailDtoOfFriendReviewByPlaceAndMember(Member loginMember, Place place) {
//
//        JPQLQuery<Long> friendMemberIds = JPAExpressions
//                .select(friendShip.friend.id)
//                .from(friendShip)
//                .where(friendShip.member.id.eq(loginMember.getId()));
//
//        queryFactory.select()
//    }

    private NumberTemplate<Double> calculateDistance(double latitude1, double longitude1, StringPath latitude2, StringPath longitude2) {
        return Expressions.numberTemplate(Double.class,
                "6371 * acos(cos(radians({0})) * cos(radians({1})) * cos(radians({2}) - radians({3})) + sin(radians({4})) * sin(radians({5})))",
                latitude1,
                Expressions.numberTemplate(Double.class, "CAST({0} AS DOUBLE)", latitude2),
                Expressions.numberTemplate(Double.class, "CAST({0} AS DOUBLE)", longitude2),
                longitude1,
                latitude1,
                Expressions.numberTemplate(Double.class, "CAST({0} AS DOUBLE)", latitude2)
        );
    }
}