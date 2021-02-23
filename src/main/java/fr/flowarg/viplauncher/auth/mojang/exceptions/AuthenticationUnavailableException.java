package fr.flowarg.viplauncher.auth.mojang.exceptions;

import fr.flowarg.viplauncher.auth.mojang.responses.ErrorResponse;

public class AuthenticationUnavailableException extends Exception {

    public AuthenticationUnavailableException(ErrorResponse error) {

    }
}
