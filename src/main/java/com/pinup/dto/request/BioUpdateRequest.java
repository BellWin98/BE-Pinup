package com.pinup.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class BioUpdateRequest {
    @NotBlank(message = "한줄소개를 입력해주세요.")
    @Size(max = 100, message = "한줄소개는 100자 이내로 입력해주세요.")
    private String bio;
}