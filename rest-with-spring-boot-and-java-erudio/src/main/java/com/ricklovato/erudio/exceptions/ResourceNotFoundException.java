package com.ricklovato.erudio.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.io.Serial;

// A anotação "ResponseStatus" informa ao Spring que, quando essa exceção for lançada,
// uma resposta HTTP com o status "NOT_FOUND" (404) deve ser retornada.
@ResponseStatus(HttpStatus.NOT_FOUND)
public class ResourceNotFoundException extends RuntimeException {

    // Número de série para garantir a compatibilidade entre diferentes versões da classe.
    @Serial
    private static final long serialVersionUID = 4598311467919700229L;

    // Construtor que recebe uma mensagem de erro e a repassa para o construtor da classe pai.
    public ResourceNotFoundException(String s) {
        super(s);
    }
}
