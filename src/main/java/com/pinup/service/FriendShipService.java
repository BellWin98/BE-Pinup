package com.pinup.service;

import com.pinup.dto.response.MemberResponse;
import com.pinup.entity.FriendShip;
import com.pinup.entity.Member;
import com.pinup.exception.FriendNotFoundException;
import com.pinup.exception.MemberNotFoundException;
import com.pinup.repository.FriendShipRepository;
import com.pinup.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
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
                .orElseThrow(MemberNotFoundException::new);

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
        Member currentUser = getCurrentMember();
        Member friend = memberRepository.findById(friendId)
                .orElseThrow(FriendNotFoundException::new);

        FriendShip friendship1 = friendShipRepository.findByMemberAndFriend(currentUser, friend)
                .orElseThrow(FriendNotFoundException::new);
        friendShipRepository.delete(friendship1);

        FriendShip friendship2 = friendShipRepository.findByMemberAndFriend(friend, currentUser)
                .orElseThrow(FriendNotFoundException::new);
        friendShipRepository.delete(friendship2);
    }

    @Transactional(readOnly = true)
    public List<MemberResponse> getAllFriends() {
        Member currentUser = getCurrentMember();

        List<MemberResponse> friendList = friendShipRepository.findAllByMember(currentUser)
                .stream()
                .map(friendShip -> MemberResponse.from(friendShip.getFriend()))
                .collect(Collectors.toList());
        friendList.sort(Comparator.comparing(MemberResponse::getNickname));

        return friendList;
    }

    @Transactional(readOnly = true)
    public MemberResponse searchMyFriendInfoByNickname(String query) {
        Member member = getCurrentMember();

        return friendShipRepository.findAllByMember(member)
                .stream()
                .filter(friendShip -> friendShip.getFriend().getNickname().equals(query))
                .map(FriendShip::getFriend)
                .map(MemberResponse::from)
                .findFirst()
                .orElseThrow(FriendNotFoundException::new);
    }

    private Member getCurrentMember() {
        String currentUserEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        return memberRepository.findByEmail(currentUserEmail)
                .orElseThrow(MemberNotFoundException::new);
    }

    public boolean existsFriendship(Member sender, Member receiver) {
        return friendShipRepository.findByMemberAndFriend(sender, receiver)
                .isPresent();
    }
}