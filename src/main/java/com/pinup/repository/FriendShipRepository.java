package com.pinup.repository;

import com.pinup.entity.FriendShip;
import com.pinup.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FriendShipRepository extends JpaRepository<FriendShip, Long> {

    List<FriendShip> findAllByMember(Member member);
}
