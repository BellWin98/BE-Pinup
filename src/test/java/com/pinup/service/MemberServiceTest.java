package com.pinup.service;

import com.pinup.dto.response.search.MemberSearchResponse;
import com.pinup.entity.Member;
import com.pinup.repository.MemberRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.lang.reflect.Field;
import java.util.List;

import static com.pinup.constants.TestConstants.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

public class MemberServiceTest {

    @Mock
    private MemberRepository memberRepository;

    @InjectMocks
    private MemberService memberService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("특정 닉네임을 포함하는 회원이 존재하면, MemberSearchResponse를 원소로 가지는 비어있지 않은 리스트를 반환해야 한다")
    public void 주어진_닉네임을_포함하는_회원이_존재하면_MemberSearchResponse_리스트를_반환해야_한다() throws Exception {
        // given
        Member testMember = Member.builder()
                .profileImageUrl(TEST_PROFILE_IMAGE)
                .email(TEST_EMAIL)
                .socialId(TEST_SOCIAL_ID)
                .nickname(TEST_NICKNAME)
                .name(TEST_NAME)
                .loginType(TEST_LOGIN_TYPE)
                .build();
        setMemberId(testMember, 1L);
        when(memberRepository.findByNicknameContainingIgnoreCase(TEST_NICKNAME)).thenReturn(List.of(testMember));

        // when
        List<MemberSearchResponse> memberSearchResponses = memberService.searchUsers(TEST_NICKNAME);

        // then
        assertThat(memberSearchResponses).isNotEmpty();
        assertThat(memberSearchResponses).hasSize(1);
        assertThat(memberSearchResponses)
                .allSatisfy(response -> assertThat(response).isInstanceOf(MemberSearchResponse.class));
    }


    private void setMemberId(Object object, Long id) throws Exception {
        Field idField = object.getClass().getDeclaredField("id");
        idField.setAccessible(true);
        idField.set(object, id);
    }
}
