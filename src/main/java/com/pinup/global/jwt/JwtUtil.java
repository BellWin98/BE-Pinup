package com.pinup.global.jwt;

import com.pinup.entity.Member;
import com.pinup.global.exception.PinUpException;
import com.pinup.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class JwtUtil {

    private final MemberRepository memberRepository;
    private final JwtTokenProvider jwtTokenProvider;

    public Long getUserIdFromAuthorizationHeader(String authorizationHeader){
        String token = authorizationHeader.replace("Bearer ","");
        String email = jwtTokenProvider.getEmail(token);
        Member member = memberRepository.findByEmail(email)
                .orElseThrow(()->PinUpException.MEMBER_NOT_FOUND);

        return member.getId();
    }
}
