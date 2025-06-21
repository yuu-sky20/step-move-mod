package com.ysk.stepmove.common;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

public class Result<T> {
    private final boolean success;
    private final T data;
    private final String errorMessage;

    public Result(boolean success, T data, String errorMessage) {
        this.success = success;
        this.data = data;
        this.errorMessage = errorMessage;
    }

    public boolean isSuccess() {
        return success;
    }

    public boolean isFailure() {
        return !success;
    }

    public T getData() {
        return data;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    @Contract(value = "_ -> new", pure = true)
    public static <T> @NotNull Result<T> success(T data) {
        return new Result<>(true, data, "Operation successful");
    }

    @Contract(value = "_ -> new", pure = true)
    public static <T> @NotNull Result<T> failure(String message) {
        return new Result<>(false, null, message);
    }
}
