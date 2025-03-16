package com.school.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalException {

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<Map<String,Object>> resourceNotFound(UserNotFoundException ex) {
        Map<String, Object> errorInfo = new HashMap<>();
        ExceptionResponse response = new ExceptionResponse();
        response.setHttpCode(String.valueOf(HttpStatus.NOT_FOUND.value()));
        response.setMessage(ex.getMessage());
        response.setSuccess(false);
        errorInfo.put("status", response);
        errorInfo.put("data", null);
        return new ResponseEntity<Map<String, Object>>(errorInfo, HttpStatus.NOT_FOUND);
    }
    @ExceptionHandler(BadReqException.class)
    public ResponseEntity<Map<String,Object>> badReqException(BadReqException ex) {
        Map<String, Object> errorInfo = new HashMap<>();
        ExceptionResponse response = new ExceptionResponse();
        response.setHttpCode(String.valueOf(HttpStatus.BAD_REQUEST.value()));
        response.setMessage(ex.getMessage());
        response.setSuccess(false);
        errorInfo.put("status", response);
        errorInfo.put("data", null);
        return new ResponseEntity<Map<String, Object>>(errorInfo, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String,Object>> exception(Exception ex) {

        Map<String, Object> errorInfo = new HashMap<>();
        ExceptionResponse response = new ExceptionResponse();
        response.setHttpCode(String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR.value()));
        response.setMessage(ex.getMessage());
        response.setSuccess(false);
        errorInfo.put("status", response);
        errorInfo.put("data", null);
        return new ResponseEntity<>(errorInfo, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
