package it.ispw.daniele.backpacker.exceptions;


public class LoginFailException extends EmptyFieldException{

    public LoginFailException(String message) {
        super(message);
    }
}
