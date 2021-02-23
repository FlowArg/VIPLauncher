package fr.flowarg.viplauncher.auth.microsoft;

import java.io.Serializable;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

// Adapted from MiniLauncher (a project of MiniDigger), see his amazing work on GitHub !
public class AuthInfo implements Serializable
{
    private static final long serialVersionUID = 1L;

    private final String username;
    private final String token;
    private final UUID uuid;
    private final Map<String, String> properties;
    private final String userType;

    public AuthInfo(String username, String token, UUID uuid, Map<String, String> properties, String userType)
    {
        Objects.requireNonNull(username);
        Objects.requireNonNull(token);
        Objects.requireNonNull(uuid);
        Objects.requireNonNull(properties);
        Objects.requireNonNull(userType);

        this.username = username;
        this.token = token;
        this.uuid = uuid;
        this.properties = properties;
        this.userType = userType;
    }

    public String getUsername()
    {
        return username;
    }

    public String getToken()
    {
        return token;
    }

    public UUID getUUID()
    {
        return uuid;
    }

    public Map<String, String> getProperties()
    {
        return properties;
    }

    public String getUserType()
    {
        return userType;
    }

    @Override
    public String toString()
    {
        return String.format("AuthInfo [username=%s, token=%s, uuid=%s, properties=%s, userType=%s]", username, token, uuid, properties, userType);
    }

    @Override
    public boolean equals(Object obj)
    {
        if (obj == this)
            return true;
        if (obj instanceof AuthInfo)
        {
            final AuthInfo another = (AuthInfo)obj;
            return Objects.equals(username, another.username) && Objects.equals(token, another.token) && Objects.equals(uuid, another.uuid) && Objects.equals(properties, another.properties) && Objects.equals(userType, another.userType);
        }
        return false;
    }

    @Override
    public int hashCode()
    {
        return token.hashCode();
    }
}
