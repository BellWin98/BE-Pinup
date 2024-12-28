package com.pinup.repository;

import com.pinup.entity.Member;
import com.pinup.entity.Review;
import com.pinup.enums.ReviewType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {
    List<Review> findAllByMember(Member member);
    List<Review> findAllByType(ReviewType type);
    List<Review> findAllByMemberAndType(Member member, ReviewType type);
}
