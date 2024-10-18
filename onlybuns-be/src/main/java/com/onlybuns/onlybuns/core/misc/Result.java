package com.onlybuns.onlybuns.core.misc;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Result<T> {

    private boolean success; // Indicates if the operation was successful
    private String message; // Message describing the result
    private T data; // Data returned from the operation
    private int code; // Status code associated with the result

    // Static factory methods
    public static <T> Result<T> success(T data) {
        return new Result<>(true, "Operation was successful", data, 200);
    }

    public static <T> Result<T> failure(String message, int code) {
        return new Result<>(false, message, null, code);
    }

    // Builder methods
    public Result<T> withMessage(String message) {
        this.message = message;
        return this;
    }

    public Result<T> withCode(int code) {
        this.code = code;
        return this;
    }

    public Result<T> withData(T data) {
        this.data = data;
        return this;
    }
}