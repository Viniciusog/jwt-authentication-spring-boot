package com.viniciusog.userjwtexample.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
public class AppException extends RuntimeException{
    //A api irá retornar erros quando a request não for válida ou quando um erro acontecer
    //Além disso, queremos diferentes tipos de HTTP status para cada tipo de erro

    public AppException(String message) {
        super(message);
    }

    public AppException(String message, Throwable cause) {
        super(message, cause);
    }
}
