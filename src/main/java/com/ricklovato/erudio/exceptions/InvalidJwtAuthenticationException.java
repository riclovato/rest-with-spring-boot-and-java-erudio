package com.ricklovato.erudio.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.io.Serial;

// A anotação "ResponseStatus" informa ao Spring que, quando essa exceção for lançada,
// uma resposta HTTP com o status "NOT_FOUND" (404) deve ser retornada.
@ResponseStatus(HttpStatus.FORBIDDEN)
public class InvalidJwtAuthenticationException extends AuthenticationException {

    @Serial
    private static final long serialVersionUID = 3767545382542541030L;



    // Construtor que recebe uma mensagem de erro e a repassa para o construtor da classe pai.
    public InvalidJwtAuthenticationException(String s) {
        super(s);
    }

}
