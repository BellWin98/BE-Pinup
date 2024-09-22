package com.pinup.service;

import com.pinup.dto.response.MemberSearchResponse;
import com.pinup.entity.FriendShip;
import com.pinup.entity.Member;
import com.pinup.global.exception.PinUpException;
import com.pinup.repository.FriendShipRepository;
import com.pinup.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class FriendShipService {

    private final FriendShipRepository friendShipRepository;
    private final MemberRepository memberRepository;

    @Transactional(readOnly = true)
    public List<MemberSearchResponse> getAllFriends(Long memberId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> PinUpException.MEMBER_NOT_FOUND);

        return friendShipRepository.findAllByMember(member)
                .stream()
                .map(friendShip -> MemberSearchResponse.from(friendShip.getFriend()))
                .collect(Collectors.toList());
    }

    @Transactional
    public void createFriendShip(Member member, Member friend){
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
}