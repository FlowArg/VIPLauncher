package fr.flowarg.viplauncher.auth.mojang.responses;

import fr.flowarg.viplauncher.auth.mojang.Profile;

public class LoginResponse
{
    private final String accessToken;
    private final String clientToken;
    private final Profile selectedProfile;

    public LoginResponse(String accessToken, String clientToken, Profile selectedProfile)
    {
        this.accessToken = accessToken;
        this.clientToken = clientToken;
        this.selectedProfile = selectedProfile;
    }

    public String getAccessToken()
    {
        return accessToken;
    }

    public String getClientToken()
    {
        return clientToken;
    }

    public Profile getSelectedProfile()
    {
        return selectedProfile;
    }
}
