package fr.flowarg.vipbootstrap;

import java.awt.*;
import java.awt.event.*;
import java.util.concurrent.atomic.AtomicBoolean;

public class Splash extends Frame implements ActionListener
{
    public static AtomicBoolean isDownloading = new AtomicBoolean(true);

    private static final WindowListener CLOSE_WINDOW = new WindowAdapter()
    {
        @Override
        public void windowClosing(WindowEvent e)
        {
            e.getWindow().dispose();
        }
    };

    public Splash()
    {
        super("VIPLauncher - Bootstrap");
        this.setSize(218, 234);
        this.setLayout(new BorderLayout());
        this.addWindowListener(CLOSE_WINDOW);
        this.setAlwaysOnTop(false);
        this.setResizable(false);
        this.setUndecorated(true);

        final SplashScreen splash = SplashScreen.getSplashScreen();
        splash.createGraphics();
        while (isDownloading.get())
            splash.update();

        splash.close();
    }

    @Override
    public void actionPerformed(ActionEvent ae)
    {
        System.exit(0);
    }
}
