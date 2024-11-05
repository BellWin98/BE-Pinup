package com.pinup.service;


import com.pinup.dto.request.MemberJoinRequest;
import com.pinup.entity.Member;

import com.pinup.global.response.TokenResponse;
import com.pinup.enums.LoginType;
import com.pinup.global.exception.PinUpException;
import com.pinup.global.jwt.JwtTokenProvider;
import com.pinup.repository.MemberRepository;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Map;

import static com.pinup.global.exception.PinUpException.ALREADY_EXIST_EMAIL;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final RestTemplate restTemplate;
    private final MemberRepository memberRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final RedisService redisService;
    private final PasswordEncoder passwordEncoder;

    @Value("${oauth2.google.client-id}")
    private String googleClientId;

    @Value("${oauth2.google.client-secret}")
    private String googleClientSecret;

    @Value("${oauth2.google.redirect-uri}")
    private String googleRedirectUri;

    @Value("${oauth2.google.token-uri}")
    private String googleTokenUri;

    @Value("${oauth2.google.resource-uri}")
    private String googleResourceUri;

    @Value("${oauth2.google.auth-uri}")
    private String googleAuthUri;

    public String getGoogleAuthorizationUrl() {
        return UriComponentsBuilder.fromHttpUrl(googleAuthUri)
                .queryParam("client_id", googleClientId)
                .queryParam("redirect_uri", googleRedirectUri)
                .queryParam("response_type", "code")
                .queryParam("scope", "https://www.googleapis.com/auth/userinfo.email https://www.googleapis.com/auth/userinfo.profile openid")
                .toUriString();
    }

    @Transactional
    public TokenResponse googleLogin(String code) {
        String accessToken = getAccessToken(code);
        Map<String, Object> userInfo = getUserInfo(accessToken);

        String socialId = (String) userInfo.get("sub");
        String email = (String) userInfo.get("email");
        String name = (String) userInfo.get("name");
        String profilePictureUrl = (String) userInfo.get("picture");

        Member member = memberRepository.findByEmail(email)
                .orElseGet(() -> memberRepository.save(Member.builder()
                        .email(email)
                        .name(name)
                        .profileImageUrl(profilePictureUrl)
                        .loginType(LoginType.GOOGLE)
                        .socialId(socialId)
                        .build()));

        String jwtToken = jwtTokenProvider.createAccessToken(member.getEmail(), member.getRole());
        String refreshToken = jwtTokenProvider.createRefreshToken(email);

        redisService.setValues(member.getEmail(), refreshToken);

        return new TokenResponse(jwtToken, refreshToken);
    }

    @Transactional
    public TokenResponse getTokens(HttpServletRequest request) {

        String token = request.getHeader("Authorization");
        Map<String, Object> userInfo = getUserInfo(token);
        Member createdMember = createMember(userInfo);
        String accessToken = jwtTokenProvider.createAccessToken(createdMember.getEmail(), createdMember.getRole());
        String refreshToken = jwtTokenProvider.createRefreshToken(createdMember.getEmail());

        redisService.setValues(createdMember.getEmail(), refreshToken);

        return new TokenResponse(accessToken, refreshToken);
    }

    public TokenResponse refresh(String refreshToken) {
        if (!jwtTokenProvider.validateToken(refreshToken)) {
            throw PinUpException.INVALID_TOKEN;
        }

        String email = jwtTokenProvider.getEmail(refreshToken);
        String storedRefreshToken = redisService.getValues(email);
        if (storedRefreshToken == null || !storedRefreshToken.equals(refreshToken)) {
            throw PinUpException.INVALID_TOKEN;
        }

        Member member = memberRepository.findByEmail(email)
                .orElseThrow(() -> PinUpException.MEMBER_NOT_FOUND);

        String newAccessToken = jwtTokenProvider.createAccessToken(email, member.getRole());
        String newRefreshToken = jwtTokenProvider.createRefreshToken(email);

        redisService.setValues(email, newRefreshToken);

        return new TokenResponse(newAccessToken, newRefreshToken);
    }

    public void logout(String accessToken) {
        if (!jwtTokenProvider.validateToken(accessToken)) {
            throw PinUpException.INVALID_TOKEN;
        }

        String email = jwtTokenProvider.getEmail(accessToken);
        redisService.deleteValues(email);
    }

    private String getAccessToken(String authorizationCode) {
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("code", authorizationCode);
        params.add("client_id", googleClientId);
        params.add("client_secret", googleClientSecret);
        params.add("redirect_uri", googleRedirectUri);
        params.add("grant_type", "authorization_code");

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(params, headers);

        ResponseEntity<Map> response = restTemplate.exchange(googleTokenUri, HttpMethod.POST, request, Map.class);

        if (response.getBody() == null) {
            throw PinUpException.INTERNAL_SERVER_ERROR;
        }

        return (String) response.getBody().get("access_token");
    }

    private Map<String, Object> getUserInfo(String accessToken) {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(accessToken);

        HttpEntity<String> entity = new HttpEntity<>("", headers);

        ResponseEntity<Map> response = restTemplate.exchange(googleResourceUri, HttpMethod.GET, entity, Map.class);

        if (response.getBody() == null) {
            throw PinUpException.INTERNAL_SERVER_ERROR;
        }

        return response.getBody();
    }

    private Member createMember(Map<String, Object> userInfo) {

        String socialId = (String) userInfo.get("sub");
        String email = (String) userInfo.get("email");
        String name = (String) userInfo.get("name");
        String profilePictureUrl = (String) userInfo.get("picture");

        return memberRepository.findByEmail(email)
                .orElseGet(() -> memberRepository.save(Member.builder()
                        .email(email)
                        .name(name)
                        .profileImageUrl(profilePictureUrl)
                        .loginType(LoginType.GOOGLE)
                        .socialId(socialId)
                        .build()));
    }

    @Transactional
    public void join(MemberJoinRequest request) {
        validateExistEmail(request.getEmail());

        Member newMember = Member.builder()
                .email(request.getEmail())
                .nickname(request.getNickname())
                .name(request.getName())
                .loginType(LoginType.NORMAL)
                .profileImageUrl(request.getProfileImageUrl())
                .password(passwordEncoder.encode(request.getPassword()))
                .build();
        memberRepository.save(newMember);
    }

    private void validateExistEmail(String email) {
        memberRepository.findByEmail(email)
                .ifPresent(member -> {
                    throw ALREADY_EXIST_EMAIL;
                });
    }
}