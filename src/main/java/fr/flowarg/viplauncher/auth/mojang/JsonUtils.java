package fr.flowarg.viplauncher.auth.mojang;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.LinkedHashMap;
import java.util.Map;

public class JsonUtils
{
    private static final Map<String, Object> MINECRAFT_AGENT = new LinkedHashMap<>();
    public static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    public static final Type STRING_OBJECT_MAP = new TypeToken<Map<String, Object>>() {}.getType();;

    static
    {
        final Map<String, Object> agentValues = new LinkedHashMap<>();
        agentValues.put("name", "Minecraft");
        agentValues.put("version", 1);
        MINECRAFT_AGENT.put("agent", agentValues);
    }

    public static String credentialsToJson(String username, String password, String clientToken)
    {
        final Map<String, Object> jsonData = new LinkedHashMap<>(MINECRAFT_AGENT);
        jsonData.put("username", username);
        jsonData.put("password", password);
        if (clientToken != null)
            jsonData.put("clientToken", clientToken);
        return GSON.toJson(jsonData);
    }

    public static String tokenToJson(String authToken, String clientToken)
    {
        final Map<String, Object> jsonData = new LinkedHashMap<>();
        jsonData.put("accessToken", authToken);
        if (clientToken != null)
            jsonData.put("clientToken", clientToken);
        return GSON.toJson(jsonData);
    }
}
