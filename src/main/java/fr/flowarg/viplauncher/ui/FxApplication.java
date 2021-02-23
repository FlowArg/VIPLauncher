package fr.flowarg.viplauncher.ui;

import fr.flowarg.viplauncher.VIPLauncher;
import fr.flowarg.viplauncher.auth.mojang.exceptions.AuthenticationUnavailableException;
import fr.flowarg.viplauncher.auth.mojang.exceptions.InvalidCredentialsException;
import fr.flowarg.viplauncher.auth.mojang.exceptions.UserMigratedException;
import fr.flowarg.viplauncher.ui.panels.HomePanel;
import fr.flowarg.viplauncher.ui.panels.LoginPanel;
import javafx.application.Application;
import javafx.stage.Stage;

public class FxApplication extends Application
{
    @Override
    public void start(Stage stage)
    {
        final VIPLauncher launcher = VIPLauncher.getInstance();
        final PanelManager manager = new PanelManager(launcher, stage);
        manager.init();
        try
        {
            if(launcher.refresh())
                manager.showPanel(new HomePanel());
            else manager.showPanel(new LoginPanel());
        } catch (UserMigratedException | AuthenticationUnavailableException | InvalidCredentialsException e)
        {
            launcher.getLogger().printStackTrace(e);
            manager.showPanel(new LoginPanel());
        }
    }
}
