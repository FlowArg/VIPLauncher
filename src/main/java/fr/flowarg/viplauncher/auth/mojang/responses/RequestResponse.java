package fr.flowarg.viplauncher.auth.mojang.responses;

import java.util.Map;

public class RequestResponse
{
    private final int responseCode;
    private final Map<String, Object> data;

    public RequestResponse(int responseCode, Map<String, Object> data)
    {
        this.responseCode = responseCode;
        this.data = data;
    }

    public int getResponseCode()
    {
        return responseCode;
    }

    public boolean isSuccessful()
    {
        return responseCode == 200 || responseCode == 204;
    }

    public Map<String, Object> getData()
    {
        return data;
    }
}
