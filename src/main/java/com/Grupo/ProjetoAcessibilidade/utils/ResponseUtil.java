package com.Grupo.ProjetoAcessibilidade.utils;

import com.Grupo.ProjetoAcessibilidade.model.Response;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

public class ResponseUtil {

    public static <T> Response<T> success(T data, String message) {
        Response<T> response = new Response<>();
        response.setSuccess(true);
        response.setMessage(message);
        response.setData(data);
        response.setErrors(null);
        response.setTimestamp(LocalDateTime.now());
        response.setPath(RequestContext.getUri());
        response.setErrorCode(null);
        return response;
    }

    public static <T> Response<T> error(List<String> errors, String message, int errorCode) {
        Response<T> response = new Response<>();
        response.setSuccess(false);
        response.setMessage(message);
        response.setData(null);
        response.setErrors(errors);
        response.setTimestamp(LocalDateTime.now());
        response.setPath(RequestContext.getUri());
        response.setErrorCode(errorCode);
        return response;
    }

    public static <T> Response<T> error(String error, String message, int errorCode) {
        return error(Arrays.asList(error), message, errorCode);
    }
}