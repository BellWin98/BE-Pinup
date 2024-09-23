package com.pinup.service;

import com.pinup.dto.response.MemberResponse;
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
    public List<MemberResponse> searchUsers(String query) {
        List<Member> users = memberRepository.findByNicknameContainingIgnoreCase(query);
        return users.stream()
                .map(MemberResponse::from)
                .collect(Collectors.toList());
    }
}
