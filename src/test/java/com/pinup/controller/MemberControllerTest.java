package com.pinup.controller;

import com.pinup.dto.response.MemberResponse;
import com.pinup.mock.WithCustomMockUser;
import com.pinup.service.MemberService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static com.pinup.constants.TestConstants.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.hamcrest.Matchers.containsString;


@AutoConfigureMockMvc
@SpringBootTest
public class MemberControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private MemberService memberService;

    @Test
    @WithCustomMockUser
    @DisplayName("인증된 사용자는 닉네임으로 다른 멤버를 검색할 수 있다")
    public void 인증된_사용자는_닉네임으로_다른_멤버를_검색할_수_있다() throws Exception {
        // given
        String queryNickname = "testNick";
        MemberResponse mockResponse = MemberResponse.builder()
                .memberId(1L)
                .email(TEST_EMAIL)
                .name(TEST_NAME)
                .profilePictureUrl(TEST_IMAGE_URL)
                .nickname(queryNickname)
                .build();

        when(memberService.searchUsers(anyString())).thenReturn(List.of(mockResponse));

        // when & then
        mockMvc.perform(get("/api/members/search")
                        .param("query", queryNickname)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.data").isArray())
                .andExpect(jsonPath("$.data[0].memberId").value(1))
                .andExpect(jsonPath("$.data[0].email").value(TEST_EMAIL))
                .andExpect(jsonPath("$.data[0].name").value(TEST_NAME))
                .andExpect(jsonPath("$.data[0].profilePictureUrl").value(TEST_IMAGE_URL))
                .andExpect(jsonPath("$.data[0].nickname").value(containsString(queryNickname)));
    }

    @Test
    @DisplayName("인증되지 않은 사용자는 멤버 검색 시 인증 오류를 받는다")
    public void 인증되지_않은_사용자는_멤버_검색_시_인증_오류를_받는다() throws Exception {
        String queryNickname = "testNick";

        mockMvc.perform(get("/api/members/search")
                        .param("query", queryNickname)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());
    }
}