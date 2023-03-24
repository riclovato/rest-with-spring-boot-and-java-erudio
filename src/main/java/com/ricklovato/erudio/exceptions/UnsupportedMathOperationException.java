package com.ricklovato.erudio.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.io.Serial;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class UnsupportedMathOperationException extends RuntimeException {

    @Serial
    private static final long serialVersionUID = 4598311467919700229L;

    public UnsupportedMathOperationException(String s) {
        super(s);
    }
}
