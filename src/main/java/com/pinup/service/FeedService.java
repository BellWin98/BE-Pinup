package com.pinup.service;

import com.pinup.dto.request.BioUpdateRequest;
import com.pinup.dto.request.NicknameUpdateRequest;
import com.pinup.dto.response.MemberResponse;
import com.pinup.entity.Member;
import com.pinup.global.exception.PinUpException;
import com.pinup.global.s3.S3Service;
import com.pinup.repository.MemberRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import static com.pinup.global.exception.PinUpException.*;

@Service
@RequiredArgsConstructor
public class FeedService {
    private static final String PROFILE_IMAGE_DIRECTORY = "profiles";

    private final MemberRepository memberRepository;
    private final S3Service s3Service;

    @Transactional
    public boolean checkNicknameDuplicate(String nickname) {
        return memberRepository.findByNickname(nickname).isPresent();
    }

    @Transactional
    public MemberResponse updateBio(BioUpdateRequest request) {
        Member member = getCurrentMember();
        member.updateBio(request.getBio());
        memberRepository.save(member);
        return MemberResponse.from(member);
    }

    @Transactional
    public MemberResponse updateNickname(NicknameUpdateRequest request) {
        Member member = getCurrentMember();

        validateNicknameUpdate(member, request.getNickname());

        member.updateNickname(request.getNickname());
        memberRepository.save(member);
        return MemberResponse.from(member);
    }

    private void validateNicknameUpdate(Member member, String newNickname) {
        validateNicknameUpdateTimeLimit(member);
        validateNicknameDuplicate(newNickname);
    }

    private void validateNicknameUpdateTimeLimit(Member member) {
        if (!member.canUpdateNickname()) {
            throw NICKNAME_UPDATE_TIME_LIMIT;
        }
    }

    private void validateNicknameDuplicate(String nickname) {
        if (memberRepository.findByNickname(nickname).isPresent()) {
            throw ALREADY_EXIST_NICKNAME;
        }
    }

    @Transactional
    public MemberResponse updateProfileImage(MultipartFile image) {
        Member member = getCurrentMember();

        if (member.getProfileImageUrl() != null) {
            s3Service.deleteFile(member.getProfileImageUrl());
        }

        String imageUrl = s3Service.uploadFile(PROFILE_IMAGE_DIRECTORY, image);
        member.updateProfileImage(imageUrl);
        memberRepository.save(member);
        return MemberResponse.from(member);
    }

    private Member getCurrentMember() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        return memberRepository.findByEmail(email)
                .orElseThrow(() -> MEMBER_NOT_FOUND);
    }

}
