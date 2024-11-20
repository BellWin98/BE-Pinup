package com.pinup.service;

import com.pinup.entity.Member;
import com.pinup.cache.MemberCacheManager;
import com.pinup.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.Optional;

import static com.pinup.global.exception.PinUpException.MEMBER_NOT_FOUND;

@Service
@RequiredArgsConstructor
@Slf4j
public class MemberCacheService {

    private final MemberRepository memberRepository;
    private final MemberCacheManager memberCacheManager;

    public void cacheNickname(String email, String nickname) {
        memberCacheManager.putInCache(email, nickname);
    }

    public boolean isNicknameDuplicate(String nickname) {
        String cachedEmail = memberCacheManager.getCachedEmail(nickname);
        if (cachedEmail != null) {
            return true;
        }

        Optional<Member> memberOpt = memberRepository.findByNickname(nickname);
        if (memberOpt.isPresent()) {
            Member member = memberOpt.get();
            cacheNickname(member.getEmail(), nickname);
            memberCacheManager.cacheNicknameEmail(nickname, member.getEmail());
            return true;
        }
        return false;
    }

    public void invalidateCache(String email) {
        memberCacheManager.invalidateCache(email);
    }

    @Transactional
    public void syncToDatabase() {
        memberCacheManager.syncCache(email ->
                memberRepository.findByEmail(email)
                        .map(Member::getNickname)
                        .orElse(null)
        );
    }
}