package com.flight.util;

import java.util.List;

public class ApiResponse<T> {

    private int responseCode;
    private String responseMessage;
    private T data;
    private List<ApiError> error;

    public ApiResponse(int responseCode, String responseMessage, T data, List<ApiError> error) {
        this.responseCode = responseCode;
        this.responseMessage = responseMessage;
        this.data = data;
        this.error = error;
    }

    public static <T> ApiResponse<T> success(T data) {
        return new ApiResponse<>(0, "SUCCESS", data, null);
    }

    public static ApiResponse<Void> failure(int responseCode, List<ApiError> errors) {
        return new ApiResponse<>(responseCode, "FAILURE", null, errors);
    }

    public int getResponseCode() {
        return responseCode;
    }

    public String getResponseMessage() {
        return responseMessage;
    }

    public T getData() {
        return data;
    }

    public List<ApiError> getError() {
        return error;
    }
}
