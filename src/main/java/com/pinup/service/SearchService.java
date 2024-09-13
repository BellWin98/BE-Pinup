package com.pinup.service;

import com.pinup.domain.member.entity.Member;
import com.pinup.dto.response.search.MemberSearchResponse;
import com.pinup.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SearchService {

    private final MemberRepository memberRepository;

    @Transactional(readOnly = true)
    public List<MemberSearchResponse> searchUsers(String query) {
        List<Member> users = memberRepository.findByNicknameContainingIgnoreCase(query);
        return users.stream()
                .map(this::convertToUserSearchResponse)
                .collect(Collectors.toList());
    }

    private MemberSearchResponse convertToUserSearchResponse(Member member) {
        return MemberSearchResponse.builder()
                .memberId(member.getId())
                .email(member.getEmail())
                .name(member.getName())
                .profilePictureUrl(member.getProfileImageUrl())
                .nickname(member.getNickname())
                .build();
    }
}
