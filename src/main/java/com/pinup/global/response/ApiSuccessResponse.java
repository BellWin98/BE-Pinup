package com.pinup.global.response;

import lombok.Getter;

@Getter
public class ApiSuccessResponse<T> {

    public static final ApiSuccessResponse<?> NO_DATA_RESPONSE = new ApiSuccessResponse<>(null);

    private final T data;

    protected ApiSuccessResponse(T data){
        this.data = data;
    }

    public static <T> ApiSuccessResponse<T> from(T data) {
        return new ApiSuccessResponse<>(data);
    }
}
