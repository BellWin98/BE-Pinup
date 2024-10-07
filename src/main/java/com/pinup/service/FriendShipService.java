package com.pinup.service;

import com.pinup.dto.response.MemberResponse;
import com.pinup.entity.FriendShip;
import com.pinup.entity.Member;
import com.pinup.repository.FriendShipRepository;
import com.pinup.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

import static com.pinup.global.exception.PinUpException.*;

@RequiredArgsConstructor
@Service
public class FriendShipService {

    private final FriendShipRepository friendShipRepository;
    private final MemberRepository memberRepository;

    @Transactional(readOnly = true)
    public List<MemberResponse> getAllFriends(Long memberId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> MEMBER_NOT_FOUND);

        return friendShipRepository.findAllByMember(member)
                .stream()
                .map(friendShip -> MemberResponse.from(friendShip.getFriend()))
                .collect(Collectors.toList());
    }

    @Transactional
    public void createFriendShip(Member member, Member friend) {
        FriendShip friendShip1 = FriendShip.builder()
                .member(member)
                .friend(friend)
                .build();
        friendShipRepository.save(friendShip1);

        FriendShip friendShip2 = FriendShip.builder()
                .member(friend)
                .friend(member)
                .build();
        friendShipRepository.save(friendShip2);
    }

    @Transactional
    public void removeFriend(Long friendId) {
        String currentUserEmail = SecurityContextHolder.getContext().getAuthentication().getName();

        Member currentUser = memberRepository.findByEmail(currentUserEmail)
                .orElseThrow(() -> MEMBER_NOT_FOUND);
        Member friend = memberRepository.findById(friendId)
                .orElseThrow(() -> MEMBER_NOT_FOUND);

        FriendShip friendship1 = friendShipRepository.findByMemberAndFriend(currentUser, friend)
                .orElseThrow(() -> FRIENDSHIP_NOT_FOUND);
        friendShipRepository.delete(friendship1);

        FriendShip friendship2 = friendShipRepository.findByMemberAndFriend(friend, currentUser)
                .orElseThrow(() -> FRIENDSHIP_NOT_FOUND);
        friendShipRepository.delete(friendship2);
    }
}