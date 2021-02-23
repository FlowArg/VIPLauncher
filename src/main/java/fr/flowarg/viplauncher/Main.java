package fr.flowarg.viplauncher;

import fr.flowarg.flowupdater.utils.IOUtils;

import javax.swing.*;
import java.io.IOException;
import java.net.URL;

public class Main
{
    public static void main(String[] args) throws Exception
    {
        try
        {
            new URL("https://repo1.maven.org/").openStream();
        } catch (IOException e)
        {
            JOptionPane.showMessageDialog(null, "Pas de connexion Internet !", "Unknown Host", JOptionPane.ERROR_MESSAGE);
            System.exit(0);
        }

        if(args.length >= 2)
        {
            if(args[0].equals("--start") && args[1].equalsIgnoreCase(IOUtils.getContent(new URL("https://flowarg.github.io/minecraft/launcher/vip/VIPBootstrap.sha1")).trim()))
                new VIPLauncher().start();
            else JOptionPane.showMessageDialog(null, "Veuillez télécharger le Bootstrap à nouveau !\n https://flowarg.github.io/minecraft/launcher/vip/VIPBootstrap.jar (content)", "Boostrap pas à jour !", JOptionPane.ERROR_MESSAGE);
        }
        else JOptionPane.showMessageDialog(null, "Veuillez télécharger le Bootstrap à nouveau !\n https://flowarg.github.io/minecraft/launcher/vip/VIPBootstrap.jar (arguments)", "Boostrap pas à jour !", JOptionPane.ERROR_MESSAGE);
    }
}
