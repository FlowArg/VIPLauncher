package fr.flowarg.viplauncher.auth.mojang.exceptions;

import fr.flowarg.viplauncher.auth.mojang.responses.ErrorResponse;

public class UserMigratedException extends RequestException {
    public UserMigratedException(ErrorResponse error) { super(error); }
}
