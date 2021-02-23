package fr.flowarg.viplauncher.auth.mojang.responses;

import fr.flowarg.viplauncher.auth.mojang.Profile;

public class AuthenticationResponse extends LoginResponse
{
    private final Profile[] availableProfiles;

    public AuthenticationResponse(String accessToken, String clientToke, Profile selectedProfile, Profile[] availableProfiles)
    {
        super(accessToken, clientToke, selectedProfile);
        this.availableProfiles = availableProfiles;
    }

    public Profile[] getAvailableProfiles()
    {
        return availableProfiles;
    }
}