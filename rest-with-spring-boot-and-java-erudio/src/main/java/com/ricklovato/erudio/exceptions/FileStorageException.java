package com.ricklovato.erudio.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.io.Serial;

// A anotação "ResponseStatus" informa ao Spring que, quando essa exceção for lançada,
// uma resposta HTTP com o status "NOT_FOUND" (404) deve ser retornada.
@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
public class FileStorageException extends RuntimeException {

    @Serial
    private static final long serialVersionUID = 4598311467919700229L;

    public FileStorageException(String s) {
        super(s);
    }

    public FileStorageException(String s, Throwable cause) {
        super(s,cause);
    }
}
