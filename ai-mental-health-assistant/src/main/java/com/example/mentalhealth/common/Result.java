package com.example.mentalhealth.common;

public record Result<T>(int code, String message, T data) {

    public static <T> Result<T> success(T data) {
        return new Result<>(200, "操作成功", data);
    }

    public static Result<Void> failure(int code, String message) {
        return new Result<>(code, message, null);
    }
}
