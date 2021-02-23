package fr.flowarg.viplauncher.auth.mojang.responses;

public class ErrorResponse
{
    private String error;
    private String errorMessage;
    private String cause;

    public String getError()
    {
        return error;
    }

    public String getErrorMessage()
    {
        return errorMessage;
    }

    public String getCause()
    {
        return cause;
    }
}
