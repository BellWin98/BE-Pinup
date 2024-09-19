package com.pinup.service;

import com.pinup.dto.response.MemberSearchResponse;
import com.pinup.entity.Member;
import com.pinup.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;

    @Transactional(readOnly = true)
    public List<MemberSearchResponse> searchUsers(String query) {
        List<Member> users = memberRepository.findByNicknameContainingIgnoreCase(query);
        return users.stream()
                .map(MemberSearchResponse::from)
                .collect(Collectors.toList());
    }
}
