package com.pinup.repository;

import com.pinup.entity.FriendShip;
import com.pinup.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FriendShipRepository extends JpaRepository<FriendShip, Long> {

    List<FriendShip> findAllByMember(Member member);

    Optional<FriendShip> findByMemberAndFriend(Member member, Member friend);
}
