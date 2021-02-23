package fr.flowarg.viplauncher.auth.mojang.exceptions;

import fr.flowarg.viplauncher.auth.mojang.responses.ErrorResponse;

public class RequestException extends Exception{

    private final ErrorResponse error;
    public RequestException(ErrorResponse error) {
        this.error = error;
    }

    public ErrorResponse getError() {
        return error;
    }
    public String getErrorMessage(){
        return this.error.getErrorMessage();
    }
    public String getErrorCause(){
        return this.error.getCause();
    }
}
