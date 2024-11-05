package com.pinup.dto;

import lombok.Data;

@Data
public class NormalLoginRequest {
    private String email;
    private String password;
}
