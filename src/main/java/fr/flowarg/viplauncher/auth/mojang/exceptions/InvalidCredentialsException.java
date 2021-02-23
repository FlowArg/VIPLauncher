package fr.flowarg.viplauncher.auth.mojang.exceptions;

import fr.flowarg.viplauncher.auth.mojang.responses.ErrorResponse;

public class InvalidCredentialsException extends RequestException
{
    public InvalidCredentialsException(ErrorResponse error) { super(error); }
}
