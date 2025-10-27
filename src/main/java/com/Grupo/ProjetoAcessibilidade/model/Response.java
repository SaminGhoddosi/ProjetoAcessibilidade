package com.Grupo.ProjetoAcessibilidade.model;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class Response<T> {
    private boolean success;
    private String message;
    private T data;
    private List<String> errors;
    private LocalDateTime timestamp;
    private String path;
    private Integer errorCode;
}
