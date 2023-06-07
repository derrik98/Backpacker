package it.ispw.daniele.backpacker.exceptions;

import java.io.Serial;

public class MonumentNotFoundException extends Exception {

    @Serial
    private static final long serialVersionUID = 1L;

    public MonumentNotFoundException(String message) {
        super(message);
    }
}
