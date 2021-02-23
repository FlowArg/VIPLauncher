package fr.flowarg.vipbootstrap;

import fr.flowarg.flowcompat.Platform;
import fr.flowarg.flowio.FileUtils;
import fr.flowarg.flowlogger.ILogger;
import fr.flowarg.flowlogger.Logger;

import javax.swing.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.net.URLDecoder;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.security.NoSuchAlgorithmException;

public class Main implements Runnable
{
    private final ILogger logger = new Logger("[VIPLauncher - Bootstrap]", null);
    private final File launcher = new File(Platform.isOnWindows() ? System.getenv("APPDATA") : (Platform.isOnMac() ? System.getProperty("user.home") + "/Library/Application Support/" : System.getProperty("user.home")), ".vip/VIPLauncher.jar");

    public static void main(String[] args)
    {
        new Main().run();
    }

    @Override
    public void run()
    {
        new Thread(Splash::new).start();
        this.logger.info("Starting VIP Bootstrap...");
        if(this.launcher.exists())
        {
            try
            {
                final String existSha1 = FileUtils.getSHA1(this.launcher);
                final URL sha1URL = new URL("https://flowarg.github.io/minecraft/launcher/vip/VIPLauncher.sha1");
                final BufferedReader reader = new BufferedReader(new InputStreamReader(sha1URL.openStream()));
                final String str = reader.readLine().trim();
                if(!existSha1.equals(str))
                {
                    this.logger.info("SHA1 not valid, downloading launcher...");
                    this.download();
                    this.logger.info("Launching...");
                }
                else this.logger.info("SHA1 valid, launching launcher...");
                this.launch();
            } catch (IOException | NoSuchAlgorithmException e)
            {
                this.logger.printStackTrace(e);
                JOptionPane.showMessageDialog(null, "Une erreur est survenue, veuillez envoyer l'erreur à un développeur.\n" + e.getMessage(), "Une erreur est survenue", JOptionPane.ERROR_MESSAGE);
            }
        }
        else
        {
            try
            {
                this.logger.info("Launcher not existing, downloading it...");
                this.download();
                this.launch();
            } catch (IOException e)
            {
                this.logger.printStackTrace(e);
                JOptionPane.showMessageDialog(null, "Une erreur est survenue, veuillez envoyer l'erreur à un développeur.\n" + e.getMessage(), "Une erreur est survenue", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void download() throws IOException
    {
        final URL launcherURL = new URL("https://flowarg.github.io/minecraft/launcher/vip/VIPLauncher.jar");
        this.launcher.getParentFile().mkdirs();
        this.launcher.delete();
        Files.copy(launcherURL.openStream(), this.launcher.toPath(), StandardCopyOption.REPLACE_EXISTING);
    }

    private void launch()
    {
        try
        {
            final URLClassLoader loader = (URLClassLoader)ClassLoader.getSystemClassLoader();
            final Class<URLClassLoader> loaderClass = URLClassLoader.class;
            final Method method = loaderClass.getDeclaredMethod("addURL", URL.class);
            method.setAccessible(true);
            method.invoke(loader, this.launcher.toURI().toURL());

            Splash.isDownloading.set(false);

            new Thread(() -> {
                try
                {
                    final Class<?> cls = Class.forName("fr.flowarg.viplauncher.Main", true, loader);
                    final Method main = cls.getDeclaredMethod("main", String[].class);
                    main.invoke(null, (Object)new String[]{"--start", FileUtils.getSHA1(new File(URLDecoder.decode(Main.class.getProtectionDomain().getCodeSource().getLocation().getFile(), "UTF-8")))});
                }
                catch (Exception e)
                {
                    this.logger.printStackTrace(e);
                    JOptionPane.showMessageDialog(null, "Une erreur est survenue, veuillez envoyer l'erreur à un développeur.\n" + e.getMessage(), "Une erreur est survenue", JOptionPane.ERROR_MESSAGE);
                }
            }).start();
        } catch (IOException | NoSuchMethodException | IllegalAccessException | InvocationTargetException e)
        {
            this.logger.printStackTrace(e);
            JOptionPane.showMessageDialog(null, "Une erreur est survenue, veuillez envoyer l'erreur à un développeur.\n" + e.getMessage(), "Une erreur est survenue", JOptionPane.ERROR_MESSAGE);
        }
    }
}
