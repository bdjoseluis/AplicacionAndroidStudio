package com.backend.TGF.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class ControllerExceptionHandler
{
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Response> handlevalidationArgumentErrors(MethodArgumentNotValidException ex)
    {
        Map<String, String> errores = new HashMap<>();

        ex.getBindingResult().getAllErrors().forEach((error) ->
                {
                    String fieldName = ((FieldError) error).getField();
                    String erroMessage = error.getDefaultMessage();
                    errores.put(fieldName, erroMessage);
                }
        );

        return new ResponseEntity<Response>(Response.validationError(errores), HttpStatus.BAD_REQUEST);
    }
}
