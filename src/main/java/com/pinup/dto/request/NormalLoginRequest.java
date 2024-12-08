package com.pinup.dto.request;

import lombok.Data;

@Data
public class NormalLoginRequest {
    private String email;
    private String password;
}
