package com.Grupo.ProjetoAcessibilidade.controller;

import com.Grupo.ProjetoAcessibilidade.exceptions.ResourceAlreadyExistsException;
import com.Grupo.ProjetoAcessibilidade.exceptions.ResourceNotFound;
import com.Grupo.ProjetoAcessibilidade.model.Response;
import com.Grupo.ProjetoAcessibilidade.utils.ResponseUtil;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;


@RestControllerAdvice
public class GlobalExceptionController {

//    @ExceptionHandler(RuntimeException.class)
//    public ResponseEntity<Response<Object>> handleRuntimeException(RuntimeException ex) {
//        Response<Object> body = ResponseUtil.error("Ocorreu um erro inesperado", "Erro", 1001);
//        return ResponseEntity.status(500).body(body);
//    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Response<Object>> handleIllegalArgumentException(IllegalArgumentException ex) {
        Response<Object> body = ResponseUtil.error(ex.getMessage(), "Requisição inválida", 400);
        return ResponseEntity.status(400).body(body);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Response<Object>> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
        List<String> errors = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(err -> err.getField() + ": " + err.getDefaultMessage())
                .toList();
        Response<Object> body = ResponseUtil.error(errors, "Erro de validação", 400);
        return ResponseEntity.status(400).body(body);
    }

    @ExceptionHandler(ResourceNotFound.class)
    public ResponseEntity<Response<Object>> handleResourceNotFound(ResourceNotFound ex){
        Response<Object> body = ResponseUtil.error(ex.getMessage(), "Recurso não encontrado", 404);
        return ResponseEntity.status(404).body(body);
    }

    @ExceptionHandler(ResourceAlreadyExistsException.class)
    public ResponseEntity<Response<Object>> handleResourceAlreadyExistsException(ResourceAlreadyExistsException ex){
        Response<Object> body = ResponseUtil.error(ex.getMessage(), "Recurso já está cadastrado", 409);
        return ResponseEntity.status(409).body(body);
    }

}