package com.pinup.repository;

import com.pinup.domain.friend.FriendRequest;
import com.pinup.entity.Member;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FriendRequestRepository extends JpaRepository<FriendRequest, Long> {

    Optional<FriendRequest> findById(Long id);

    List<FriendRequest> findBySenderAndReceiver(Member sender, Member receiver);

    Page<FriendRequest> findBySender(Member sender, Pageable pageable);

    Page<FriendRequest> findByReceiver(Member receiver, Pageable pageable);
}
