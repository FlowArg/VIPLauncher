package fr.flowarg.viplauncher.utils;

import fr.flowarg.viplauncher.VIPLauncher;

import java.io.*;
import java.util.Properties;

public class Config
{
    private final File file;
    private final Properties properties;

    public Config(File file) throws Exception
    {
        this.file = file;
        this.properties = new Properties();
        if(this.file.exists())
            this.load();
        else this.file.createNewFile();
    }

    private void load() throws IOException
    {
        this.properties.load(new BufferedReader(new FileReader(this.file)));
    }

    public String get(String key)
    {
        return this.properties.getProperty(key);
    }

    public String get(String key, String defaultValue)
    {
        return this.properties.getProperty(key, defaultValue);
    }

    public void set(String key, String value)
    {
        this.properties.setProperty(key, value);
        this.save();
    }

    private void save()
    {
        try
        {
            this.properties.store(new BufferedWriter(new FileWriter(this.file)), "Storage of accessToken, username, clientToken and UUID of player.");
        } catch (IOException e)
        {
            VIPLauncher.getInstance().getLogger().printStackTrace(e);
        }
    }
}
