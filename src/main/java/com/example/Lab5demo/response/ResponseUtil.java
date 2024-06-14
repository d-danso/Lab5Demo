package com.example.Lab5demo.response;

import com.example.Lab5demo.entity.User;

import java.util.List;

public class ResponseUtil {

    public static <T> ApiResponse<List<User>> success(T data) {
        return (ApiResponse<List<User>>) new ApiResponse<>("success", "Operation successful", data);
    }

    public static <T> ApiResponse<T> success(String message, T data) {
        return new ApiResponse<>("success", message, data);
    }

    public static <T> ApiResponse<T> error(String message) {
        return new ApiResponse<>("error", message, null);
    }
}

