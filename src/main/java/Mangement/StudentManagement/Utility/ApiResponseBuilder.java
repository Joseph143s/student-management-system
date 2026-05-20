package Mangement.StudentManagement.Utility;

import Mangement.StudentManagement.DTO.Response.ApiResponse;

import java.time.LocalDateTime;

public class ApiResponseBuilder {

    public static <T> ApiResponse<T> success(
            int status,
            String message,
            T data){

        return new ApiResponse<>(
                LocalDateTime.now(),
                status,
                message,
                data
        );
    }

    public static <T> ApiResponse<T> error(
            int status,
            String message,
            T data){

        return new ApiResponse<>(
                LocalDateTime.now(),
                status,
                message,
                data
        );
    }
}